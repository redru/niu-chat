package io.zen.niu.chat.chat;

import io.zen.niu.chat.authentication.NiiuUser;
import io.zen.niu.domain.tables.pojos.Messages;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/chat")
public class ChatApi {

  private final MessageRepository messageRepository;

  public ChatApi(MessageRepository messageRepository) {
    this.messageRepository = messageRepository;
  }

  @PostMapping(path = "/message", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public Messages postMessage(Principal principal, @RequestBody Messages message) {
    NiiuUser niiuUser = NiiuUser.from(principal);

    LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);

    message.setId(UUID.randomUUID().toString());
    message.setUserId(niiuUser.getUser().getId());
    message.setCreateDate(now);
    message.setTimestamp(now.toInstant(ZoneOffset.UTC).toEpochMilli());

    messageRepository.create(message);

    return message;
  }

}
