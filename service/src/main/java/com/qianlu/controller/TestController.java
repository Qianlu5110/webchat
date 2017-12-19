package com.qianlu.controller;

import com.qianlu.pojo.IMessage;
import com.qianlu.pojo.MessageDTO;
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
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setType(IMessage.TYPE_HTML);
        messageDTO.setToUser("15623885110");
        messageDTO.setHeadImg("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=4131634322,487666839&fm=27&gp=0.jpg");
        messageDTO.setClickUrl("https://www.baidu.com");
        messageDTO.setTitle("标题");
        messageDTO.setContent("内容，内容 " + new Date());
        msgSenderService.send(messageDTO);
    }
}
