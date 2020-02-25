package cn.loveyx815.damai.spring;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Auther: Yonggang Shi
 * @Date: 2020/01/30 15:52
 * @Description: spring IoC实现生产者
 */
public class SpringProducer {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private String producerGroupName;

    private String nameServerAddr;

    private DefaultMQProducer producer;

    public SpringProducer(String producerGroupName, String nameServerAddr) {
        this.producerGroupName = producerGroupName;
        this.nameServerAddr = nameServerAddr;
    }

    public void init() throws Exception {
        logger.info("开始启动消息生产者服务。。。");

        //创建一个消息生产者，并设置一个消息生产者组
        producer = new DefaultMQProducer(producerGroupName);
        //指定nameserver地址
        producer.setNamesrvAddr(nameServerAddr);
        producer.start();
        logger.info("消息生产者服务启动成功。");

    }
/*
 * @Description: 销毁对象
 * @Param: []
 * @Return: void
 * @Author: Yonggang Shi
 * @Date: 2020/1/30/030 下午 4:28
 */
    public void destroy() {
        logger.info("开始关闭消息生产者服务。。");
        producer.shutdown();
        logger.info("消息生产者服务已关闭");
    }

    public DefaultMQProducer getProducer(){
        return producer;
    }
}
