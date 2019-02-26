package de.vsfexperts.rbac.sample.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;

import de.vsfexperts.rbac.sample.Roles;
import de.vsfexperts.rbac.spring.userdetails.RbacUserDetailsService;

/**
 * Configuration of spring security & rbac starter
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private RbacUserDetailsService rbacUserDetailService;

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		super.configure(http);

		http //
				.csrf().disable() //
				.logout().logoutSuccessUrl("/");
	}

	@Override
	public void configure(final WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/");
	}

	@Override
	protected void configure(final AuthenticationManagerBuilder auth) throws Exception {

		auth.userDetailsService(rbacUserDetailService);

		final UserDetails user = User //
				.withUsername("user") //
				.password("{noop}user") //
				.roles(Roles.USER) //
				.build();

		final UserDetails admin = User //
				.withUsername("admin") //
				.password("{noop}admin") //
				.roles(Roles.ADMIN) //
				.build();

		final UserDetails none = User //
				.withUsername("none") //
				.password("{noop}none") //
				.roles(Roles.NONE) //
				.build();

		final UserDetailsManager wrappedUserDetailsService = auth.inMemoryAuthentication() //
				.withUser(user) //
				.withUser(admin) //
				.withUser(none) //
				.getUserDetailsService();

		rbacUserDetailService.setUserDetailService(wrappedUserDetailsService);

	}

}
