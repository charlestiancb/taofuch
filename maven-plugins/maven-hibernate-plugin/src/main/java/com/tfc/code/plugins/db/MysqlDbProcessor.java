package com.tfc.code.plugins.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MysqlDbProcessor implements DbProcessor {
    private static String driver = "com.mysql.jdbc.Driver";
    private static String url = "jdbc:mysql://192.168.21.243/evolve?useUnicode=true&characterEncoding=utf-8";
    private static String user = "root";
    private static String password = "root";
    private static Connection conn = null;
    private static Statement statement = null;
    private ResultSet rs = null;

    private void initConn() {
        try {
            // 加载驱动程序
            Class.forName(driver);
            // 连续数据库
            conn = DriverManager.getConnection(url, user, password);
            if (!conn.isClosed()) {
                System.err.println("Succeeded connecting to the Database!");
            }
            // statement用来执行SQL语句
            statement = conn.createStatement();
        }
        catch (Exception e) {
            System.err.println("connect database failed!" + e);
        }
    }

    private void closeConn() {
        try {
            if (statement != null) {
                statement.close();
            }
            if (conn != null) {
                conn.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> showAllTables() {
        initConn();
        List<String> tables = new ArrayList<String>();
        String sql = "show tables";
        try {
            rs = statement.executeQuery(sql);
            if (rs != null) {
                while (rs.next()) {
                    tables.add(rs.getString(1));
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        closeConn();
        return tables;
    }

    public List<FieldDescription> showTableStructs(String tableName) {
        initConn();
        List<FieldDescription> tables = new ArrayList<FieldDescription>();
        String sql = "SHOW FULL FIELDS FROM " + tableName;
        try {
            rs = statement.executeQuery(sql);
            if (rs != null) {
                while (rs.next()) {
                    FieldDescription ts = new FieldDescription();
                    ts.setFieldName(rs.getString("Field"));
                    ts.setFieldType(rs.getString("Type"));
                    ts.setCanNull(rs.getString("Null").equalsIgnoreCase("YES"));
                    ts.setKey(rs.getString("Key") != null && rs.getString("Key").equalsIgnoreCase("PRI"));
                    ts.setDefaultValue(rs.getObject("Default"));
                    ts.setExtraInfo(rs.getString("Extra") == null ? "" : rs.getString("Extra"));
                    ts.setComment(rs.getString("Comment") == null ? "" : rs.getString("Comment"));
                    tables.add(ts);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        closeConn();
        return tables;
    }

    @Override
    public void setDriverClass(String driverClass) {
        driver = driverClass;
    }

    @Override
    public void setUrl(String _url) {
        url = _url;
    }

    @Override
    public void setUsername(String username) {
        user = username;
    }

    @Override
    public void setPassword(String password) {
        MysqlDbProcessor.password = password;
    }
}
