package com.example.git_demo.websocket;


import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@ServerEndpoint(value = "/demo/websocket")
@Component
public class DemoEndpoint {

    private Session session;

    @OnMessage
    public void onMessage(String message) throws IOException {
        log.info("[DemoEndpoint] 收到消息：id={}，message={}", this.session.getId(), message);
        String ask = "问：" + message + "\n";
        String ans = "答：server time" + LocalDateTime.now();
        this.session.getAsyncRemote().sendText(ask + ans);
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        this.session = session;
        log.info("[DemoEndpoint] 新的连接：id={}", this.session.getId());
    }

    @OnClose
    public void onClose(CloseReason closeReason) {
        log.info("[DemoEndpoint] 连接断开：id={}", this.session.getId());
    }

    @OnError
    public void onError(Throwable throwable) throws Exception {
        log.info("[DemoEndpoint] 连接异常：id={}，throwable={}", this.session.getId(), throwable.getMessage());
        this.session.close(new CloseReason(CloseReason.CloseCodes.UNEXPECTED_CONDITION, throwable.getMessage()));
    }
}