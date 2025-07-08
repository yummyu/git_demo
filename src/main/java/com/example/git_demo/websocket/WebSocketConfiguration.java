package com.example.git_demo.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

//@Configuration
//@EnableWebSocket
public class WebSocketConfiguration {
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        ServerEndpointExporter exporter = new ServerEndpointExporter();
        exporter.setAnnotatedEndpointClasses(DemoEndpoint.class);
        return exporter;
    }

    /**
     * 容器有数据大小限制，默认为 8192
     * @return container
     */
    @Bean
    public ServletServerContainerFactoryBean servletServerContainerFactoryBean() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean() ;
        container.setMaxTextMessageBufferSize(1024*1024) ;
        container.setMaxBinaryMessageBufferSize(1024*1024) ;
        return container ;
    }
}