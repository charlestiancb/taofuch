package com.tfc.code.plugins.db;

import java.util.List;

public interface DbProcessor {
    public List<String> showAllTables();

    public List<FieldDescription> showTableStructs(String tableName);

    public void setDriverClass(String driverClass);

    public void setUrl(String url);

    public void setUsername(String username);

    public void setPassword(String password);
}
