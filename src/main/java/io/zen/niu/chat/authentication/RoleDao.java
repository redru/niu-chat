package io.zen.niu.chat.authentication;

import static io.zen.niu.domain.tables.Roles.ROLES;
import static io.zen.niu.domain.tables.UsersRoles.USERS_ROLES;

import java.util.List;
import org.jooq.DSLContext;
import org.jooq.types.UInteger;
import org.springframework.stereotype.Service;

@Service
public class RoleDao {

  private final DSLContext dslContext;

  public RoleDao(DSLContext dslContext) {
    this.dslContext = dslContext;
  }

  public List<String> getRoles(UInteger userId) {
    return dslContext.select(ROLES.ID)
        .from(ROLES)
        /**/.join(USERS_ROLES)
        /*  */.on(USERS_ROLES.ROLE_ID.eq(ROLES.ID))
        .where(USERS_ROLES.USER_ID.eq(userId))
        .fetchInto(String.class);
  }

}
