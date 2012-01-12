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
public class IntegerJavaType implements JavaType {

    @Override
    public String getJavaType() {
        return "Integer";
    }

    @Override
    public String getImport() {
        return "";
    }

    @Override
    public List<String> getName() {
        List<String> names = new ArrayList<String>();
        names.add("int");
        names.add("integer");
        return names;
    }

}
