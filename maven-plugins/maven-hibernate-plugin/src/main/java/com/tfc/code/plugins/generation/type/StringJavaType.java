package com.tfc.code.plugins.generation.type;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * <li>整型的类型</li>
 * 
 * @author taofucheng
 * 
 */
public class StringJavaType implements JavaType {

    @Override
    public String getJavaType() {
        return "String";
    }

    @Override
    public String getImport() {
        return "";
    }

    @Override
    public List<String> getName() {
        List<String> names = new ArrayList<String>();
        names.add("char");
        names.add("varchar");
        names.add("tinytext");
        names.add("text");
        names.add("mediumtext");
        names.add("longtext");
        return names;
    }

}
