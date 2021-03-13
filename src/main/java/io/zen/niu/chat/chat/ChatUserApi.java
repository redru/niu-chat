package io.zen.niu.chat.chat;

import static io.zen.niu.domain.Tables.MESSAGES;

import io.zen.niu.domain.tables.pojos.Messages;
import io.zen.niu.domain.tables.records.MessagesRecord;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import org.jooq.DSLContext;
import org.jooq.InsertSetStep;
import org.jooq.InsertValuesStepN;
import org.jooq.SelectLimitPercentAfterOffsetStep;
import org.jooq.types.UInteger;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/chat_users")
public class ChatUserApi {

  private final DSLContext dslContext;

  public ChatUserApi(DSLContext dslContext) {
    this.dslContext = dslContext;
  }

  @Transactional
  @GetMapping(path = "/v2/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Messages> getChatUsersV2(
      @PathVariable Long userId,
      @RequestParam(name = "page", required = false, defaultValue = "0") Integer page
  ) {
    SelectLimitPercentAfterOffsetStep<MessagesRecord> selectFrom = dslContext.selectFrom(MESSAGES)
        .where(MESSAGES.USER_ID.eq(UInteger.valueOf(userId)))
        .orderBy(MESSAGES.TIMESTAMP.desc())
        .offset(page * 10)
        .limit(10);

    return selectFrom.fetchInto(Messages.class).stream()
        .sorted(Comparator.comparing(Messages::getTimestamp).reversed())
        .collect(Collectors.toList());
  }

  @PostMapping(path = "/fill_messages")
  public void postFillMessagesTable() {
    for (int x = 0; x < 1_000; x++) {
      InsertSetStep<MessagesRecord> insert = dslContext.insertInto(MESSAGES);
      InsertValuesStepN<MessagesRecord> insertToExecute = null;

      for (int i = 0; i < 1_000; i++) {
        byte[] array = new byte[250];
        new Random().nextBytes(array);
        String generatedString = new String(array, StandardCharsets.UTF_8);

        String id = UUID.randomUUID().toString();
        long from = (long) (Math.random() * 100) + 1;
        long to = (long) (Math.random() * 100) + 1;
        LocalDateTime timestamp = LocalDateTime.now(ZoneOffset.UTC);

        insertToExecute = insert.values(id, from, to, generatedString, timestamp, timestamp.toInstant(ZoneOffset.UTC).toEpochMilli());
      }

      insertToExecute.execute();
    }
  }

}
