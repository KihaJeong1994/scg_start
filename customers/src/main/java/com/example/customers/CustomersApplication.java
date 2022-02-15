package com.example.customers;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class CustomersApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomersApplication.class, args);
	}

	private final String [] names = "Jean,Yuxin,Mario,Zhen,Mia,Maria,Dave,Johan,Francoise".split(",");

	private final AtomicInteger counter = new AtomicInteger();

	private final Flux<Customer> customers = Flux.fromStream(
		Stream.generate(()->{
				var id = counter.incrementAndGet();
				return new Customer(id,names[id%names.length]);
		})
	).delayElements(Duration.ofSeconds(3));

	@Bean
	Flux<Customer> customers (){
		return this.customers.publish().autoConnect();
	}

}

@Configuration
@RequiredArgsConstructor
class CustomerWebSocketConfiguration{

	private final ObjectMapper objectMapper;

	@SneakyThrows
	private String from (Customer customer){
		return this.objectMapper.writeValueAsString(customer);
	}

	@Bean
	WebSocketHandler webSocketHandler(Flux<Customer> customerFlux){
		return  webSocketSession ->{
			
				var map = customerFlux.map(customer -> from(customer))
							.map(webSocketSession::textMessage);
				return webSocketSession.send(map);
				
			
		};
	}

	@Bean
	SimpleUrlHandlerMapping simpleUrlHandlerMapping(WebSocketHandler customersWsh){
		return new SimpleUrlHandlerMapping(Map.of("/ws/customers",customersWsh),10);
	}
}

@RestController
@RequiredArgsConstructor
class CustomerRestController{

	private final Flux<Customer> customerFlux;

	@GetMapping(
		produces = MediaType.TEXT_EVENT_STREAM_VALUE,
		value = "/customers"
	)
	Flux<Customer> get(){
		return this.customerFlux;
	}
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Customer {
	private Integer id;
	private String name;
}