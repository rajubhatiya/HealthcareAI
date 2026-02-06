package com.ai.platform.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@org.springframework.context.annotation.Import(com.ai.platform.shared.config.KafkaTopicConfig.class)
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @org.springframework.context.annotation.Bean
    public org.springframework.cloud.gateway.route.RouteLocator customRouteLocator(
            org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder builder) {
        return builder.routes()
                .route("retrieval_service_route", r -> r.path("/api/rag/**")
                        .uri("http://retrieval-service:8083"))
                .build();
    }

    @org.springframework.context.annotation.Bean
    public org.springframework.cloud.gateway.filter.GlobalFilter customGlobalFilter() {
        return (exchange, chain) -> {
            org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(GatewayApplication.class);
            logger.info("Incoming request: {} {}", exchange.getRequest().getMethod(), exchange.getRequest().getURI());
            return chain.filter(exchange).then(reactor.core.publisher.Mono.fromRunnable(() -> {
                logger.info("Response status: {}", exchange.getResponse().getStatusCode());
            }));
        };
    }
}
