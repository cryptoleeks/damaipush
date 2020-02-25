package cn.loveyx815.damai.bean;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Auther: Yonggang Shi
 * @Date: 2020/02/25 13:34
 * @Description:
 */
@Getter
@Setter
@ToString
public class MailBean implements Serializable {
    private static final long serialVersionUID = -2116367492649751914L;
    private String recipient;//邮件接收人
    private String subject; //邮件主题
    private String content; //邮件内容
    // 省略setget方法
}
