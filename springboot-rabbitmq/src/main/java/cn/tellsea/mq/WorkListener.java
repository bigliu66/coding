package cn.tellsea.mq;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Work：生产者-->队列-->多个消费者共同消费
 */
@Component
public class WorkListener {

    // 通过注解自动创建 spring.work.queue 队列
    @RabbitListener(queuesToDeclare = @Queue("spring.work.queue"))
    public void listen(String msg) {
        System.out.println("WorkListener listen 接收到消息：" + msg);
    }

    // 创建十个队列共同消费
    @RabbitListener(queuesToDeclare = @Queue("spring.work.queue"))
    public void listen2(String msg) {
        System.out.println("WorkListener listen2 接收到消息：" + msg);
    }

    // 创建十个队列共同消费
    @RabbitListener(queuesToDeclare = @Queue("spring.work.queue"))
    public void listen3(String msg) {
        System.out.println("WorkListener listen3 接收到消息：" + msg);
    }

    // 创建十个队列共同消费
    @RabbitListener(queuesToDeclare = @Queue("spring.work.queue"))
    public void listen4(String msg) {
        System.out.println("WorkListener listen4 接收到消息：" + msg);
    }

    // 创建十个队列共同消费
    @RabbitListener(queuesToDeclare = @Queue("spring.work.queue"))
    public void listen5(String msg) {
        System.out.println("WorkListener listen5 接收到消息：" + msg);
    }

    // 创建十个队列共同消费
    @RabbitListener(queuesToDeclare = @Queue("spring.work.queue"))
    public void listen6(String msg) {
        System.out.println("WorkListener listen6 接收到消息：" + msg);
    }

    // 创建十个队列共同消费
    @RabbitListener(queuesToDeclare = @Queue("spring.work.queue"))
    public void listen7(String msg) {
        System.out.println("WorkListener listen7 接收到消息：" + msg);
    }

    // 创建十个队列共同消费
    @RabbitListener(queuesToDeclare = @Queue("spring.work.queue"))
    public void listen8(String msg) {
        System.out.println("WorkListener listen8 接收到消息：" + msg);
    }

    // 创建十个队列共同消费
    @RabbitListener(queuesToDeclare = @Queue("spring.work.queue"))
    public void listen9(String msg) {
        System.out.println("WorkListener listen9 接收到消息：" + msg);
    }

    // 创建十个队列共同消费
    @RabbitListener(queuesToDeclare = @Queue("spring.work.queue"))
    public void listen10(String msg) {
        System.out.println("WorkListener listen10 接收到消息：" + msg);
    }

}