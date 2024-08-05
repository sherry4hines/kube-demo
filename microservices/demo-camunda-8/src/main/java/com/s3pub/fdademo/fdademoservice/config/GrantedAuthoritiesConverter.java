package com.s3pub.fdademo.fdademoservice.config;


import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class GrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

	@Override
	public Collection<GrantedAuthority> convert(Jwt source) {
		Map<String, Object> realmAccess = source.getClaimAsMap("realm_access");

		if (Objects.nonNull(realmAccess)) {
			@SuppressWarnings("unchecked")
			List<String> roles = (List<String>) realmAccess.get("roles");
			log.info("Roles: {}", roles);
			
			if (Objects.nonNull(roles)) {
				return roles.stream().map(rn -> new SimpleGrantedAuthority("ROLE_" + rn)).collect(Collectors.toList());
			}

		}

		return List.of();
	}

}
