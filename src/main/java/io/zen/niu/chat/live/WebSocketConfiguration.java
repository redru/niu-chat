package io.zen.niu.chat.live;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

  private final ObjectMapper mapper;

  public WebSocketConfiguration(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry.addHandler(new LiveSocketHandler(mapper), "/live")
        .addInterceptors(new HttpSessionHandshakeInterceptor())
        .withSockJS();
  }

}
