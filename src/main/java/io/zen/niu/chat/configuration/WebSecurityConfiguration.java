package io.zen.niu.chat.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable()
        .authorizeRequests()
        .antMatchers("/admin/**").hasRole("ADMIN")
        .anyRequest().authenticated()
        .and()
        .formLogin()
        .permitAll()
        .and()
        .logout()
        .permitAll();
  }

  @Bean
  @Override
  public UserDetailsService userDetailsService() {
    UserDetails user = User.withDefaultPasswordEncoder()
        .username("user")
        .password("user")
        .roles("USER")
        .build();

    UserDetails user2 = User.withDefaultPasswordEncoder()
        .username("admin")
        .password("admin")
        .roles("ADMIN")
        .build();

    return new InMemoryUserDetailsManager(user, user2);
  }

  /*@Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }*/

}
