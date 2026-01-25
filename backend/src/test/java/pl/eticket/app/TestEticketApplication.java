package pl.eticket.app;

import org.springframework.boot.SpringApplication;

public class TestEticketApplication {

	public static void main(String[] args) {
		SpringApplication.from(EticketApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
