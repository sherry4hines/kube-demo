package com.s3pub.fdademo.fdademoservice.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true) // allows using the @RolesAllowed annotation
public class WebSecurityConfiguration {

	public static final String[] SWAGGER_ENDPOINTS = {
			"/swagger-ui.html",
			"/swagger-ui/**",
			"/v3/api-docs/**",
			"/swagger-resources/**",
			"/swagger-resources"
	};

    @Value("${keycloak.client-id}")
    private String kcClientId;

    @Value("${keycloak.issuer-url}")
    private String tokenIssuerUrl;

	@Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
	String jwkSetUri;

	@Bean
	protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
		return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.cors(Customizer.withDefaults());
		http.authorizeHttpRequests(authorize -> authorize
						.requestMatchers(SWAGGER_ENDPOINTS)
						.permitAll()
						.requestMatchers("/actuator/**", "/api/v1/fdademo/public/**")
						.permitAll()
						.anyRequest().authenticated()
				)
				.oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer
						.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()
								))
						)
				.sessionManagement(
					sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
					)
				.cors(Customizer.withDefaults());
		return http.build();
	}



	@Bean
	CorsConfigurationSource corsConfigurationSource() {
	  CorsConfiguration configuration = new CorsConfiguration();
	  configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173","http://s3publocal.com:5173","https://localhost:5173","https://s3publocal.com:5173", "http://localhost:18080", "https://localhost:18080"));
	  configuration.setAllowedMethods(Arrays.asList("GET","POST","PATCH", "PUT", "DELETE", "OPTIONS", "HEAD"));
	  configuration.setAllowCredentials(true);
	  configuration.setAllowedHeaders(Arrays.asList("Authorization", "Requestor-Type", "Content-Type", "Accept", "Host", "Access-Control-Allow-Origin"));
	  configuration.setExposedHeaders(Arrays.asList("X-Get-Header","Authorization", "Requestor-Type", "Content-Type", "Accept", "Host", "Access-Control-Allow-Origin"));
	  configuration.setMaxAge(3600L);
	  UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	  source.registerCorsConfiguration("/**", configuration);
	  return source;
	}	

	private JwtAuthenticationConverter jwtAuthenticationConverter() {
		JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
		jwtConverter.setJwtGrantedAuthoritiesConverter(new GrantedAuthoritiesConverter());
		return jwtConverter;
	}

    @Bean
    public JwtDecoder jwtDecoder() {
        return JwtDecoders.fromIssuerLocation(tokenIssuerUrl);
    }


}