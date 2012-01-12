package com.tfc.code.plugins;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tfc.code.plugins.db.DbProcessor;
import com.tfc.code.plugins.db.FieldDescription;
import com.tfc.code.plugins.db.MysqlDbProcessor;
import com.tfc.code.plugins.generation.EntityGeneration;

/**
 * Combine css from "import" keyword
 * 
 * @author taofucheng
 * @goal db2entity
 * @phase process-resources
 * @threadSafe
 * @requiresDependencyResolution runtime
 */
public class Db2AnotationEntityMojo extends AbstractMojo {
    /**
     * general hibernate annotation entity cache file.
     * 
     * @parameter expression="${basedir}/src/main/resources/context/init/db2hbnEntity.xml"
     */
    private File tmpCacheFile;
    /**
     * general hibernate annotation entity cache file.
     * 
     * @parameter expression="${basedir}/src/main/java/com/tfc/evolve/model"
     */
    private File entityTarget;
    /**
     * @parameter expression="com.tfc.evolve.model"
     */
    private String packageName;
    /**
     * @parameter default-value= "com.mysql.jdbc.Driver"
     */
    private String driverClass;

    /**
     * @parameter default-value= "jdbc:mysql://192.168.21.243/evolve?useUnicode=true&characterEncoding=utf-8"
     */
    private String connUrl;
    /**
     * @parameter default-value= "root"
     */
    private String connUsername;
    /**
     * @parameter default-value= "root"
     */
    private String connPassword;
    /**
     * @parameter
     */
    private DbProcessor processor = new MysqlDbProcessor();
    private SAXBuilder saxB = new SAXBuilder();// 创建一个SAXBuilder对象

    private static Map<String, List<FieldDescription>> cacheStruts = null;
    private Map<String, List<FieldDescription>> differenctStruts = new HashMap<String, List<FieldDescription>>();

    public void execute() throws MojoExecutionException, MojoFailureException {
        // TODO 缓存对比有问题！
        getLog().info("start db2Entity...");
        initProcessor();
        readCache();
        // read all tables
        List<String> tables = processor.showAllTables();
        // compare every table structs
        if (tables != null && !tables.isEmpty()) {
            for (String table : tables) {
                // TODO 将数据库中不存在的表的缓存去除
                compareAndMergeCache(table, processor.showTableStructs(table));
            }
        }
        // add different field to entity
        if (!differenctStruts.isEmpty()) {
            for (String tableName : differenctStruts.keySet()) {
                addField2Entity(tableName, differenctStruts.get(tableName));
            }
            rewriteCache();
            differenctStruts.clear();
        }
    }

    private void initProcessor() {
        if (processor == null) {
            processor = new MysqlDbProcessor();
        }
        processor.setDriverClass(driverClass);
        processor.setUrl(connUrl);
        processor.setUsername(connUsername);
        processor.setPassword(connPassword);
    }

