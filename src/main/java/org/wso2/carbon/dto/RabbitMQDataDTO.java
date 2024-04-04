package org.wso2.carbon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class RabbitMQDataDTO {
    private String host;
    private int port;
    private String user;
    private String password;
    private String queuename;
}



