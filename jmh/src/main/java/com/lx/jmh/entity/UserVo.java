package com.lx.jmh.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class UserVo extends BaseEntity {
    private Long id;
    private String username;
    private String gender;
    private Integer age;
    private String level;
    private String label;
    private String mobile;
    private String email;
    private String address;
    private String profession;
    private Date createTime;
    private Long createUser;
    private Date updateTime;
    private Long updateUser;
}