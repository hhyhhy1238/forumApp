package com.mxy.bbs_server.configuration;

import jakarta.annotation.Resource;
import lombok.extern.java.Log;
import org.apache.ibatis.io.Resources;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.util.Properties;

@Configuration
@Log
public class AvatarVirtualConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Properties properties=new Properties();
        try {
            properties.load(Resources.getResourceAsStream("resourceLocations.properties"));
        } catch (IOException e) {
            log.info(e.getMessage());
        }
        String location=properties.getProperty("resources_location");
        //file:///D:/%E7%A7%BB%E5%8A%A8%E4%BA%92%E8%81%94%E9%A1%B9%E7%9B%AE/bbs_server-master/ImageResource
        //System.out.println(location);
        registry.addResourceHandler("/ImageResource/**")
                .addResourceLocations(location);
    }
}
