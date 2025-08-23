package com.hmdp.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // 注解，创建getter和setter方法
@NoArgsConstructor // 注解，创建无参构造方法
@AllArgsConstructor // 注解，创建有参构造方法
public class user {
    private String name;
    private Integer age;
}
