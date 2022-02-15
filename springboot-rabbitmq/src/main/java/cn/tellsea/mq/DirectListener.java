package cn.tellsea.mq;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Direct：定向，把消息交给符合指定routing key 的队列
 */
@Component
public class DirectListener {

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "spring.direct.queue", durable = "true"),
            exchange = @Exchange(
                    value = "spring.direct.exchange",
                    ignoreDeclarationExceptions = "true"
            ),
            key = {"direct"}
    ), ackMode = "MANUAL")
    public void listen(String msg, Message message, Channel channel) {
        try {
            System.out.println("DirectListener listen 接收到消息：" + msg);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            System.out.println("发生异常:" + e.getMessage());
        }
    }

    // 队列2（第二个人），key值不同，接收不到消息
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "spring.direct2.queue", durable = "true"),
            exchange = @Exchange(
                    value = "spring.direct.exchange",
                    ignoreDeclarationExceptions = "true"
            ),
            key = {"direct-test"}
    ))
    public void listen2(String msg) {
        System.out.println("DirectListener listen2 接收到消息：" + msg);
    }

    //ttl
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "test.dlx.queue", durable = "true"),
            exchange = @Exchange(
                    value = "spring.direct.exchange",
                    ignoreDeclarationExceptions = "true"
            ),
            key = {"test.dlx.queue"}
    ), ackMode = "MANUAL")
    public void listen3(String msg, Message message, Channel channel) throws IOException {
        try {
            System.out.println("DirectListener listen3 接收到消息：" + msg);
            int i = 1 / 0;
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            //消费失败重试3次，3次失败后放入死信队列
            String msgId = (String) message.getMessageProperties().getHeaders().get("spring_returned_message_correlation");
//            int retryCount = (int) redisUtil.get(msgId);
//            System.out.println("------ retryCount : " + retryCount);
//            if (retryCount >= MAX_RECONSUME_COUNT) {
//                //requeue = false 放入死信队列
//                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
//            } else {
//                //requeue = true 放入消费队列重试消费
//                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
//                redisUtil.set(msgId, retryCount + 1);
//            }
            System.out.println("msgId:" + msgId);
            System.out.println("发生异常:" + e.getMessage());
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
        }
    }

    //dlx
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "dlx.queue", durable = "true"),
            exchange = @Exchange(
                    value = "dlx.direct.exchange",
                    ignoreDeclarationExceptions = "true"
            ),
            key = {"dlx.queue"}
    ))
    public void dlxListen(String msg) {
        System.out.println("DlxListener listen 接收到消息：" + msg);
    }

}