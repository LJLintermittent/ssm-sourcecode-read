package org.apache.ibatis.learn;

import java.lang.reflect.Method;

/**
 * Description:
 * date: 2021/5/16 23:16
 * Package: org.apache.ibatis.learn
 *
 * @author 李佳乐
 * @version 1.0
 */
interface Test {
    void say();
}

interface InvokeHandler {
    Object invoke(Object obj, Method method, Object... args);
}

public class ProxyDemo {
    public static void main(String[] args) {

    }

    public Test createProxyInstance(final InvokeHandler handler, final Class<?> clazz) {
        return new Test() {
            @Override
            public void say() {
                try {
                    Method sayMethod = clazz.getMethod("say");
                    handler.invoke(this, sayMethod);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
