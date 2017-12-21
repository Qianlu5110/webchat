package com.qianlu.pojo;

import java.util.Date;

/**
 * 消息体
 *
 * @author Qianlu
 * @date 2017/12/08 11:21
 **/
public class MessageDTO implements IMessage {

    /**
     * 消息发送人
     */
    private String fromUser;

    /**
     * 消息接收人
     */
    private String toUser;

    /**
     * 消息类型
     */
    private String type;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息头像
     */
    private String headImg;

    /**
     * 消息点击链接
     */
    private String clickUrl;

    /**
     * 消息创建时间
     * 除作为显示时间外，同时创建时间作为逾期消息进行销毁的依据
     */
    private Date createDate;

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getClickUrl() {
        return clickUrl;
    }

    public void setClickUrl(String clickUrl) {
        this.clickUrl = clickUrl;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "MessageDTO{" +
                "fromUser='" + fromUser + '\'' +
                ", toUser='" + toUser + '\'' +
                ", type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", headImg='" + headImg + '\'' +
                ", clickUrl='" + clickUrl + '\'' +
                ", createDate=" + createDate +
                '}';
    }
}
