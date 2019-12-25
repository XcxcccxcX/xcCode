package com.xuecheng.test.rabbitmq;

import com.xuecheng.test.rabbitmq.config.RabbitMqConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
/**
 * @author: huiyishe
 * @Date: 2019-12-24 17:35
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class Producer05_topic_springboot {

    @Autowired
    RabbitTemplate rabbitTemplate;



    @Test
    public void testSendEmail(){
        String message = "send email message to user<SpringBoot>";

        rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_TOPICS_INFORM,"inform.email",message);
    }
}
