package com.xuecheng.test.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *  消费者 consumer
 * @author: huiyishe
 * @Date: 2019-12-24 19:21
 */
public class Consumer01 {
    //队列
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

        //建立新连接
        try {
            connection = connectionFactory.newConnection();
            //创建会话通道  生产者和mq服务所有通信都在channel通道中完成
            Channel channel = connection.createChannel();

            //监听队列
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

            //实现消费方法
            DefaultConsumer defaultConsumer = new DefaultConsumer(channel){
                //当接受到消息后 此方法将被调用

                /**
                 *
                 * @param consumerTag 消费者标签, 用来标识消费者, 在监听队列时设置(channel.basicConsume())
                 * @param envelope 信封, 通过envelope
                 * @param properties 消息属性,
                 * @param body 消息内容,
                 * @throws IOException
                 */
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    //获取交换机
                    String exchange = envelope.getExchange();
                    //消息id, mq在通道中用来标识消息的id, 可以用于确认消息已接收
                    long deliveryTag = envelope.getDeliveryTag();
                    //消息内容
                    String message = new String(body,"utf-8");
                    System.out.println("receive message >>>>>  "+message);
                }
            };

            //监听队列
            //参数: String queue, boolean autoAck, Consumer callback
            /**
             * 1. queue: 队列名称
             * 2. autoAck: 自动回复, 当消费者接收到信息后要告诉mq消息已经接收, 如果将此参数设置为true 表示会自动回复mq, 如果设置false要通过编程实现回复
             * 3. callback: 消费方法, 当消费者接收到消息要执行的方法
             */
            channel.basicConsume(QUEUE,true,defaultConsumer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }
}
