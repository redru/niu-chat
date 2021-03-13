package io.zen.niu.chat.live;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class LiveSocketHandler extends TextWebSocketHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(LiveSocketHandler.class);

  private final Map<String, WebSocketSession> sessions = Collections.synchronizedMap(new HashMap<>());

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws IOException {
    final Principal principal = session.getPrincipal();

    if (principal == null) {
      session.close();
      return;
    }

    LOGGER.info("User {} connected to websocket", principal.getName());
    sessions.put(principal.getName(), session);
  }

  @Override
  @SuppressWarnings("unchecked")
  public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
    Map<String, Object> value = new ObjectMapper().readValue(message.getPayload(), Map.class);

    session.sendMessage(new TextMessage("Hello " + value.get("name") + " !"));
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    Iterator<Entry<String, WebSocketSession>> sessionsIterator = sessions.entrySet().iterator();

    while (sessionsIterator.hasNext()) {
      Entry<String, WebSocketSession> it = sessionsIterator.next();

      if (!it.getValue().isOpen()) {
        it.getValue().close();
        sessionsIterator.remove();
      }
    }
  }

}
