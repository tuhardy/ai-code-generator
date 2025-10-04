package com.tlj.aicodegenerator.generator;

import cn.hutool.core.lang.Dict;
import cn.hutool.setting.yaml.YamlUtil;
import com.mybatisflex.codegen.Generator;
import com.mybatisflex.codegen.config.GlobalConfig;
import com.zaxxer.hikari.HikariDataSource;

public class MybatisFlexGenerator {
    //定义要生成的表名
    private static final String[] TABLE_NAMES = {"chat_history"};

    public static void main(String[] args) {
        //读取配置文件
        Dict dict = YamlUtil.loadByPath("application.yaml");
        String url = dict.getByPath("spring.datasource.url");
        String username = dict.getByPath("spring.datasource.username");
        Integer password = dict.getByPath("spring.datasource.password");
        String passwordStr = String.valueOf(password);
        //配置数据源
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(passwordStr);

        //创建配置内容，两种风格都可以。
//        GlobalConfig globalConfig = createGlobalConfigUseStyle1();
        GlobalConfig globalConfig = createGlobalConfigUseStyle2();

        //通过 datasource 和 globalConfig 创建代码生成器
        Generator generator = new Generator(dataSource, globalConfig);

        //生成代码
        generator.generate();
    }
//
//    public static GlobalConfig createGlobalConfigUseStyle1() {
//        //创建配置内容
//        GlobalConfig globalConfig = new GlobalConfig();
//
//        //设置根包
//        globalConfig.setBasePackage("com.test");
//
//        //设置表前缀和只生成哪些表
//        globalConfig.setTablePrefix("tb_");
//        globalConfig.setGenerateTable("tb_account", "tb_account_session");
//
//        //设置生成 entity 并启用 Lombok
//        globalConfig.setEntityGenerateEnable(true);
//        globalConfig.setEntityWithLombok(true);
//        //设置项目的JDK版本，项目的JDK为14及以上时建议设置该项，小于14则可以不设置
//        globalConfig.setEntityJdkVersion(17);
//
//        //设置生成 mapper
//        globalConfig.setMapperGenerateEnable(true);
//
//        //可以单独配置某个列
//        ColumnConfig columnConfig = new ColumnConfig();
//        columnConfig.setColumnName("tenant_id");
//        columnConfig.setLarge(true);
//        columnConfig.setVersion(true);
//        globalConfig.setColumnConfig("tb_account", columnConfig);
//
//        return globalConfig;
//    }

    public static GlobalConfig createGlobalConfigUseStyle2() {
        //创建配置内容
        GlobalConfig globalConfig = new GlobalConfig();

        //设置根包
        globalConfig.getPackageConfig()
                .setBasePackage("com.tlj.aicodegenerator.generateResult");

        //设置表前缀和只生成哪些表，setGenerateTable 未配置时，生成所有表
        globalConfig.getStrategyConfig()
                .setGenerateTable(TABLE_NAMES)
                .setLogicDeleteColumn("isDelete");

        //设置生成 entity 并启用 Lombok
        globalConfig.enableEntity()
                .setWithLombok(true)
                .setJdkVersion(21);

        //设置生成 mapper
        globalConfig.enableMapper();
        globalConfig.enableMapperXml();
        //设置生成 service 和 serviceImpl
        globalConfig.enableService();
        globalConfig.enableServiceImpl();
        //设置生成 controller
        globalConfig.enableController();
        //设置作者信息
        globalConfig.getJavadocConfig()
                .setAuthor("<a href='https://github.com/tlj-x'>tlj</a>")
                .setSince("");
        return globalConfig;
    }
}