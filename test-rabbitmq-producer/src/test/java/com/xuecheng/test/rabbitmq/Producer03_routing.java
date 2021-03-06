package com.xuecheng.test.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author: huiyishe
 * @Date: 2019-12-24 17:35
 */
public class Producer03_routing {
    private static final String QUEUE_INFORM_EMAIL = "queue_inform_email";
    private static final String QUEUE_INFORM_SMS = "queue_inform_sms";
    private static final String EXCHANGE_ROUNTING_INFORM="exchange_routing_inform";
    private static final String ROUTINGKEY_EMAIL = "inform_email";
    private static final String ROUTINGKEY_SMS = "inform_sms";

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
            channel.queueDeclare(QUEUE_INFORM_EMAIL,true,false,false,null);
            channel.queueDeclare(QUEUE_INFORM_SMS,true,false,false,null);

            //参数  String exchange, BuiltinExchangeType type
            /**
             * 1. exchange: 交换机的名称
             * 2. BuiltinExchangeType: 交换机的类型
             *      1. fanout: 对应rabbitmq的工作模式是 publish/subscribe
             *      2. direct: 对应Routing 工作模式
             *      3. topic:  对应Topics 工作模式
             *      4. headers: 对应 headers 工作模式
             */
            channel.exchangeDeclare(EXCHANGE_ROUNTING_INFORM, BuiltinExchangeType.DIRECT);
            //进行交换机和队列绑定
            //参数 String queue, String exchange, String routingKey
            /**
             * 1. queue 队列名称
             * 2. exchange 交换机名称
             * 3.routingKey 路由key 作用是交换机根据路由key的值将消息转发的指定的队列中  在发布订阅中 设置为空串
             */
            channel.queueBind(QUEUE_INFORM_EMAIL,EXCHANGE_ROUNTING_INFORM,ROUTINGKEY_EMAIL);
            channel.queueBind(QUEUE_INFORM_SMS,EXCHANGE_ROUNTING_INFORM,ROUTINGKEY_SMS);

            //发送消息
            //参数: String exchange, String routingKey, BasicProperties props, byte[] body
            /**
             * 1. exchange : 交换机 ,如果不指定将使用mq的默认交换机 (设置为 "")
             * 2. routingKey : 路由key ,交换机根据路由key来将消息转发到指定队列 ,如果使用默认交换机 ,routingKey设置为队列名
             * 3. props : 额外的属性
             * 4. body : 消息内容
             */

            for (int i = 0;i < 5;i++){
                //定义消息内容  发送消息指定 routing key
                String message = "send email message  to user ";
                channel.basicPublish(EXCHANGE_ROUNTING_INFORM,ROUTINGKEY_EMAIL,null,message.getBytes("UTF-8"));
                System.out.println(" send to mq   >>>       "+message);
            }

            for (int i = 0;i < 5;i++){
                //定义消息内容  发送消息指定 routing key
                String message = "send sms message  to user ";
                channel.basicPublish(EXCHANGE_ROUNTING_INFORM,ROUTINGKEY_SMS,null,message.getBytes("UTF-8"));
                System.out.println(" send to mq   >>>       "+message);
            }
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
