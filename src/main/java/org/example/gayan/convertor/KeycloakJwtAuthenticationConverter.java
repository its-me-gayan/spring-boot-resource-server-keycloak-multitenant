package org.example.gayan.convertor;

import org.example.gayan.config.IssuerPropConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 4/21/24
 * Time: 8:32 PM
 */
@Component
public class KeycloakJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken>
{
    private static final String RESOURCE_ACCESS = "resource_access";
    private static final String ROLES = "roles";
    private static final String ROLE_PREFIX = "ROLE_";

    @Autowired
    private IssuerPropConfig issuerPropConfig;

    @Override
    public AbstractAuthenticationToken convert(Jwt source)
    {
        Set<GrantedAuthority> collect = Stream.concat(new JwtGrantedAuthoritiesConverter().convert(source)
                        .stream(), extractClientRoles(source).stream())
                .collect(Collectors.toSet());
        collect.forEach(grantedAuthority -> System.out.println(grantedAuthority.getAuthority()));
        return new JwtAuthenticationToken(source, collect);
    }
    private Collection<GrantedAuthority> extractClientRoles(Jwt jwt) {
        Map<String, Object> resourceAccess = jwt.getClaim(RESOURCE_ACCESS);
        Object iss = jwt.getClaim("iss");
        Optional<String> any = issuerPropConfig.getIssuers().stream().filter(s -> s.equalsIgnoreCase(iss.toString())).findAny();
        if(Objects.nonNull(iss) && any.isPresent()){
            Map<String, Object> resource;
            Collection<String> resourceRoles;
            Object azp = jwt.getClaim("azp");
            String tokenIssuedPartAzp = "";
            if(Objects.nonNull(azp)){
                tokenIssuedPartAzp = azp.toString();
            }
            if ( resourceAccess == null
                    || (resource = (Map<String, Object>) resourceAccess.get(tokenIssuedPartAzp)) == null
                    || (resourceRoles = (Collection<String>) resource.get(ROLES)) == null) {
                return Set.of();
            }
            return resourceRoles.stream()
                    .map(role -> new SimpleGrantedAuthority(ROLE_PREFIX + role))
                    .collect(Collectors.toSet());
        }else {
           throw new IllegalArgumentException("invalid unknown issuer");

        }

    }
}
