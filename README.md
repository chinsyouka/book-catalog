# book-catalog
demo project for test

# task
Build a “Book Catalog Service”: a Spring Boot microservice that allows clients to manage a collection of books (CRUD)
1.RESTful CRUD API(No UI needed)
2.Apply at least 2 design patterns in your code
3.Write unit tests and API tests. Aim for ≥ 80% code coverage
4.Should build it with AI tools
5.Should submit all the AI prompts，skills or any other AI related inputs used in the homework


提交：请以github链接的形式提交，方便查看，谢谢配合。

## prompt for building
### framework
当前目录下创建j2ee微服务framework,包名为com.example.bookcatalog,以下需要依赖的包的要求
包管理: 通过maven来管理.
模块1: boot-parent 存放所有依赖pom.xml文件纯依赖管理形式（dependencyManagement），并使用 properties 来配置版本号。
模块2: book-catalog-api存放所有控制器,服务接口以及dao层接口,日志配置为logback.xml,此模块为启动模块。
- java版本: 采用jdk21
- 核型框架: spring boot:3.5.3,spring cloud:4.3.0,lombok
- 持久层: jdbc,mysql,redis,mybatis
- 消息中间件: kafka
- 测试: junit5,mockito

### 微服务service层
创建实体类,类名以Entity结尾 :
- Book(id,name,price,author,catalogId,createTime,updateTime)
创建Service接口:
- 业务服务接口:根据实体类创建CRUD接口,接口名以Service结尾,接口方法名以findAll,findOne,create,update,delete开头
实现Service接口,类名以ServiceImpl结尾并且实现:
- 实现业务接口,在实现类中使用log.info代替数据库读写

### 观察者模式Observer
创建观察者类,类名以Observer结尾, 继承AbstractObserver.
创建抽象类AbstractObserver,抽象方法为update、getType,并添加内部共通方法do.
创建订阅类,类名以Subscriber结尾, 继承AbstractSubscriber.
创建抽象类AbstractSubscriber,抽象方法为add(Observer),remove(Observer),notifyAllObservers. 并添加内部共通方法do.

### 基于策略的模版方法Abstract
根据抽象类AbstractObserver.getObserverType的值构建策略模式,类名以Strategy结尾, 继承AbstractStrategy.
AbstractStrategy带有抽象方法process.
创建策略类的工厂类名以StrategyFactory结尾, 类初始化的时候将所有策略类实例化,并保存在map中,key为观察者的getObserverType的值,value为策略类实例.
各strategy类通过list来注入spring环境,将strategyMap的初始化与注入放到BookCatalogConfiguration中

### api controller 层
创建api controller层,类名以Controller结尾, 需要包含CRUD的方法，并且符合RESTful风格

## prompt for testing
对service包内的类方法创建单元测试类,类名以Test结尾
创建api controller层单元测试类,类名以ControllerTest结尾


