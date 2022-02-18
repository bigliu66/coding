package com.lx.jmh.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author lx
 * @date 2022/2/18 11:10
 */
@Data
@Accessors(chain = true)
public class BaseEntity {
    private Long id = 1l;
    private Date createTime = new Date();
    private Long createUser = 1l;
    private Date updateTime = new Date();
    private Long updateUser = 1l;
}
