package com.example.basics;

import java.beans.BeanProperty;
import java.time.ZonedDateTime;

import org.reactivestreams.Publisher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.handler.AsyncPredicate;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@SpringBootApplication
public class BasicsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BasicsApplication.class, args);
	}

	// @Bean
	// RouteLocator gateway(RouteLocatorBuilder rlb){
	// 	return rlb
	// 			.routes()
	// 			.route(routeSpec -> routeSpec
	// 				//predicate : 조건
	// 				.path("/hello")
	// 				.and().host("*.spring.io")
	// 				// .and().asyncPredicate(serverWebExchange->
	// 				// 		 Mono.just( serverWebExchange.getAttribute("foo")))
	// 				// filter : 처리
	// 				.filters(gatewayFilterSpec -> 
	// 						gatewayFilterSpec.setPath("/guides")	
	// 				)
	// 				// 목적지
	// 				.uri("https://spring.io/")
	// 			)
	// 			.route("twitter", routeSpec->
	// 						routeSpec
	// 							.path("/twitter/**")
	// 					.filters(fs->fs.rewritePath(
	// 						"/twitter/(?<handle>.*)"
	// 						, "/${handle}"
	// 					))
	// 					.uri("http://twitter.com/@")
	// 			)
	// 			.build();
	// }

}
