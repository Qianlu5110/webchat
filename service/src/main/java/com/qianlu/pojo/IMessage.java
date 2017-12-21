package com.qianlu.pojo;

/**
 * 消息体声明
 *
 * @author Qianlu
 * @date 2017/12/08 10:56
 **/
public interface IMessage {

    /**
     * 消息体存活时间
     */
    Long SURVIVAL_TIME = 7 * 24 * 60 * 60 * 1000L;

    /**
     * 注册
     */
    String TYPE_REGISTER = "type/register";

    /**
     * 文本型消息
     */
    String TYPE_TEXT = "type/text";

    /**
     * html链接型消息
     */
    String TYPE_HTML = "type/html";

    /**
     * 图片消息
     */
    String TYPE_IMG = "type/img";
}
