package org.wso2.carbon.config;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.wso2.carbon.dto.RabbitMQDataDTO;

public class RabbitMQConfig {



    public Channel configure(RabbitMQDataDTO rabbitMQDataDTO) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(rabbitMQDataDTO.getHost());
        factory.setPort(rabbitMQDataDTO.getPort());
        factory.setUsername(rabbitMQDataDTO.getUser());
        factory.setPassword(rabbitMQDataDTO.getPassword());
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(rabbitMQDataDTO.getQueuename(), false, false, false, null);
        return channel;
    }
}
