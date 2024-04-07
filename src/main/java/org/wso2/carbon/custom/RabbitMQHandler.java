package org.wso2.carbon.custom;

import com.rabbitmq.client.Channel;
import org.apache.synapse.ManagedLifecycle;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.SynapseEnvironment;
import org.apache.synapse.rest.AbstractHandler;
import org.wso2.carbon.dto.RabbitMQDataDTO;
import org.wso2.carbon.properties.InternalLoader;
import org.wso2.carbon.properties.PropertyRetriever;

import java.util.logging.Logger;

import static org.wso2.carbon.utils.RabbitMQUtils.*;

public class RabbitMQHandler  extends AbstractHandler implements ManagedLifecycle {
    private static final Logger logger = Logger.getLogger(RabbitMQHandler.class.getName());

    public boolean handleRequest(MessageContext messageContext) {
        return true;
    }

    public boolean handleResponse(MessageContext messageContext) {
        logger.info("launching custom handler RabbitMQHandler");
        String request = (String) messageContext.getProperty("REST_FULL_REQUEST_PATH");
        try {
            PropertyRetriever propertyRetriever = new InternalLoader();
            RabbitMQDataDTO rabbitMQDataDTO = propertyRetriever.retrieveProperties();
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
