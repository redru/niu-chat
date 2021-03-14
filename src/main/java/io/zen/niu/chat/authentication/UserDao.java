package io.zen.niu.chat.authentication;

import static io.zen.niu.domain.tables.Users.USERS;

import io.zen.niu.domain.tables.pojos.Users;
import java.util.Optional;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {

  private final DSLContext dslContext;

  public UserDao(DSLContext dslContext) {
    this.dslContext = dslContext;
  }

  public Optional<Users> getUser(String email) {
    Users user = dslContext.select()
        .from(USERS)
        .where(USERS.EMAIL.eq(email))
        .fetchOneInto(Users.class);

    return Optional.ofNullable(user);
  }

}
