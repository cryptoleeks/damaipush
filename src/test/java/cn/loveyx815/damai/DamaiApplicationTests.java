package cn.loveyx815.damai;

import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import cn.loveyx815.damai.bean.MailBean;
import cn.loveyx815.damai.compont.MailUtil;

@SpringBootTest
class DamaiApplicationTests {

    @Autowired
    private MailUtil mailUtil;

    @Test
    void contextLoads() {
    }

    @Test
    public void sendHTMLMail() {
        MailBean mailBean = new MailBean();
        mailBean.setRecipient("xshiyonggang@163.com;2638342739@qq.com;626614916@qq.com");
        mailBean.setSubject("SpringBootMailHTML之这是一封HTML格式的邮件");
        StringBuilder sb = new StringBuilder();
        sb.append("<h2>SpirngBoot测试邮件HTML</h2>")
                .append("<p style='text-align:left'>这是一封HTML邮件...</p>")
                .append("<p> 时间为：" + new Date() + "</p>");
        mailBean.setContent(sb.toString());

        mailUtil.sendHTMLMail(mailBean);


    }
}
