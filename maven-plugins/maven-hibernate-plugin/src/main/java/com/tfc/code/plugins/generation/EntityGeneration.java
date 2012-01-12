package com.tfc.code.plugins.generation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.tfc.code.plugins.db.FieldDescription;

/**
 * <li>实体对象生成器</li>
 * 
 * @author taofucheng
 */
public class EntityGeneration {
    private String packageName = "";
    private File entityFile;
    private List<FieldDescription> fields;

    public EntityGeneration(String packageName, File entity) {
        this.packageName = packageName;
        this.entityFile = entity;
    }

    /**
     * 添加整个类的一些描述信息，如package信息等
     */
    public void addEntityInfo() {
        List<String> content = new ArrayList<String>();
        content.add("package com.tfc.evolve.model;");
        content.add("");
        content.add("import javax.persistence.Entity;");
        content.add("");
        content.add("import org.hibernate.annotations.GenericGenerator;");
        content.add("");
        content.add("@Entity");
        content.add("public class User {");
        content.add("");
        content.add("}");
        try {
            FileUtils.writeLines(entityFile, "UTF-8", content);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将字段信息插入到实体中。
     * 
     * @param fields2
     */
    public void addField2Entity(List<FieldDescription> fields2) {
        if (fields2 == null || fields2.isEmpty()) {
            return;
        }
        // 先将整个类读取进入，然后将Import单独存放一份
        try {
            List<String> content = FileUtils.readLines(entityFile, "UTF-8");
            Set<String> imports = parseImports(content);
            List<String> fields = parseFields(content);
            List<String> getterAndSetter = parseGetterAndSetters(content);
            for (FieldDescription field : fields2) {
                fields.addAll(field.getFieldDefine());
                if (field.getAllImport() != null && !field.getAllImport().isEmpty()) {
                    imports.addAll(field.getAllImport());
                }
                getterAndSetter.addAll(field.getMethodsDefine());
            }
            // 将这些部分的内容进行连接，并重新写入文件！
            List<String> result = new ArrayList<String>();
            for (String c : content) {
                if (c.startsWith("package ")) {
                    result.add(c);
                }
                else if (c.startsWith("@Entity")) {
                    result.addAll(imports);
                    result.add(c);
                }
                else if (c.startsWith("public class ")) {
                    result.add(c);
                    result.addAll(fields);
                    result.addAll(getterAndSetter);
                }
                else {
                    result.add(c);
                }
            }
            if (entityFile.isFile()) {
                FileUtils.forceDelete(entityFile);
                entityFile.createNewFile();
            }
            FileUtils.writeLines(entityFile, "UTF-8", result);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<String> parseGetterAndSetters(List<String> content) {
        List<String> fieldsMethods = new ArrayList<String>();
        if (content == null || content.isEmpty()) {
            return fieldsMethods;
        }
        // 找出开头部分与结束部分，其余都加入到getter/setter中
        int begin = 0;
        int end = 0;
        for (int i = 0; i < content.size(); i++) {
            String line = content.get(i);
            // 注意区分格式
            if ((line.startsWith("public class ") && line.endsWith(" {"))
                    || (i > 1 && content.get(i - 1).startsWith("public class ") && line.trim().equals("{"))) {
                begin = i + 1;
            }
            else if (line.trim().equals("}")) {
                end = i - 1;
            }
        }
        // 注意空结构体的情况
        if (end > begin) {
            for (int i = begin; i <= end;) {
                fieldsMethods.add(content.get(i));
                // 将相应的内容删除
                content.remove(i);
                end--;
            }
        }
        return fieldsMethods;
    }

    private List<String> parseFields(List<String> content) {
        List<String> fields = new ArrayList<String>();
        if (content == null || content.isEmpty()) {
            return fields;
        }
        for (int i = 0; i < content.size();) {
            String line = content.get(i).trim();
            if (line.startsWith("private ") && line.endsWith(";")) {
                content.remove(i);// 先移除，免得到后面操作之后再移除就不准确了！
                // 注释+注解
                for (int j = i - 1; j >= 0; j--) {
                    String l = content.get(j).trim();
                    if (l.startsWith("@") || (l.startsWith("/** ") && l.endsWith(" */"))) {
                        fields.add("    " + l);
                        content.remove(j);
                        --i;// i的指定也要跟着往上走
                    }
                    else {
                        break;
                    }
                }
                // 定义
                fields.add("    " + line);
            }
            else {
                i++;
            }
        }
        return fields;
    }

    private Set<String> parseImports(List<String> content) {
        Set<String> imports = new LinkedHashSet<String>();
        if (content == null || content.isEmpty()) {
            return imports;
        }
        for (int i = 0; i < content.size();) {
            String line = content.get(i);
            if (line.startsWith("import ")) {
                imports.add(line);
                content.remove(i);
            }
            else {
                i++;
            }
        }
        return imports;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public File getEntityFile() {
        return entityFile;
    }

    public void setEntityFile(File entityFile) {
        this.entityFile = entityFile;
    }

    public List<FieldDescription> getFields() {
        return fields;
    }

    public void setFields(List<FieldDescription> fields) {
        this.fields = fields;
    }

}
