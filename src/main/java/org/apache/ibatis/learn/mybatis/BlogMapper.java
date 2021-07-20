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
public interface BlogMapper {
    Department selectDeptById(@Param("id") Integer id);
}
