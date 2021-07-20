package org.apache.ibatis.learn;

import com.google.common.collect.Maps;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.learn.entity.User;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * date: 2021/5/16 21:43
 * Package: org.apache.ibatis.learn
 *
 * @author 李佳乐
 * @version 1.0
 * <p>
 * 解析注解里面的sql语句的源码
 */

/**
 * mybatis架构设计
 * -------------------------------------------------   字段映射处理器
 * ---------------------------------------------------------↓
 * 配置类 ---- mapper注册中心----执行器-----statementHandler -----结果集Handler
 */
interface UserMapper {
    @Select("select * from tbl_user where id = #{id} and name = #{name}")
    List<User> selectUserList(Integer id, String name);
}

@SuppressWarnings("all")
public class Application {
    public static void main(String[] args) {
        /**
         * jdk动态代理
         */
        UserMapper UserMapperProxy = (org.apache.ibatis.learn.UserMapper) Proxy.newProxyInstance(Application.class.getClassLoader()
                , new Class<?>[]{UserMapper.class},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//                        System.out.println(Arrays.toString(args));
                        Select annotation = method.getAnnotation(Select.class);
                        Map<String, Object> nameArgMap = buildMethodArgNameMap(method, args);
                        if (annotation != null) {
                            String[] value = annotation.value();
                            String sql = value[0];
                            sql = parseSQL(sql, nameArgMap);
                            System.out.println(method.getReturnType());
                            System.out.println(method.getGenericReturnType());
                            System.out.println(sql);
//                            System.out.println(Arrays.toString(value));
                        }
                        return null;
                    }
                });
        List<User> users = UserMapperProxy.selectUserList(999, "李佳乐");
    }

    public static String parseSQL(String sql, Map<String, Object> nameArgMap) {
        //        String parseSQL = "";
        StringBuilder stringBuilder = new StringBuilder();
        int length = sql.length();
        for (int i = 0; i < length; i++) {
            char c = sql.charAt(i);
            if (c == '#') {
                int nextIndex = i + 1;
                char nextChar = sql.charAt(nextIndex);
                if (nextChar != '{') {
                    throw new RuntimeException(String.format("这里应该为#{\nsql:%s\nindex:%d"
                            , stringBuilder.toString(), nextIndex));
                }
                StringBuilder argSB = new StringBuilder();
                i = parseSQLArg(argSB, sql, nextIndex);
                String argName = argSB.toString();
                Object argValue = nameArgMap.get(argName);
                if (argValue == null) {
                    throw new RuntimeException(String.format("找不到参数值:%s", argName));
                }
                stringBuilder.append(argValue.toString());
                continue;
            }
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }

    private static int parseSQLArg(StringBuilder argSB, String sql, int nextIndex) {
        nextIndex++;
        for (; nextIndex < sql.length(); nextIndex++) {
            char c = sql.charAt(nextIndex);
            if (c != '}') {
                argSB.append(c);
                continue;
            }
            if (c == '}') {
                return nextIndex;
            }
        }
        throw new RuntimeException(String.format("缺少右括号\nindex:%d", nextIndex));
    }

    public static Map<String, Object> buildMethodArgNameMap(Method method, Object[] args) {
        Map<String, Object> nameArgMap = Maps.newHashMap();
        Parameter[] parameters = method.getParameters();
        int index[] = {0};
        Arrays.asList(parameters).forEach(parameter -> {
            String name = parameter.getName();
//            System.out.println(name);
            nameArgMap.put(name, args[index[0]]);
            index[0]++;
        });
        return nameArgMap;
    }
}
