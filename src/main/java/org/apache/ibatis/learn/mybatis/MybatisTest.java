package org.apache.ibatis.learn.mybatis;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;

/**
 * Description:
 * date: 2021/6/15 11:56
 * Package: org.apache.ibatis.learn.mybatis
 *
 * @author 李佳乐
 * @version 1.0
 */
public class MybatisTest {
    public static void main(String[] args) throws IOException {

        /**
         * orm对象关系映射框架，在我的理解中主要就是完成三项工作，分别是三个映射：
         * 1.将映射文件中的sql语句与映射接口中的抽象方法做一个映射
         * 2.sql语句中的参数与方法的入参做一个映射
         * 3.sql语句的返回结果与方法的返回结果做一个映射
         *
         * 基于这三种映射，可以总结出mybatis的核心功能：
         * 1，将xml文件中的带各种if foreach复杂标签的sql语句解析为干净的sql语句
         * 2.将数据库操作节点和映射接口中的抽象方法进行绑定，在抽象方法被调用的时候执行数据库操作
         * 3.将方法的入参转换为sql语句的占位符参数
         * 4.将sql语句的返回结果封装成对象
         *
         * Resources.getResourceAsStream(null);
         * 这里面借助了类加载器，来负责加载类的对象，给定类的二进制名，类加载器会尝试定位和生成该名称的文件
         * 因此，类加载器具有读取外部资源的能力，这里正是利用了类加载器的这种能力
         * 可以说io包中的resource类和ClassLoaderWrapper负责mybatis对外部文件的读写
         */
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);

        DataSource dataSource = getDataSource();
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("development", transactionFactory, dataSource);
        Configuration configuration = new Configuration(environment);
        configuration.addMapper(BlogMapper.class);
        /*
            这个建造者模式方法执行完毕后，至此mybatis的初始化阶段完成，在初始化阶段，
            mybatis主要做了以下工作:
            1.根据配置文件的位置，获取输入流
            2.从配置文件的根节点开始
         */
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        SqlSession session = sqlSessionFactory.openSession();
        BlogMapper blogMapper = session.getMapper(BlogMapper.class);
        System.out.println(blogMapper.selectDeptById(1));
    }

    private static DataSource getDataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl("jdbc:mysql://localhost:3306/mybatis?characterEncoding=utf8&useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true");
        druidDataSource.setUsername("root");
        druidDataSource.setPassword("1234");
        return druidDataSource;
    }
}
