# 点评笔记
更详细的可以参考https://blog.csdn.net/qq_66345100/article/details/131986713
包括业务逻辑等

以下是本人自留
# redis客户端
## 可能出现的问题
### 1. redis连接失败
可能是虚拟机重新打开后ip地址改变，需要重新配置ip地址
![img_29.png](img_29.png)
![img_30.png](img_30.png)
![img_31.png](img_31.png)
## springboot配置jedis
### 配置依赖项
先从官网（github项目地址)找到对应的Maven依赖
![img_1.png](img_1.png)
![img.png](img.png)
复制到pom.xml文件当中，刷新Maven依赖；
![img_2.png](img_2.png)
由红变白就是依赖包下载完成，另外要配置单元测试的junit包
![img.png](img_3.png)
### 连接jedis
创建redis实例后，创建setup方法配置连接设置
![img_4.png](img_4.png)
### 单元测试
@BeforeEach注解
![img_5.png](img_5.png)
通过set、get方法存入、获取redis数据
![img_6.png](img_6.png)
内存回收，防止空指针
![img_7.png](img_7.png)
执行测试用例，查看结果：
![img_8.png](img_8.png)
哈希类型测试：
![img_9.png](img_9.png)
### Jedis 线程池
线程池工具类：
![img_10.png](img_10.png)
修改测试里的连接配置：
![img_13.png](img_13.png)
报错了，问了deepseek说是方法是旧版本，教程里是3.x
这里2025.8.16官方依赖是6.x
![img_12.png](img_12.png)
![img_11.png](img_11.png)
![img_14.png](img_14.png)
按照ds结论，决定修改springboot版本为3.x;
其他配置项也有响应修改；
![img_15.png](img_15.png)
![img_17.png](img_17.png)
![img_20.png](img_20.png)
所有的jakarta 修改为jakarta
![img_18.png](img_18.png)
修改JedisConnectionFactory工具类的方法:
![img_16.png](img_16.png)
之后就可以运行了：
![img_19.png](img_19.png)
## SpringDataRedis
* 整合了Lettuce和Jedis;
* 提供了RedisTemplate类统一API来操作redis;
* 支持redis的发布订阅模型
* 支持redis哨兵和redis集群
* 支持基于JDK、JSON、字符串、Spring对象的数据序列化和反序列化
* 支持基于redis的JUKCollection实现
### 快速入门
#### 文件配置
注意版本不同配置略有变化
![img_21.png](img_21.png)
#### 测试
1. 代码编写(ps：这里的忘记加上@Autowired注解了)
![img_22.png](img_22.png)
![img_23.png](img_23.png)
2. 此时存入的不是字符串：
![img_24.png](img_24.png)
3. debug查看数据情况：
![img_25.png](img_25.png)
![img_26.png](img_26.png)
![img_27.png](img_27.png)
![img_28.png](img_28.png)
这样存入的数据可读性较差，并且键值也会修改失败；
另外存储占用的空间也较大，因此需要修改默认的序列化设置；
4. 修改默认的序列化设置
![img_33.png](img_33.png)
这里key和value的序列化方式调用的参数不一致，询问ai得知：
这里的RedisSerializer.string()方法是静态实例创建的，因此不需要new
![img_35.png](img_35.png)
也就是说，这里的key是序列化为了字符串，而value是序列化为了对象(json格式)，
保持可读性的同时能够存储复杂的对象信息。
![img_34.png](img_34.png)
修改后尝试运行单元测试：
![img_32.png](img_32.png)
5. 尝试存入对象
新建一个类用于测试,数据保存时会存入json类型数据，包括对象类型和值；
这样获取数据时能够自动反序列化为对应的对象类型，但是增加了存储空间和数据处理时间开销；
![img_36.png](img_36.png)
![img_37.png](img_37.png)
![img_38.png](img_38.png)
6. 考虑到存储空间的额外开销，一般不使用json自动序列化和反序列化，而是统一使用String序列化器，要求只存储字符串数据；
需要存储对象数据时，手动完成序列化和反序列化；
spring提供了一个StringRedisTemplate类，序列化方式默认为StringRedisSerializer
![img_39.png](img_39.png)
![img_40.png](img_40.png)
# 黑马点评项目实战
## 导入项目文件
### 导入后端项目及数据库
navicat新建数据库，输入数据库名，选择utf8编码：
![img_41.png](img_41.png)
导入数据库文件：
![img_42.png](img_42.png)
![img_43.png](img_43.png)
![img_44.png](img_44.png)
将项目文件复制到自己的开发文件夹，修改yaml文件配置及pom文件配置
![img_48.png](img_48.png)
![img_45.png](img_45.png)
启动项目，没有报错之后访问：http://localhost:8080/shop-type/list
![img_47.png](img_47.png)
这里我出现了报错：
![img_53.png](img_53.png)
查询对应的链接发现
![img_54.png](img_54.png)
需要将项目当中所有jakarta 相关的包修改为jakarta
![img_55.png](img_55.png)
重新启动后刷新页面，可以获取到数据库中的数据就是启动成功
![img_56.png](img_56.png)
### 导入前端项目
前端项目文件复制到自己的开发文件夹，在nginx文件夹下cmd命令行运行start nginx.exe
![img_57.png](img_57.png)
![img_58.png](img_58.png)
成功后访问：http://localhost/8080 刷新后可以看到对应页面
![img_59.png](img_59.png)
## 短信验证功能
### 基于Session实现登录
功能逻辑
其中threadLocal变量用于保存当前线程的session对象，
每个请求是单独都线程，保存对应的信息，线程之间不共享数据，互不干扰；
![img_60.png](img_60.png)
#### 验证码发送
1）需要有可用手机号才能发送验证码
2）验证码发送成功之后，将验证码保存在session中，用于后续验证
3）模拟验证码发送，使用log打印
![img_61.png](img_61.png)
#### 验证
1）需要有手机号和验证码才能验证
2）验证码验证成功之后，判断用户是否存在，
存在则保存到session中，不存在则创建用户并保存到数据库及session中
注：这里不需要返回登录凭证（如token）,因为session会生成cookie，自带登录凭证
![img_62.png](img_62.png)
#### 配置拦截器
拦截器用于判断用户是否登录，如果登录则放行，否则返回登录页面；
配置拦截器后，就不用每个请求都手动判断用户是否登录了
1）拦截器逻辑
![img_63.png](img_63.png)
2）配置拦截器
![img_64.png](img_64.png)
![img_65.png](img_65.png)
#### 隐藏用户敏感信息
直接使用user类存入的信息包含用户密码等敏感信息，另外也会占用存储空间，
进行拦截校验所需要的信息只要存入基础信息，因此创建一个UserDTO类，继承User类，只包含基础信息（源码已创建）
![img_66.png](img_66.png)
#### session集群共享问题
多台tomcat服务器之间session不共享存储空间，请求切换到不同服务器时session数据丢失；
数据拷贝：内存空间浪费+拷贝时间开销导致数据不一致
redis替代session：数据共享、内存存储、key/value结构
#### 基于redis代替共享session实现登录
1）验证码保存数据结构：key：phone（注意唯一性及客户端能够使用key获取数据），value：code（string）
2）用户对象保存：
    * hash结构（可以将对象当中每个字段单独保存，对单个字段crud且内存占用更少）
    * key:随机token（数据保存在前端，存在安全风险）
![img_67.png](img_67.png)
具体代码修改(详见教程)：
UserServiceImpl.java(sendcode、login)
LoginInterceptor.java等

双拦截器：一个拦截，一个刷新token（单个拦截器部分拦截，可能导致token过期不刷新）
将刷新逻辑转移至第一个拦截器当中
![img_68.png](img_68.png)
移除原拦截器相关逻辑，只校验用户是否存在ThreadLocal当中
![img_69.png](img_69.png)
修改拦截器配置（MVCConfig）
![img_70.png](img_70.png)















