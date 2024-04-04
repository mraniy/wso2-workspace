package org.wso2.carbon.test;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.apache.synapse.ManagedLifecycle;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.SynapseEnvironment;
import org.apache.synapse.rest.AbstractHandler;
import org.wso2.carbon.config.RabbitMQConfig;

public class RabbitMQHandler  extends AbstractHandler implements ManagedLifecycle {

    public boolean handleRequest(MessageContext messageContext) {
        return true;
    }

    public boolean handleResponse(MessageContext messageContext) {
        String rest_full_request_path = (String) messageContext.getProperty("REST_FULL_REQUEST_PATH");
        configureAndSendMessage(rest_full_request_path);
        return true;
    }

    @Override
    public void init(SynapseEnvironment synapseEnvironment) {
        System.out.println("ca init");
    }

    @Override
    public void destroy() {
        System.out.println("ca destroy");
    }

    private static void configureAndSendMessage(String request) {
        RabbitMQConfig rabbitMQConfig = new RabbitMQConfig();

        try {
            // Obtention de la connexion RabbitMQ
            Connection connection = rabbitMQConfig.getConnection();
            // Création d'un canal
            Channel channel = connection.createChannel();
            // Déclaration d'une queue
            String QUEUE_NAME = "handlerqueue";
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            // Envoi d'un message à la queue
            channel.basicPublish("", QUEUE_NAME, null, request.getBytes());
            System.out.println("request intercepted with params " +request);
            // Fermeture du canal et de la connexion
            channel.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

