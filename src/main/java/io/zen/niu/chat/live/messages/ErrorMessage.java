package io.zen.niu.chat.live.messages;

import lombok.Data;

@Data
public class ErrorMessage {

  private final String code;
  private final String message;

}
