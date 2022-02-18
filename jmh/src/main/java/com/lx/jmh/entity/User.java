package com.lx.jmh.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@Accessors(chain = true)
public class User extends BaseEntity{
    private String name;
    private String gender;
    private Integer age;
    private String level;
    private String label;
    private String mobile;
    private String email;
    private String address;
    private String profession;
}