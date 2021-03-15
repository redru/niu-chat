package io.zen.niu.chat.live;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.socket.WebSocketSession;

public enum WsSessions {
  I;

  private final Map<String, WebSocketSession> sessions = Collections.synchronizedMap(new HashMap<>());

  public void put(String key, WebSocketSession session) {
    sessions.put(key, session);
  }

  public boolean exists(String key) {
    return sessions.containsKey(key);
  }

  public WebSocketSession remove(String key) {
    return sessions.remove(key);
  }

}
