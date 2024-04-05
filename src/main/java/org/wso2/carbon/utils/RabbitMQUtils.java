package org.wso2.carbon.utils;

import com.rabbitmq.client.Channel;
import org.wso2.carbon.config.RabbitMQConfig;
import org.wso2.carbon.dto.RabbitMQDataDTO;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMQUtils {

    public static void cleanRessources(Channel channel) throws IOException, TimeoutException {
        channel.close();
        channel.getConnection().close();
    }

    public static void sendMessageToRabbit(String request, RabbitMQDataDTO rabbitMQDataDTO, Channel channel) throws IOException {
        channel.basicPublish("", rabbitMQDataDTO.getQueuename(), null, request.getBytes());
    }

    public static Channel configureRabbit(RabbitMQDataDTO rabbitMQDataDTO) throws Exception {
        RabbitMQConfig rabbitMQConfig = new RabbitMQConfig();

        // configuration de rabbitMQ
        return rabbitMQConfig.configure(rabbitMQDataDTO);
    }
}
