package org.wso2.carbon.properties;

import com.moandjiezana.toml.Toml;
import org.wso2.carbon.dto.RabbitMQDataDTO;

import java.io.File;

public class TomlLoader implements PropertyRetriever {

    @Override
    public RabbitMQDataDTO retrieveProperties() {
        String deploymenttoml = System.getProperty("carbon.home") + "/repository/conf/deployment.toml";
        // Chemin vers votre fichier TOML
        File file = new File(deploymenttoml);
        // Création d'un objet Toml à partir du fichier
        Toml toml = new Toml().read(file);
        return RabbitMQDataDTO.builder()
                .host(toml.getString("broker.host"))
                .port(Long.valueOf(toml.getLong("broker.port")).intValue())
                .user(toml.getString("broker.username"))
                .password(toml.getString("broker.password"))
                .queuename(toml.getString("broker.queuename"))
                .build();
    }
}
