package com.tfc.code.plugins.generation.type;

import java.util.ArrayList;
import java.util.List;

public class ObjectJavaType implements JavaType {

    @Override
    public String getJavaType() {
        return "Object";
    }

    @Override
    public String getImport() {
        return "";
    }

    @Override
    public List<String> getName() {
        List<String> names = new ArrayList<String>();
        names.add("object");
        return names;
    }

}
