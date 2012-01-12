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
public class DateJavaType implements JavaType {

    @Override
    public String getJavaType() {
        return "Date";
    }

    @Override
    public String getImport() {
        return "import java.util.Date;";
    }

    @Override
    public List<String> getName() {
        List<String> names = new ArrayList<String>();
        names.add("date");
        names.add("timestamp");
        names.add("datetime");
        return names;
    }

}
