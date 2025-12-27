package com.readingweb.controller;

import com.readingweb.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试控制器
 * 
 * @author ReadingWeb Team
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/hello")
    public Result<String> hello() {
        return Result.success("Hello, ReadingWeb!");
    }
}

