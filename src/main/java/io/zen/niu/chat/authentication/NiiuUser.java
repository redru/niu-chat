package io.zen.niu.chat.authentication;

import io.zen.niu.domain.tables.pojos.Users;
import java.util.Collection;
import java.util.Objects;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class NiiuUser extends User {

  private final Users user;

  public NiiuUser(
      String username,
      String password,
      Collection<? extends GrantedAuthority> authorities,
      Users user
  ) {
    super(username, password, authorities);
    this.user = user;
  }

  public NiiuUser(
      String username,
      String password,
      boolean enabled,
      boolean accountNonExpired,
      boolean credentialsNonExpired,
      boolean accountNonLocked,
      Collection<? extends GrantedAuthority> authorities,
      Users user
  ) {
    super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    this.user = user;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    NiiuUser niiuUser = (NiiuUser) o;
    return Objects.equals(user, niiuUser.user);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), user);
  }

}
