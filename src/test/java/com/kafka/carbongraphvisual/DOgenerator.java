package com.kafka.carbongraphvisual;


import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

public class DOgenerator {
    public static void main(String[] args) {
        //====================配置变量区域=====================//
        String author="Kafka";//生成文件的作者，可以不填
        String rootPackage="com.kafka.carbongraphvisual";//生成的entity、controller、service等包所在的公共上一级包路径全限定名
        String moduleName="carbon-graph-visual";
        //数据库配置
        String url="jdbc:mysql:///graduation_design?useSSL=false&characterEncoding=utf8";
        String driverClassName="com.mysql.jdbc.Driver";//或者com.mysql.cj.jdbc.Driver
        String username="root";
        String password="root";
        String tableNames="transaction";//表名，多个使用,分隔
        //====================配置变量区域=====================//

        // 代码生成器
        AutoGenerator generator = new AutoGenerator();
        // 全局配置
        GlobalConfig globalConfig = new GlobalConfig();
//        String projectPath = System.getProperty("user.dir");
        String projectPath = "D:/work/JAVA";
        globalConfig.setOutputDir(projectPath +"/"+moduleName+"/src/main/java");
        globalConfig.setFileOverride(false);//是否覆盖已有文件，默认false
        globalConfig.setOpen(false);//是否打开输出目录
        globalConfig.setAuthor(author);
        globalConfig.setServiceName("%sManager");//去掉service接口的首字母I
        globalConfig.setBaseResultMap(true);//开启 BaseResultMap
        globalConfig.setDateType(DateType.ONLY_DATE);//只使用 java.util.date代替
        globalConfig.setIdType(IdType.ASSIGN_ID);//分配ID (主键类型为number或string）
        generator.setGlobalConfig(globalConfig);

        // 数据源配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUrl(url);
        dataSourceConfig.setDbType(DbType.MYSQL);//数据库类型
        dataSourceConfig.setDriverName(driverClassName);
        dataSourceConfig.setUsername(username);
        dataSourceConfig.setPassword(password);
        generator.setDataSource(dataSourceConfig);

        // 包配置
        PackageConfig packageConfig = new PackageConfig();
        //packageConfig.setModuleName(scanner("模块名"));
        packageConfig.setParent(rootPackage);//例：org.jeecg.modules.xqxy
        generator.setPackageInfo(packageConfig);


        // 策略配置：配置根据哪张表生成代码
        StrategyConfig strategy = new StrategyConfig();
        strategy.setInclude(tableNames);//表名，多个英文逗号分割（与exclude二选一配置）
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        //strategy.setSuperEntityClass("你自己的父类实体,没有就不用设置!");
        strategy.setEntityLombokModel(true);//lombok模型，@Accessors(chain = true)setter链式操作
        strategy.setRestControllerStyle(true);//controller生成@RestController
        strategy.setEntityTableFieldAnnotationEnable(true);//是否生成实体时，生成字段注解

        generator.setStrategy(strategy);
        generator.setTemplateEngine(new FreemarkerTemplateEngine());
        generator.execute();
    }
}
