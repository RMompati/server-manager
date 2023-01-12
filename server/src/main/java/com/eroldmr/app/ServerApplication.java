package com.eroldmr.app;

import com.eroldmr.app.server.Server;
import com.eroldmr.app.server.ServerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static com.eroldmr.app.enumeration.Status.SERVER_DOWN;
import static com.eroldmr.app.enumeration.Status.SERVER_UP;

@SpringBootApplication
public class ServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

	@Bean
	CommandLineRunner run(ServerRepository serverRepository) {
		return args -> {
			serverRepository.save(
							new Server(null, "146.64.217.42", "Patco's Machine", "16 GB", "Desktop",
											"http://localhost:8080/server/image/server1.png", SERVER_UP));
			serverRepository.save(
							new Server(null, "146.64.216.249", "Morebodi's Linux Machine", "16 GB", "Desktop",
											"http://localhost:8080/server/image/server3.png", SERVER_UP));
			serverRepository.save(
							new Server(null, "192.168.1.162", "Fedora Linux", "16 GB", "Dell Box",
											"http://localhost:8080/server/image/server2.png", SERVER_DOWN));
			serverRepository.save(
							new Server(null, "192.168.1.163", "Red Hat Ent. Linux", "16 GB", "Mail Server",
											"http://localhost:8080/server/image/server4.png", SERVER_UP));
			serverRepository.save(
							new Server(null, "146.64.19.112", "Dashney's Machine", "26 GB", "Desktop",
											"http://localhost:8080/server/image/server3.png", SERVER_UP));
		};
	}

}
