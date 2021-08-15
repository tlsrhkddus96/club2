package org.zerock.club2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Club2Application {

	public static void main(String[] args) {
		SpringApplication.run(Club2Application.class, args);
	}

}
