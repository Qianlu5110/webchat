package com.qianlu.controller;

import com.qianlu.producer.MsgSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 消息发送测试demo
 *
 * @author Qianlu
 * @date 2017/12/12 11:28
 **/
@RestController
@RequestMapping("/rabbit")
public class TestController {

    @Autowired
    private MsgSenderService msgSenderService;

    @GetMapping("/hello")
    public void hello() {
        msgSenderService.send(
                "admin",
                "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=4131634322,487666839&fm=27&gp=0.jpg",
                "https://www.baidu.com",
                "待办事项标题",
                "你今天有新的待办事项！" + new Date());
    }
}
