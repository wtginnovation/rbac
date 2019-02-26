package de.vsfexperts.rbac.sample.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint in order to return current {@link Authentication} in json format
 */
@RestController
public class AuthenticationController {

	@GetMapping(value = "/auth", produces = APPLICATION_JSON_UTF8_VALUE)
	@PreAuthorize("permitAll()")
	public Authentication listOrders() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

}
