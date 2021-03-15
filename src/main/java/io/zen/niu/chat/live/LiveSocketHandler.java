package io.zen.niu.chat.live;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.zen.niu.chat.authentication.NiiuUser;
import io.zen.niu.chat.live.messages.ChatMessage;
import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class LiveSocketHandler extends TextWebSocketHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(LiveSocketHandler.class);

  private final ObjectMapper mapper;

  public LiveSocketHandler(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws IOException {
    Principal principal = session.getPrincipal();

    if (principal == null) {
      session.close();
      LOGGER.info("Socket {} not authenticated", session.getId());
      return;
    }

    NiiuUser niiuUser = NiiuUser.from(principal);
    String username = niiuUser.getUser().getUsername();

    if (WsSessions.I.exists(niiuUser.getUsername())) {
      session.close();
      LOGGER.info("User {} already connected", username);
      return;
    }

    WsSessions.I.put(username, session);
    LOGGER.info("User {} connected to websocket", username);
  }

  @Override
  @SuppressWarnings("unchecked")
  public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
    byte[] bytes = message.asBytes();

    ChatMessage value = mapper.readValue(Arrays.copyOfRange(bytes, 0, bytes.length), ChatMessage.class);

    session.sendMessage(new TextMessage(mapper.writeValueAsBytes(value)));
//    session.sendMessage(new TextMessage("Hello " + value.get("name") + " !"));
  }

  /*private boolean isJsonMessage(byte[] bytes) {
    return bytes[0] == 1;
  }*/

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    Principal principal = session.getPrincipal();

    if (principal == null) {
      session.close();
      return;
    }

    NiiuUser niiuUser = NiiuUser.from(principal);
    String username = niiuUser.getUser().getUsername();

    session.close();
    WsSessions.I.remove(username);
    LOGGER.info("User {} disconnected", niiuUser.getUser().getUsername());
  }

}
