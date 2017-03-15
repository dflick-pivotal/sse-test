package com.example;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@SpringBootApplication
@RestController
@EnableScheduling
public class SseTestApplication {


	private final Map<String, SseEmitter> sses = new ConcurrentHashMap<>();

	@Scheduled(fixedRate = 5000)
    public void sayHello() {
		sses.forEach((k, sse) -> {
			try {
				sse.send("Hello!!!");
			}
			catch (IOException e) {
				throw new RuntimeException(e);
			}
		});    }
	
	@GetMapping("/test/{client}")
	SseEmitter files(@PathVariable String client) {
		SseEmitter sseEmitter = new SseEmitter(60 * 60 * 1000L);
		sses.put(client, sseEmitter);
		return sseEmitter;
	}

	public static void main(String[] args) {
		SpringApplication.run(SseTestApplication.class, args);
	}
}