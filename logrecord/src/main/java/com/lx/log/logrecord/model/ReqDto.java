package com.lx.log.logrecord.model;

import lombok.Data;

import java.util.List;

/**
 * @author lx
 * @date 2021/12/9 10:25
 */
@Data
public class ReqDto {
    private String bizNo;
    private String userId;
    private List<String> testList;
}
