package cn.loveyx815.damai.compont;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.loveyx815.damai.bean.MailBean;
import cn.loveyx815.damai.spring.SpringProducer;
import cn.loveyx815.damai.utils.SerializeUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @Auther: Yonggang Shi
 * @Date: 2020/02/25 10:31
 * @Description: 发送邮件实体消息队列
 */
@Slf4j

public class DMJSONP {


    @Autowired
    private SpringProducer producer;

    @Value("{mail.to.address}")
    private static String TOEMAIL_ARR;

    public static final String DAMAI_URL = "https://search.damai.cn/searchajax.html?keyword=&cty=&ctl=%E6%BC%94%E5%94%B1%E4%BC%9A&sctl=&tsg=0&st=&et=&order=2&pageSize=30&currPage=1&tn=";

    public DMJSONP() {

    }

    public void sendMsgMail() {
        String object = doGetOne(DAMAI_URL);
        JSONObject jsonObject = JSON.parseObject(object);
        JSONArray array = JSON.parseArray(JSON.parseObject(jsonObject.get("pageData").toString()).get("resultData").toString());
        MailBean mailBean = new MailBean();//邮件bean
        mailBean.setRecipient(TOEMAIL_ARR);//
        mailBean.setSubject("【大麦网最新演唱会】");
        StringBuilder sb = new StringBuilder();
        for (Object obj : array) {
            JSONObject json = JSON.parseObject(obj.toString());
            if (json.toJSONString().indexOf("周杰伦")>0) {

                sb.append("<h2>【" + json.get("cityname") + "】 " + json.get("name") + "</h2>")
                        .append("<p style='text-align:left'>" + json.get("actors") + "</p>")
                        .append("<p> 时间:" + json.get("showtime") + "</p>");
                sb.append("<hr/>");
            }

        }
        mailBean.setContent(sb.toString());
        //发送消息
        Message message = new Message(
                "DAMAI-rocketMQ-topic",
                null,
                SerializeUtil.serialize(mailBean)
        );
        SendResult sendResult = null;
        try {
            sendResult = producer.getProducer().send(message);
        } catch (Exception e) {
            log.info("【发送邮件MQ是失败】" + e);
        }

        log.info(sendResult.toString());

        // mailUtil.sendHTMLMail(mailBean);

    }

    /*
     * @Description: HTTPclient调接口
     * @Param: [url]
     * @Return: void
     * @Author: Yonggang Shi
     * @Date: 2020/2/25/025 下午 4:20
     */
    public String doGetOne(String url) {

        //响应内容
        String responseString = null;
        HttpClient client = null;
        try {
            client = new DefaultHttpClient();
            HttpGet htttpGet = new HttpGet(url);
            //设置请求的报文头部的编码
            htttpGet.setHeader(new BasicHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8"));
            //设置期望服务端返回的编码
            htttpGet.setHeader(new BasicHeader("Accept", "application/json;charset=utf-8"));
            HttpResponse response = client.execute(htttpGet);
            String charset = "UTF-8";
            /**请求发送成功，并得到响应**/
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                /**读取服务器返回过来的json字符串数据**/
                // 使用EntityUtils的toString方法，传递编码，默认编码是ISO-8859-1
                responseString = EntityUtils.toString(response.getEntity(), charset);

            }


        } catch (Exception e) {
            log.error("【httpclient调用失败】" + e);
        } finally {
            try {
                // 释放资源
                if (client != null) {
                    ((DefaultHttpClient) client).close();
                }

            } catch (Exception e) {
                log.error("【httpclient关闭失败】" + e);
            }
        }
        return responseString;
    }


}
