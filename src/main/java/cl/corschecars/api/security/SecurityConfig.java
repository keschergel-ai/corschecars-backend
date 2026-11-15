package cl.corschecars.api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import java.util.Collection;
import java.util.List;

@Configuration
public class SecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        Converter<Jwt, Collection<GrantedAuthority>> authoritiesConverter = jwt -> {
            List<String> roles = jwt.getClaimAsStringList("roles");
            if (roles == null) {
                return List.of();
            }
            return roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .map(authority -> (GrantedAuthority) authority)
                    .toList();
        };
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/auth/**", "/api/contactos", "/swagger-ui.html",
                                "/swagger-ui/**", "/v3/api-docs", "/v3/api-docs/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/vehiculos", "/api/vehiculos/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/vehiculos")
                        .hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/vehiculos/**")
                        .hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/vehiculos/**")
                        .hasAnyRole("USER", "ADMIN")
                        .anyRequest()
                        .denyAll())
                .oauth2ResourceServer(resourceServer -> resourceServer.jwt(jwt ->
                        jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)))
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .build();
    }
}
