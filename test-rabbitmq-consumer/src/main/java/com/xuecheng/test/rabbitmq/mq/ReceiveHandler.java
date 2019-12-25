package com.xuecheng.test.rabbitmq.mq;

import com.rabbitmq.client.Channel;
import com.xuecheng.test.rabbitmq.config.RabbitMqConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author: huiyishe
 * @Date: 2019-12-25 15:08
 */
@Component
public class ReceiveHandler {

    @RabbitListener(queues = {RabbitMqConfig.QUEUE_INFORM_EMAIL})
    public void send_email(String message, Message msg, Channel channel){
        System.out.println( "recevie message is  "+message);

    }
}
