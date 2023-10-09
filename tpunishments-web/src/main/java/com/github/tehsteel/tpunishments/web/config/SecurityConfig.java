package com.github.tehsteel.tpunishments.web.config;

import com.github.tehsteel.tpunishments.web.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig implements WebFilter {

	@Autowired
	private final Environment environment;

	@Bean
	public SecurityWebFilterChain springSecurityFilterChain(final ServerHttpSecurity http) {
		http.csrf(ServerHttpSecurity.CsrfSpec::disable)
				.httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
				.formLogin(ServerHttpSecurity.FormLoginSpec::disable)
				.logout(ServerHttpSecurity.LogoutSpec::disable)
				.authenticationManager(null)
				.addFilterBefore(this, SecurityWebFiltersOrder.AUTHENTICATION);

		return http.build();
	}

	@Override
	public Mono<Void> filter(final ServerWebExchange exchange, final WebFilterChain chain) {
		final String apiKey = exchange.getRequest().getHeaders().getFirst("X-API-Key");

		if (apiKey != null && apiKey.equals(Constants.API_KEY)) {
			return chain.filter(exchange);
		} else {
			exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
			return exchange.getResponse().setComplete();
		}
	}
}