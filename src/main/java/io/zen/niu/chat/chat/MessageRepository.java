package io.zen.niu.chat.chat;

import static io.zen.niu.domain.tables.Messages.MESSAGES;

import io.zen.niu.domain.tables.pojos.Messages;
import io.zen.niu.domain.tables.records.MessagesRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
public class MessageRepository {

  private final DSLContext dslContext;

  public MessageRepository(DSLContext dslContext) {
    this.dslContext = dslContext;
  }

  public void create(Messages messages) {
    MessagesRecord recordToInsert = dslContext.newRecord(MESSAGES, messages);

    dslContext.executeInsert(recordToInsert);
  }

}
