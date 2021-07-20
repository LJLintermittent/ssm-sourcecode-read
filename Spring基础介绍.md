# 《互联网轻量级SSM框架解密--阅读笔记》

## 第一章 Spring基础介绍

spring是一款用于简化企业级Java应用开发的分层开源框架，它有着强大的扩展融合能力，在我看来，spring就像胶水一样，它将其他框架粘合起来，发挥出一个整体的作用，建立起来一个完整的体系，让我们在应用程序的开发中好像是只使用了一个框架，在一个统一的平台下高效的创作。

spring的优势主要体现为以下几点：

* 方便集成各种框架
* 大量使用了面向对象的设计模式与设计思想，是学习Java源码的经典框架
* 面向接口编程，而不是面向类编程，不断地利用Java多态的特性及良好的面向对象设计思想，来降低程序的复杂度和耦合度

spring是一个分层非常清晰并且依赖关系，职责定位非常明确的轻量级框架。主要分为八大模块

* 数据处理模块：该模块由JDBC，Transactions,ORM,OXM和JMS等模块组成

~~~wiki
JDBC模块提供了不需要编写冗长的JDBC代码和解析和解析数据库厂商特有的错误代码的JDBC-抽象层
Transactions模块支持编程和声明式事务管理
ORM：对象关系映射模块，如JPA,Hibernate等ORM框架
OXM：对象xml映射模块
JMS：生产和消费消息
~~~

* web模块

~~~wiki
web模块提供了面向web开发的集成功能
websocket模块提供了面向websocket开发的集成功能
servlet模块：包含MVC和REST web service实现的web应用程序
~~~

* AOP模块

~~~wiki
该模块是spring的代理模块，也是spring的核心模块，它巧妙的利用了JDK的动态代理和cglib动态代理面向过程编程，来实现业务的零入侵，低耦合的效果。为了确保与其他AOP框架的互用性，SpringAOP模块支持基于AOP联盟定义的API，也就是Aspect模块，与IOC模块相辅相成。
~~~

* Aspects模块

~~~wiki
该模块提供了与AspectJ(一个功能强大并且成熟的面向切面编程的框架)的集成，扩展了Java语言，定义了AOP语法(俗称接入点语法)，持有一个专门的编译器来生成遵守Java字节码规范的Class文件，使用字节码生成技术来实现代理。
~~~

* Instrumentation模块

~~~wiki
该模块是spring对其他容器的集成及对类加载器的扩展实现
~~~

* Messaging模块

~~~wiki
该模块是从spring集成项目中抽象出来的，类似于基于注解的spring mvc编程模块，包含一系列消息与方法的映射注解
~~~

* Core Container模块

~~~wiki
该模块是spring的根基，由Beans，Core，context，spEL四个子模块组成
beans模块和core模块提供框架的基础部分，包含IOC(控制反转)和DI(依赖注入)功能，使用beanfactory基本概念来实现容器对bean的管理
是所有spring应用的核心，sping本身的运行都是由这种bean的核心模型进行加载和执行的，是spring其他模块的核心支撑，是运行的根本保证。
context模块建立在beans和core模块的坚实基础上，并且集成了beans模块的特征，增加了对国际化的支持，ApplicationContext接口是context模块的焦点，spring-context-support模块支持集成第三方常用库到spring应用的上下文中，例如缓存(EhCache，Guava)，调度框架(Quartz)及模板引擎(freemarker velocity)等
SPEL模块提供了强大的表达式语言来查询和操作运行时对象
~~~

* Test模块

~~~wiki
该模块支持通过JUnit或TestNG来进行单元测试和集成测试，并且提供了Mock object（模仿对象）方式来进行测试
~~~

### spring的领域模型

1.容器领域模型，（context模型）也叫上下文模型，是spring的掌控域，对spring核心领域模型进行生命周期管理，也可以将其称为spring的服务域，因此它将为整个应用服务

2.核心领域模型，（Bean模型）体现了spring的核心理念，“一切皆bean，bean即一切”，bean是应用运行时可执行的最小函数单元，可以是一个属性单元，也可以是java中的一个函数对象。

3.代理领域模型，spring代理的执行依赖于bean模型，但是spring代理的生成，执行及选择都依赖于spring自身定义的Advisor模型，只有符合Advisor模型的定义，才能生成sping代理。