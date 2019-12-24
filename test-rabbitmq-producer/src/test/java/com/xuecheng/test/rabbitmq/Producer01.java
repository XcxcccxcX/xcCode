package com.xuecheng.test.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author: huiyishe
 * @Date: 2019-12-24 17:35
 */
public class Producer01 {
    private static final String QUEUE = "HELLOWORLD";

    public static void main(String[] args) {
        //通过连接工场创建新的连接和MQ建立连接
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //ip
        connectionFactory.setHost("127.0.0.1");
        //端口
        connectionFactory.setPort(5672);
        //用户名
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        //设置虚拟机  一个MQ服务可以设置多个虚拟机,每个虚拟机相当于一个独立的MQ
        connectionFactory.setVirtualHost("/");

        Connection connection = null;
        Channel channel = null;
        try {
            //建立新连接
            connection = connectionFactory.newConnection();
            //创建会话通道  生产者和mq服务所有通信都在channel通道中完成
            channel = connection.createChannel();
            //声明队列,如果队列在mq中没有 则要创建
            //参数 : String queue,boolean durable, boolean exclusive, boolean autoDelete,Map<String, Object> arguments
            /**
             * 1. queue : 队列名称
             * 2. durable : 是否持久化 , 如果持久化 重启后 队列依旧存在
             * 3. exclusive : 是否独占连接, 队列只允许在改连接中访问, 如果connection连接关闭 队列自动删除, 如果将此参数设置true 可以用于临时队列的创建
             * 4. autoDelete : 自动删除 ,队列不再使用时是否自动删除此队列, 如果将此参数和exclusive参数设置为true 就可以实现临时队列
             * 5. arguments : 参数 ,可以设置一个队列的扩展参数, 比如: 存活时间 ...
             */
            channel.queueDeclare(QUEUE,true,false,false,null);

            //发送消息
            //参数: String exchange, String routingKey, BasicProperties props, byte[] body
            /**
             * 1. exchange : 交换机 ,如果不指定将使用mq的默认交换机 (设置为 "")
             * 2. routingKey : 路由key ,交换机根据路由key来将消息转发到指定队列 ,如果使用默认交换机 ,routingKey设置为队列名
             * 3. props : 额外的属性
             * 4. body : 消息内容
             */

            //定义消息内容
            String message = "HELLOWORLD RabbitMq 4 -------------------------";
            channel.basicPublish("",QUEUE,null,message.getBytes("UTF-8"));
            System.out.println("send to mq   >>>         "+message);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
