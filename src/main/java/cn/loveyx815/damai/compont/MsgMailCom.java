package cn.loveyx815.damai.compont;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @Auther: Yonggang Shi
 * @Date: 2020/02/25 16:45
 * @Description:发送消息定时任务
 */
@Slf4j
@Component
@EnableScheduling
public class MsgMailCom {

    @Autowired
    private DMJSONP dmjsonp;


    @Scheduled(cron = "0/5 * * * * *")
    public void run() throws InterruptedException {
        Thread.sleep(6000);
        dmjsonp.sendMsgMail();
        log.info(Thread.currentThread().getName()+"=====>>>>>使用cron  {}"+(System.currentTimeMillis()/1000));
    }


}
