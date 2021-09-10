package org.apache.ibatis.learn.mybatis;

import org.apache.ibatis.annotations.Param;

/**
 * Description:
 * date: 2021/6/15 11:58
 * Package: org.apache.ibatis.learn.mybatis
 *
 * @author 李佳乐
 * @version 1.0
 */
@SuppressWarnings("all")
public interface BlogMapper {

    /**
     * DAO接口，就是常说的mapper接口，通过观察xml文件的写法，mybatis需要将dao接口的全限定名写在mapper标签的namespace中
     * 而里面的每个sql语句标签，比如select标签，需要将这个接口的一个方法名字写在id上，mybatis通过全限定名+方法名，也就是namespace+id
     * 的方式来准确定位一个接口中的某个方法，所以dao接口中的方法是不可以重载的
     */
    Department selectDeptById(@Param("id") Integer id);
}
