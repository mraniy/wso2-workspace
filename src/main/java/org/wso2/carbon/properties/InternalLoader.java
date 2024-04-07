package org.wso2.carbon.properties;

import org.wso2.carbon.custom.RabbitMQHandler;
import org.wso2.carbon.dto.RabbitMQDataDTO;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class InternalLoader implements PropertyRetriever{
    private static final Logger logger = Logger.getLogger(InternalLoader.class.getName());

    @Override
    public RabbitMQDataDTO retrieveProperties() {
        logger.info("retrieve properties from config.properties");
        Properties properties = new Properties();
        try (InputStream input = RabbitMQHandler.class.getClassLoader().getResourceAsStream("config.properties")) {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return RabbitMQDataDTO.builder()
                .host(properties.getProperty("rabbit.host"))
                .port(Integer.parseInt(properties.getProperty("rabbit.port")))
                .user(properties.getProperty("rabbit.username"))
                .password(properties.getProperty("rabbit.password"))
                .queuename(properties.getProperty("rabbit.queuename"))
                .build();
    }


}
