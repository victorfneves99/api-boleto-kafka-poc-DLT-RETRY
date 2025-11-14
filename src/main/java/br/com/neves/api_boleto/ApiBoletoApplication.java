package br.com.neves.api_boleto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableKafka
@EnableRetry
public class ApiBoletoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiBoletoApplication.class, args);
	}

}
