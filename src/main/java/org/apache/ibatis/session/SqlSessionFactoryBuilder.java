/**
 * Copyright 2009-2019 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.ibatis.session;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Properties;

import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.exceptions.ExceptionFactory;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;

/**
 * Builds {@link SqlSession} instances.
 *
 * @author Clinton Begin
 */
@SuppressWarnings("all")
public class SqlSessionFactoryBuilder {

    public SqlSessionFactory build(Reader reader) {
        return build(reader, null, null);
    }

    public SqlSessionFactory build(Reader reader, String environment) {
        return build(reader, environment, null);
    }

    public SqlSessionFactory build(Reader reader, Properties properties) {
        return build(reader, null, properties);
    }

    /**
     * 建造一个SqlSessionFactory对象
     *
     * @param reader      读取字符流的抽象类
     * @param environment 环境信息
     * @param properties  配置信息
     * @return SqlSessionFactory对象
     */
    public SqlSessionFactory build(Reader reader, String environment, Properties properties) {
        try {
            // 传入配置文件，创建一个XMLConfigBuilder类
            XMLConfigBuilder parser = new XMLConfigBuilder(reader, environment, properties);
            // 分两步：
            // 1、解析配置文件，得到配置文件对应的Configuration对象
            // 2、根据Configuration对象，获得一个DefaultSqlSessionFactory
            return build(parser.parse());
        } catch (Exception e) {
            throw ExceptionFactory.wrapException("Error building SqlSession.", e);
        } finally {
            ErrorContext.instance().reset();
            try {
                reader.close();
            } catch (IOException e) {
            }
        }
    }

    public SqlSessionFactory build(InputStream inputStream) {
        return build(inputStream, null, null);
    }

    public SqlSessionFactory build(InputStream inputStream, String environment) {
        return build(inputStream, environment, null);
    }

    public SqlSessionFactory build(InputStream inputStream, Properties properties) {
        return build(inputStream, null, properties);
    }

    public SqlSessionFactory build(InputStream inputStream, String environment, Properties properties) {
        try {
            XMLConfigBuilder parser = new XMLConfigBuilder(inputStream, environment, properties);
            //parser.parse()返回一个Configuration
            //build(parser.parse())然后有调用一个build方法，这个重载build的入参是Configuration
            /*
             <?xml version="1.0" encoding="UTF-8"?>
                    <!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
                        "http://mybatis.org/dtd/mybatis-3-config.dtd">
               <configuration>
                    <!-- 1.properties属性引入外部配置文件 -->
                    <properties resource="config/db.properties">
                        <!-- property里面的属性全局均可使用 -->
                        <property name="jdbc.username" value="root"/>
                        <property name="jdbc.password" value="123456"/>
                        <!-- 启用默认值特性，这样${}拼接符才可以设置默认值 -->
                        <property name="org.apache.ibatis.parsing.PropertyParser.enable-default-value" value="true"/>
                    </properties>

                    <!-- 2.全局配置参数 -->
                    <settings>
                        <!-- 指定 MyBatis 所用日志的具体实现，未指定时将自动查找 -->
                        <setting name="logImpl" value="STDOUT_LOGGING"/>
                        <!-- 开启自动驼峰命名规则（camel case）映射 -->
                        <setting name="mapUnderscoreToCamelCase" value="true"/>
                        <!-- 开启延迟加载开关 -->
                        <setting name="lazyLoadingEnabled" value="true"/>
                        <!-- 将积极加载改为消极加载（即按需加载）,默认值就是false -->
                        <setting name="aggressiveLazyLoading" value="false"/>
                        <!-- 打开全局缓存开关（二级环境），默认值就是true -->
                        <setting name="cacheEnabled" value="true"/>
                    </settings>

                    <!-- 3.别名设置 -->
                    <typeAliases>
                        <typeAlias alias="user" type="com.pjb.mybatis.po.User"/>
                        <typeAlias alias="teacher" type="com.pjb.mybatis.po.Teacher"/>
                        <typeAlias alias="integer" type="java.lang.Integer"/>
                    </typeAliases>

                    <!-- 4.类型转换器 -->
                    <typeHandlers>
                        <!-- 一个简单的类型转换器 -->
                        <typeHandler handler="com.pjb.mybatis.example.ExampleTypeHandler"/>
                    </typeHandlers>

                    <!-- 5.对象工厂 -->
                    <objectFactory type="com.pjb.mybatis.example.ExampleObjecFactory">
                        <!-- 对象工厂注入参数 -->
                        <property name="someProperty" value="100"/>
                    </objectFactory>

                    <!-- 6.插件 -->
                    <plugins>
                        <plugin interceptor="com.pjb.mybatis.example.ExamplePlugin">
                            <property name="someProperty" value="100"/>
                        </plugin>
                    </plugins>

                    <!-- 7.environments数据库环境配置 -->
                    <!-- 和Spring整合后environments配置将被废除 -->
                    <environments default="development">
                        <environment id="development">
                            <!-- 使用JDBC事务管理 -->
                            <transactionManager type="JDBC"/>
                            <!-- 数据库连接池 -->
                            <dataSource type="POOLED">
                                <property name="driver" value="${jdbc.driver}"/>
                                <property name="url" value="${jdbc.url}"/>
                                <property name="username" value="${jdbc.username:root}"/>
                                <property name="password" value="${jdbc.password:123456}"/>
                            </dataSource>
                        </environment>
                    </environments>

                    <!-- 8.加载映射文件 -->
                    <mappers>
                        <mapper resource="com.pjb.mybatis.sqlmap.UserMapper.xml"/>
                        <mapper resource="com.pjb.mybatis.sqlmap.OtherMapper.xml"/>
                    </mappers>
                    </configuration>
             */
            return build(parser.parse());
        } catch (Exception e) {
            throw ExceptionFactory.wrapException("Error building SqlSession.", e);
        } finally {
            ErrorContext.instance().reset();
            try {
                inputStream.close();
            } catch (IOException e) {
                // Intentionally ignore. Prefer previous error.
            }
        }
    }

    /**
     * 根据配置信息建造一个SqlSessionFactory对象
     *
     * @param config 配置信息
     * @return SqlSessionFactory对象
     */
    public SqlSessionFactory build(Configuration config) {
        // 最终通过输入流的方式读取xml配置文件，读到了以后将这个输入流包装为一个XMLConfigBuilder
        // 给这个对象起名parser，代表这个对象的作用是来解析流的内容
        // 调用这个对象的parse方法，方法开始 做了一个防止重复解析的判断，
        // 重点是parseconfiguration，这个方面里面对于每一张标签都写了一个相应的解析方法
        // 并且方法有顺序性，决定了xml文件中配置的标签的顺序性
        // 最终parse方法返回一个Configuration对象
        // 然后这个建造者模式需要完成自己创建对象的职责，SqlSessionFactory是一个接口，需要创建一个实现类DefaultSqlSessionFactory
        // 并且通过构造器注入的方式将Configuration进行了一个注入
        return new DefaultSqlSessionFactory(config);
    }

}
