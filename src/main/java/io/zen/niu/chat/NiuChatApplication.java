package io.zen.niu.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration;

@SpringBootApplication(exclude = { JooqAutoConfiguration.class })
public class NiuChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(NiuChatApplication.class, args);
	}

}
