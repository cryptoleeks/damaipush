package cn.loveyx815.damai.spring;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cn.loveyx815.damai.bean.MailBean;
import cn.loveyx815.damai.compont.MailUtil;
import cn.loveyx815.damai.utils.SerializeUtil;

/**
 * @Auther: Yonggang Shi
 * @Date: 2020/01/30 17:11
 * @Description: 消息监听逻辑
 */
public class MessageListener implements MessageListenerConcurrently {

    @Autowired
    private MailUtil mail;

    private Logger logger =LoggerFactory.getLogger(this.getClass());
    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {

        if (list!=null){
            for (MessageExt ext : list){
                try {
                    MailBean mailBean = (MailBean) SerializeUtil.unserialize(ext.getBody());
                    mail.sendHTMLMail(mailBean);//发送邮件
                    logger.info("监听到消息："+mailBean.toString());
                }catch (Exception e){
                    logger.info("解码异常"+e);
                }
            }
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
