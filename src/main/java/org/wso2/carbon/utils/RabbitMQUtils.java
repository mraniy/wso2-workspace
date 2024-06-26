package org.wso2.carbon.utils;

import com.rabbitmq.client.Channel;
import org.wso2.carbon.config.RabbitMQConfig;
import org.wso2.carbon.dto.RabbitMQDataDTO;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

public class RabbitMQUtils {

    private static final Logger logger = Logger.getLogger(RabbitMQUtils.class.getName());

    public static void cleanRessources(Channel channel) throws IOException, TimeoutException {
        channel.close();
        channel.getConnection().close();
        logger.info("rabbitMQ ressources cleaned");
    }

    public static void sendMessageToRabbit(String request, RabbitMQDataDTO rabbitMQDataDTO, Channel channel) throws IOException {
        channel.basicPublish("", rabbitMQDataDTO.getQueuename(), null, request.getBytes());
        logger.info("Message sent to RabbitMQ " + request);
    }

    public static Channel configureRabbit(RabbitMQDataDTO rabbitMQDataDTO) throws Exception {
        RabbitMQConfig rabbitMQConfig = new RabbitMQConfig();

        // configuration de rabbitMQ
        return rabbitMQConfig.configure(rabbitMQDataDTO);
    }
}
