package com.sparkle.util;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;

public class ActivitiUtil {

    /*  1.通过代码形式创建
     *  - 取得ProcessEngineConfiguration对象
     *  - 设置数据库连接属性
     *  - 设置创建表的策略 （当没有表时，自动创建表）
     *  - 通过ProcessEngineConfiguration对象创建 ProcessEngine 对象
     * */
    public void createActivitiEngine() {
        //取得ProcessEngineConfiguration对象
        ProcessEngineConfiguration engineConfiguration = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        //设置数据库连接属性
        engineConfiguration.setJdbcDriver("com.mysql.jdbc.Driver");
        engineConfiguration.setJdbcUrl("jdbc:mysql://localhost:3306/activitiDB?createDatabaseIfNotExist=true" + "&useUnicode=true&characterEncoding=utf8");
        engineConfiguration.setJdbcUsername("root");
        engineConfiguration.setJdbcPassword("root");
        // 设置创建表的策略 （当没有表时，自动创建表）
        String DB_SCHEMA_UPDATE_FALSE = "false";//不会自动创建表，没有表，则抛异常
        String DB_SCHEMA_UPDATE_CREATE_DROP = "create-drop";//先删除，再创建表
        String DB_SCHEMA_UPDATE_TRUE = "true";//假如没有表，则自动创建
        engineConfiguration.setDatabaseSchemaUpdate("true");
        //通过ProcessEngineConfiguration对象创建 ProcessEngine 对象
        ProcessEngine processEngine = engineConfiguration.buildProcessEngine();
        System.out.println("流程引擎创建成功!");
    }


}