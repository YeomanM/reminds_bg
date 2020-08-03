package com.mjj.wxdemoreminds.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "wx")
public class PropertiesUtil {

    private String appId;
    private String appSecret;

}
