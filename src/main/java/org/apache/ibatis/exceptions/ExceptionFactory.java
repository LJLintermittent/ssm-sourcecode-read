/**
 * Copyright 2009-2015 the original author or authors.
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
package org.apache.ibatis.exceptions;

import org.apache.ibatis.executor.ErrorContext;

/**
 * @author Clinton Begin
 */
@SuppressWarnings("all")
public class ExceptionFactory {

    /**
     * TODO 异常处理机制
     * java标准库中的异常都是以throwable为顶级父类，派生出了error和exception两大子类及其派生子类
     * error类及其子类，代表了jvm自身的异常，无法通过修改程序来修正，最可靠的方式就是停止虚拟机的运行
     * exception及其子类，代表了程序发生了意料之外的事情，这些异常可以被java异常处理机制来处理
     * exception及其子类被分为两大类：
     * 一类是runtimeexception及其子类，这是程序设计出现的错误，比如空指针异常
     * 非runtimeexception及其子类，这一异常通常是由外部因素导致发生的，是不可以避免和预知的，如io异常等
     * <p>
     * error异常和runtimeexception及其子类异常，这两大类异常可以在编写程序时避免以及jvm自己异常，只能关jvm
     * 他们俩被称为免检异常，即不需要对这两类异常进行强制检查
     * 除了这俩类以外，非runtimeexception异常他们的发生与外部环境有关，称为必检异常，在编写程序的时候必须使用try-catch进行处理或者
     * throw给子类来处理
     * 对于throwable对象来说，它的主要成员变量有两个，一个是cause，detailmessage
     * detailmessage作为字符串，用来存储异常的详细信息
     * cause作为一个throwable对象，用来存储引发异常的原因，这是因为一个异常发生时，会导致上级程序也出现异常，而cause就代表了
     * 下级程序的异常，表示自己的异常来源于哪个下级异常，将整个异常链串起来
     */

    // 不允许实例化该类
    private ExceptionFactory() {
        // Prevent Instantiation
    }

    // 静态方法，直接调用

    /**
     * 生成一个RuntimeException异常
     *
     * @param message 异常信息
     * @param e       异常
     * @return 新的RuntimeException异常
     */
    public static RuntimeException wrapException(String message, Exception e) {
        return new PersistenceException(ErrorContext.instance().message(message).cause(e).toString(), e);
    }

}
