package io.zen.niu.chat.authentication;

import static io.zen.niu.domain.tables.Roles.ROLES;
import static io.zen.niu.domain.tables.Users.USERS;
import static io.zen.niu.domain.tables.UsersRoles.USERS_ROLES;

import io.zen.niu.domain.tables.pojos.Users;
import java.util.Set;
import java.util.stream.Collectors;
import org.jooq.DSLContext;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class NiiuUserDetailsService implements UserDetailsService {

  private final DSLContext dslContext;

  public NiiuUserDetailsService(DSLContext dslContext) {
    this.dslContext = dslContext;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Users user = dslContext.select()
        .from(USERS)
        .where(USERS.USERNAME.eq(username))
        .fetchOneInto(Users.class);

    if (user == null) {
      throw new UsernameNotFoundException("Username " + username + " was not found");
    }

    Set<GrantedAuthority> roles = dslContext.select(ROLES.ID)
        .from(ROLES)
        /**/.join(USERS_ROLES)
        /*  */.on(USERS_ROLES.ROLE_ID.eq(ROLES.ID))
        .where(USERS_ROLES.USER_ID.eq(user.getId()))
        .fetchInto(String.class)
        .stream()
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toSet());

    return new NiiuUser(
        user.getUsername(),
        user.getPassword(),
        "ACTIVE".equals(user.getStatus()),
        true,
        true,
        true,
        roles,
        user);
  }

}
