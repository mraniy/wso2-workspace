package org.wso2.carbon.properties;

import com.moandjiezana.toml.Toml;
import org.wso2.carbon.dto.RabbitMQDataDTO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

public class TomlLoader implements PropertyRetriever {

    private static final Logger logger = Logger.getLogger(TomlLoader.class.getName());

    @Override
    public RabbitMQDataDTO retrieveProperties() {
        logger.info("retrieve properties from deployment.toml");
        String deploymenttoml = System.getProperty("carbon.home") + "/repository/conf/deployment.toml";
        // Création d'un objet Toml à partir du fichier
        Toml toml = loadFileIgnoreForbiddenProperty(new File(deploymenttoml));
        return RabbitMQDataDTO.builder()
                .host(toml.getString("broker.host"))
                .port(Long.valueOf(toml.getLong("broker.port")).intValue())
                .user(toml.getString("broker.username"))
                .password(toml.getString("broker.password"))
                .queuename(toml.getString("broker.queuename"))
                .build();
    }

    private Toml loadFileIgnoreForbiddenProperty(File file) {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.replace("'header.X-WSO2-KEY-MANAGER' = \"default\"", ""); // Exemple de modification
                stringBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Charger le fichier TOML modifié
        return new Toml().read(stringBuilder.toString());

    }
}
