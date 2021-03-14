package io.zen.niu.chat.authentication;

import io.zen.niu.domain.tables.pojos.Users;
import java.util.Set;
import java.util.stream.Collectors;
import org.jooq.types.UInteger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class NiiuUserDetailsService implements UserDetailsService {

  private final UserDao userDao;
  private final RoleDao roleDao;

  public NiiuUserDetailsService(
      UserDao userDao,
      RoleDao roleDao
  ) {
    this.userDao = userDao;
    this.roleDao = roleDao;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Users user = userDao.getUser(username)
        .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " was not found"));

    Set<GrantedAuthority> roles = roleDao.getRoles(user.getId())
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
