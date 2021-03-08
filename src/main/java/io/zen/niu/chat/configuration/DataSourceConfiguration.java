package io.zen.niu.chat.configuration;

import javax.sql.DataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfiguration {

  @Bean
  public DSLContext dslContext(DataSource dataSource) {
    return DSL.using(dataSource, SQLDialect.MYSQL);
  }

}
