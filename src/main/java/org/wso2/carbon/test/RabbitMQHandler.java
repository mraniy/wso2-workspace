package org.wso2.carbon.test;

import com.rabbitmq.client.Channel;
import org.apache.synapse.ManagedLifecycle;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.SynapseEnvironment;
import org.apache.synapse.rest.AbstractHandler;
import org.wso2.carbon.config.RabbitMQConfig;
import org.wso2.carbon.dto.RabbitMQDataDTO;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

public class RabbitMQHandler  extends AbstractHandler implements ManagedLifecycle {

    private static Properties retrieveProperties() {
        Properties properties = new Properties();
        try (InputStream input = RabbitMQHandler.class.getClassLoader().getResourceAsStream("config.properties")) {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }



    private static void cleanRessources(Channel channel) throws IOException, TimeoutException {
        channel.close();
        channel.getConnection().close();
    }

    private static void sendMessageToRabbit(String request, RabbitMQDataDTO rabbitMQDataDTO, Channel channel) throws IOException {
        channel.basicPublish("", rabbitMQDataDTO.getQueuename(), null, request.getBytes());
    }

    private static Channel configureRabbit(RabbitMQDataDTO rabbitMQDataDTO) throws Exception {
        RabbitMQConfig rabbitMQConfig = new RabbitMQConfig();

        // configuration de rabbitMQ
        return rabbitMQConfig.configure(rabbitMQDataDTO);
    }



    public boolean handleRequest(MessageContext messageContext) {
        return true;
    }

    public boolean handleResponse(MessageContext messageContext) {
        String request = (String) messageContext.getProperty("REST_FULL_REQUEST_PATH");
        try {
            Properties properties = retrieveProperties();
            RabbitMQDataDTO rabbitMQDataDTO = RabbitMQDataDTO.builder()
                    .host(properties.getProperty("rabbit.host"))
                    .port(Integer.parseInt(properties.getProperty("rabbit.port")))
                    .user(properties.getProperty("rabbit.username"))
                    .password(properties.getProperty("rabbit.password"))
                    .queuename(properties.getProperty("rabbit.queuename"))
                    .build();
            Channel channel = configureRabbit(rabbitMQDataDTO);
            // Envoi d'un message à la queue
            sendMessageToRabbit(request, rabbitMQDataDTO, channel);
            // Fermeture du canal et de la connexion
            cleanRessources(channel);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void init(SynapseEnvironment synapseEnvironment) {
    }

    @Override
    public void destroy() {
    }


}

