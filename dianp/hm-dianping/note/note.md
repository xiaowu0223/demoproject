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
所有的javax修改为jakarta
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
新建一个类用于测试：
![img_36.png](img_36.png)
6. 3
7. 