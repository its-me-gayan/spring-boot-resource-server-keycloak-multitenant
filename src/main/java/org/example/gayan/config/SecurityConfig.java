package org.example.gayan.config;

import org.example.gayan.convertor.KeycloakJwtAuthenticationConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;
import org.springframework.security.web.SecurityFilterChain;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 4/21/24
 * Time: 4:42 PM
 */
@Configuration
public class SecurityConfig{

    @Autowired
    private KeycloakJwtAuthenticationConverter authenticationConverter;

    @Autowired
    private IssuerPropConfig issuerPropConfig;
    Map<String, AuthenticationManager> authenticationManagers = new HashMap<>();

    JwtIssuerAuthenticationManagerResolver authenticationManagerResolver =
            new JwtIssuerAuthenticationManagerResolver(authenticationManagers::get);

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        issuerPropConfig.getIssuers().forEach(issuer -> addManager(authenticationManagers, issuer));

        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry.anyRequest()
                                .authenticated()
                )
                // Ignoring the session cookie
                .sessionManagement(configure ->
                        configure.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(
                        resourceServer -> resourceServer.authenticationManagerResolver(authenticationManagerResolver)
                );
        return http.build();
    }

    public void addManager(Map<String, AuthenticationManager> authenticationManagers, String issuer) {
        JwtDecoder jwtDecoder = JwtDecoders.fromOidcIssuerLocation(issuer);
        JwtAuthenticationProvider authenticationProvider = new JwtAuthenticationProvider(jwtDecoder);
        authenticationProvider.setJwtAuthenticationConverter(authenticationConverter);
        authenticationManagers.put(issuer, authenticationProvider::authenticate);
    }
}
