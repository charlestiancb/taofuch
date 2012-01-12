package com.tfc.code.plugins.generation.type;

import java.util.List;

public interface JavaType {
    /**
     * 该类型的名称
     * 
     * @return
     */
    public List<String> getName();

    /**
     * 获取对应的java类型
     * 
     * @return
     */
    public String getJavaType();

    /**
     * 获取该java类型及注解对应的引入类
     * 
     * @return
     */
    public String getImport();
}
