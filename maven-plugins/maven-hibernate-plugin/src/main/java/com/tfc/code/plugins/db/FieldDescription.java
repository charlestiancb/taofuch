package com.tfc.code.plugins.db;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.tfc.code.plugins.generation.type.JavaType;
import com.tfc.code.plugins.generation.type.JavaTypeFactory;

/**
 * <li>表中每个字段的描述信息</li>
 * 
 * @author taofucheng
 */
public class FieldDescription implements Serializable {
    private static final long serialVersionUID = -5365719152162455903L;
    private String fieldName;
    private String fieldType;
    private String fieldLength;
    private boolean canNull;
    private boolean key;
    private Object defaultValue;
    private String comment;
    /**
     * extra info, such as auto_increment
     */
    private String extraInfo;
    //
    /** 字段类型 */
    private JavaType fieldJavaType = JavaTypeFactory.lookupByType(getFieldType());
    /** 这个字段上所有相关的引入 */
    private Set<String> imports = new HashSet<String>();

    /**
     * 获取该字段会引入的所有的引入定义
     * 
     * @return
     */
    public Set<String> getAllImport() {
        // TODO 完善引入
        return imports;
    }

    public List<String> getMethodsDefine() {
        List<String> getterAndSetter = new ArrayList<String>();
        String upperName = getFieldProperty().substring(0, 1).toUpperCase() + getFieldProperty().substring(1);
        getterAndSetter.add("    public " + fieldJavaType.getJavaType() + " get" + upperName + "() {");
        getterAndSetter.add("        return " + getFieldProperty() + ";");
        getterAndSetter.add("    }");
        getterAndSetter.add("");
        getterAndSetter.add("    public void set" + upperName + "(" + fieldJavaType.getJavaType() + " "
                + getFieldProperty() + ") {");
        getterAndSetter.add("        this." + getFieldProperty() + " = " + getFieldProperty() + ";");
        getterAndSetter.add("    }");
        return getterAndSetter;
    }

    /**
     * 获取属性的java定义
     * 
     * @return
     */
    public List<String> getFieldDefine() {
        List<String> def = new ArrayList<String>();
        // 注释
        if (StringUtils.isNotBlank(getComment())) {
            def.add("/** " + getComment() + " */");
        }
        // 注解
        if (getAnnotations() != null && !getAnnotations().isEmpty()) {
            def.addAll(getAnnotations());
        }
        // 定义
        StringBuffer sb = new StringBuffer();
        sb.append("private ");
        sb.append(fieldJavaType.getJavaType());
        sb.append(" ");
        sb.append(getFieldProperty());
        sb.append(";");
        def.add(sb.toString());
        return def;
    }

    private Set<String> getAnnotations() {
        // TODO Auto-generated method stub
        // 主键引入id、自增长(自增加方式接口方式)
        return null;
    }

    /** 返回字段名称对应的java属性形式，如：user_name->userName */
    public String getFieldProperty() {
        String[] names = fieldName.split("_");
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
        // 将第一个字母小写
        sb.replace(0, 1, sb.substring(0, 1).toLowerCase());
        return sb.toString();
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        // 处理类型中带长度描述信息的。
        if (StringUtils.isBlank(fieldType)) {
            return;
        }
        fieldType = fieldType.trim();
        if (fieldType.indexOf("(") > 0 && fieldType.indexOf(")") > 0 && fieldType.indexOf(")") > fieldType.indexOf("(")) {
            // 如果有“()”，说明有长度等说明，将长度拿出来单独存放！
            int start = fieldType.indexOf("(");
            int end = fieldType.indexOf(")");
            this.fieldType = fieldType.substring(0, start);
            this.fieldLength = fieldType.substring(start + 1, end).trim();
        }
        else {
            this.fieldType = fieldType;
        }
    }

    public boolean isCanNull() {
        return canNull;
    }

    public void setCanNull(boolean canNull) {
        this.canNull = canNull;
    }

    public boolean isKey() {
        return key;
    }

    public void setKey(boolean key) {
        this.key = key;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(getFieldName());
        sb.append(";");
        sb.append(getFieldType());
        sb.append(";");
        sb.append(getFieldLength());
        sb.append(";");
        sb.append(isCanNull());
        sb.append(";");
        sb.append(isKey());
        sb.append(";");
        sb.append(getDefaultValue());
        sb.append(";");
        sb.append(getComment());
        return sb.toString();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * @return the fieldLength
     */
    public String getFieldLength() {
        return fieldLength;
    }
}
