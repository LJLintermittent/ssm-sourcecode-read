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
@SuppressWarnings("all")
public class MybatisTest {
    public static void main(String[] args) throws IOException {

        /**
         * TODO mybatis的执行流程
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
         *
         * mybatis在数据操作阶段的主要步骤如下：
         * 1.建立连接数据库的sqlsession
         * 2.查找当前映射接口中抽象方法对应的数据库操作节点mapperstatement，根据该节点生成接口的实现
         * 3.接口的实现拦截对映射接口中抽象方法的调用，并将其转换为数据查询操作
         * 4.对数据库操作节点中的数据库操作语句进行多次处理，最终得到标准的sql语句
         * 5.尝试从缓存中查找操作结果，如果找到则返回，如果找不到则继续从数据库中查询
         * 6.从数据库中查询结果
         * 7.处理结果集，建立输出对象，根据输出结果遍历然后给对象赋值
         * 8.在缓存中记录查询结果
         * 9.返回查询结果
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
            2.从配置文件的根节点开始逐层解析配置文件，也包括相关的映射文件，解析过程中不断的将结果放入Configuration对象中
            3.以配置好的Configuration对象为参数，获取一个sqlSessionFactory对象
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
