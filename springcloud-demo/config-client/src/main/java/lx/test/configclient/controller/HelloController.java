package lx.test.configclient.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
class HelloController {
    // curl -X POST http://localhost:8002/actuator/refresh 手动刷新
    // curl -X POST http://localhost:8001/actuator/busrefresh 模拟config-service webhook刷新
    @Value("${config.hello}")
    private String hello;

    @GetMapping("/hello")
    public String from() {
        return this.hello;
    }
}