package com.lx.log.logrecord.controller;

import com.lx.log.logrecord.annotation.LogRecordAnnotation;
import com.lx.log.logrecord.annotation.LogRecordAnnotations;
import com.lx.log.logrecord.model.ReqDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class TestController {

    @PostMapping("/testSuccess")
    @LogRecordAnnotations(value = {
            @LogRecordAnnotation(bizNo = "#req.bizNo", success = "创建订单", operator = "#req.userId"),
            @LogRecordAnnotation(bizNo = "#req.bizNo", success = "更新订单", operator = "#req.userId")})
    public String testSuccess(@RequestBody ReqDto req) {
        return "";
    }

    @PostMapping("/testFailure")
    @LogRecordAnnotation(bizNo = "#req.bizNo", success = "发货", operator = "#req.userId")
    public void testFailure(@RequestBody ReqDto req) {
        int i = 1 / 0;
    }

}