    @SuppressWarnings("unchecked")
    private void readCache() {
        if (cacheStruts == null) {
            cacheStruts = new HashMap<String, List<FieldDescription>>();
        }
        // 文件如果不在，则返回一个空map！
        if (!tmpCacheFile.isFile()) {
            return;
        }
        try {
            Document doc = saxB.build(tmpCacheFile);
            Element root = doc.getRootElement();
            List<Element> tables = root.getChildren("table");
            if (tables == null || tables.isEmpty()) {
                return;
            }
            else {
                for (Element table : tables) {
                    String tableName = table.getAttribute("name").getValue();
                    List<FieldDescription> _fields = new ArrayList<FieldDescription>();
                    List<Element> fields = table.getChildren("field");
                    if (fields != null && !fields.isEmpty()) {
                        // 如果有字段，则进行解析转换为对应的结构
                        for (Element field : fields) {
                            String content = field.getText();
                            if (StringUtils.isNotBlank(content)) {
                                _fields.add(JSON.toJavaObject(JSONObject.parseObject(content), FieldDescription.class));
                            }
                        }
                    }
                    cacheStruts.put(tableName, _fields);
                }
            }
        }
        catch (JDOMException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 重写缓存的过程
     */
    private void rewriteCache() {
        try {
            if (tmpCacheFile.exists()) {
                FileUtils.forceDelete(tmpCacheFile);
            }
            if (!tmpCacheFile.getParentFile().exists()) {
                tmpCacheFile.getParentFile().mkdirs();
            }
            tmpCacheFile.createNewFile();
            //
            if (cacheStruts == null || cacheStruts.isEmpty()) {
                return;
            }
            Document document = new Document();
            Element root = new Element("root");
            document.addContent(root);
            // 将每个表的结构写入文件！
            for (String table : cacheStruts.keySet()) {
                Element _table = new Element("table");
                _table.setAttribute("name", table);
                root.addContent(_table);
                List<FieldDescription> fields = cacheStruts.get(table);
                if (fields != null && !fields.isEmpty()) {
                    for (FieldDescription field : fields) {
                        Element _field = new Element("field");
                        _field.setText(JSON.toJSONString(field));
                        _table.addContent(_field);
                    }
                }
            }
            Format format = Format.getPrettyFormat();
            format.setIndent("  ");
            format.setEncoding("UTF-8");
            XMLOutputter out = new XMLOutputter(format);
            out.output(document, new FileWriter(tmpCacheFile));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addField2Entity(String tableName, List<FieldDescription> fields) {
        // 如果文件不存在，则先生成相应的实体！
        File entity = new File(entityTarget, parseName2Entity(tableName));
        EntityGeneration generator = new EntityGeneration(packageName, entity);
        if (!entity.isFile()) {
            try {
                if (!entityTarget.exists()) {
                    entityTarget.mkdirs();
                }
                entity.createNewFile();
                // 增加类方面的描述信息
                generator.addEntityInfo();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 将各个不同的属性加入到实体中，并生成getter/setter方法
        generator.addField2Entity(fields);
    }

    /**
     * 将表名转换为实体的名称
     * 
     * @param tableName
     * @return
     */
    private String parseName2Entity(String tableName) {
        if (StringUtils.isBlank(tableName)) {
            return "NoName.java";
        }
        String[] names = tableName.split("_");
        StringBuffer sb = new StringBuffer();
        for (String name : names) {
            if (StringUtils.isBlank(name)) {
                continue;
            }
            else if (name.length() == 1) {
                sb.append(name.substring(0, 1).toUpperCase());
            }
            else {
                sb.append(name.substring(0, 1).toUpperCase() + name.substring(1));
            }
        }
        return sb.toString() + ".java";
    }

    /**
     * compare and merge, and find the difference.
     * 
     * @param tableName 表名
     * @param realDbStruct 该表对应的真实的数据库中的结构
     */
    private void compareAndMergeCache(String tableName, List<FieldDescription> realDbStruct) {
        if (realDbStruct == null || realDbStruct.isEmpty()) {
            return;
        }
        List<FieldDescription> struct = cacheStruts.get(tableName);
        List<FieldDescription> differences = differenctStruts.get(tableName);
        if (struct == null) {
            struct = new ArrayList<FieldDescription>();
        }
        if (differences == null) {
            differences = new ArrayList<FieldDescription>();
        }
        for (FieldDescription ts : realDbStruct) {
            if (!struct.contains(ts)) {
                differences.add(ts);
            }
        }
        differenctStruts.put(tableName, differences);
        // if have difference,merge it!
        if (differenctStruts.get(tableName) != null) {
            cacheStruts.remove(tableName);
            cacheStruts.put(tableName, realDbStruct);
        }
    }

    public File getTmpCacheFile() {
        return tmpCacheFile;
    }

    public void setTmpCacheFile(File tmpCacheFile) {
        this.tmpCacheFile = tmpCacheFile;
    }

    public File getEntityTarget() {
        return entityTarget;
    }

    public void setEntityTarget(File entityTarget) {
        this.entityTarget = entityTarget;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public String getConnUrl() {
        return connUrl;
    }

    public void setConnUrl(String connUrl) {
        this.connUrl = connUrl;
    }

    public String getConnUsername() {
        return connUsername;
    }

    public void setConnUsername(String connUsername) {
        this.connUsername = connUsername;
    }

    public String getConnPassword() {
        return connPassword;
    }

    public void setConnPassword(String connPassword) {
        this.connPassword = connPassword;
    }

    public DbProcessor getProcessor() {
        return processor;
    }

    public void setProcessor(DbProcessor processor) {
        this.processor = processor;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
