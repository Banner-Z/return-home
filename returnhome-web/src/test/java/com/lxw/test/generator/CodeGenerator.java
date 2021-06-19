package com.lxw.test.generator;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.Scanner;

public class CodeGenerator {

        /**
         * <p>
         * 读取控制台内容
         * </p>
         */
        public static String scanner(String tip) {
            Scanner scanner = new Scanner(System.in);
            StringBuilder help = new StringBuilder();
            help.append("请输入" + tip + "：");
            System.out.println(help.toString());
            if (scanner.hasNext()) {
                String ipt = scanner.next();
                if (StringUtils.isNotBlank(ipt)) {
                    return ipt;
                }
            }
            throw new MybatisPlusException("请输入正确的" + tip + "！");
        }

        public static void main(String[] args) {
            // 创建代码生成器实体对象
            AutoGenerator mpg = new AutoGenerator();

            // 全局配置
            GlobalConfig gc = new GlobalConfig();
            gc.setOutputDir(scanner("输入项目路径") + "/src/main/java");
            gc.setAuthor("lxw");
            //生成后打开资源管理器
            gc.setOpen(false);
            //重新生成是否自动覆盖文件
            gc.setFileOverride(true);
            //%s代表占位符 mp生成service层代码 默认名称接口第一个字母为I
            gc.setServiceName("%sService");
            //设置主键生成策略---自增长
            gc.setIdType(IdType.AUTO);
            //设置Date类型
            gc.setDateType(DateType.ONLY_DATE);
            //开启实体属性 Swagger2 注解
            gc.setSwagger2(true);
            mpg.setGlobalConfig(gc);

            // 数据源配置
            DataSourceConfig dsc = new DataSourceConfig();
            dsc.setUrl("jdbc:mysql://localhost:3306/returnhome?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT");
            // dsc.setSchemaName("public");
            dsc.setDriverName("com.mysql.cj.jdbc.Driver");
            dsc.setUsername("root");
            dsc.setPassword("123456");
            dsc.setDbType(DbType.MYSQL);
            mpg.setDataSource(dsc);

            // 包配置
            PackageConfig pc = new PackageConfig();
            pc.setModuleName(scanner("请输入模块名"));
            pc.setParent("com.lxw");
            pc.setController("controller");
            pc.setService("service");
            pc.setServiceImpl("service.impl");
            pc.setMapper("mapper");
            pc.setEntity("entity");
            pc.setXml("mapper");
            mpg.setPackageInfo(pc);

            // 策略配置
            StrategyConfig strategy = new StrategyConfig();
            //设置哪些表需要自动生成
            strategy.setInclude(scanner("表名，多个英文逗号分割").split(","));
            //实体类名（表名）驼峰命名
            strategy.setNaming(NamingStrategy.underline_to_camel);
            //列名驼峰命名
            strategy.setColumnNaming(NamingStrategy.underline_to_camel);
            //lombok模型（简化getter和setter）
            strategy.setEntityLombokModel(true);
            //使用@restController注解
            strategy.setRestControllerStyle(true);
            //驼峰转连接符
            strategy.setControllerMappingHyphenStyle(true);
            //忽略表中生成实体类的前缀
            strategy.setTablePrefix("tb_");
            mpg.setStrategy(strategy);
            mpg.execute();

        }

}

