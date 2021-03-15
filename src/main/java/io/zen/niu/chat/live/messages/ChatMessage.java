package io.zen.niu.chat.live.messages;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ChatMessage {

  private final String to;
  private final String message;
  private final LocalDateTime dateTime;

}
