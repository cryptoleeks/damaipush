package cn.loveyx815.damai.spring;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Auther: Yonggang Shi
 * @Date: 2020/01/30 16:29
 * @Description:
 */
public class SpringCousumer {

    private Logger logger =LoggerFactory.getLogger(this.getClass());

    private String consumerGroupName;

    private String nameServerAddr;

    private String topicName;

    private DefaultMQPushConsumer consumer;

    private MessageListenerConcurrently messageListenerConcurrently;

    public SpringCousumer(String consumerGroupName,String nameServerAddr,String topicName,MessageListenerConcurrently messageListener){
        this.consumerGroupName=consumerGroupName;
        this.nameServerAddr=nameServerAddr;
        this.topicName=topicName;
        this.messageListenerConcurrently=messageListener;
    }

    public void init() throws Exception{
        logger.info("开始启动消费者服务。。");
        consumer=new DefaultMQPushConsumer(consumerGroupName);
        consumer.setNamesrvAddr(nameServerAddr);
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.subscribe(topicName,"*");
        consumer.registerMessageListener(messageListenerConcurrently);
        consumer.start();
        logger.info("消费者服务已启动。。。");
    }

    public void destory(){
        logger.info("开始关闭消息者服务。。。");
        consumer.shutdown();
        logger.info("消费者服务已关闭！");
    }

    public DefaultMQPushConsumer getConsumer(){
        return consumer;
    }
}
