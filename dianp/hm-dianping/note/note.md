# redis客户端-jedis
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
## 单元测试
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
## Jedis 线程池
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