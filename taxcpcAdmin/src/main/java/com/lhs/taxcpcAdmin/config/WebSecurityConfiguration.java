package com.lhs.taxcpcAdmin.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * @author ayushi.jain
 *
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Bean
	public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
		StrictHttpFirewall firewall = new StrictHttpFirewall();
		firewall.setAllowUrlEncodedSlash(true);
		firewall.setAllowSemicolon(true);
		return firewall;
	}// End Method

	@Override
	public void configure(WebSecurity web) throws Exception {
		/**
		 * The following paths will be ignored by Spring Security
		 */
		web.ignoring().antMatchers("/api/**","/forgetPassword","/forgotPassword", "/changePassword","/checkPassword","/config/**", "/static/**", "/static/css/**", "/static/images/**",
				"/static/js/**", "/static/sass/**", "/static/stylesheets/**", "/templates/**",
				"/static,/font-awesome/**", "/static/font/**", "/templates/fragments/**", "/templates/pages/**",
				"/error", "/actuator/**", "/");
	}

	/**
	 * This are the request URL mapping with spring security
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/").permitAll()
				.anyRequest().authenticated().and().formLogin()
				.loginPage("/login")
				.loginProcessingUrl("/app-login").defaultSuccessUrl("/auth", true)
				.failureUrl("/invalid-login").permitAll().and().logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login")
				.logoutRequestMatcher(new AntPathRequestMatcher("/")).logoutSuccessUrl("/login").permitAll();
		http.csrf().disable();
		http.headers().disable();
		http.headers().defaultsDisabled().cacheControl();
		http.headers().frameOptions().sameOrigin();

	}

	/**
	 * This Spring API to authenticate The username and password given by the User
	 * while login in application.
	 * 
	 * @param auth
	 * @throws Exception
	 */
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}

	@Bean
	public static NoOpPasswordEncoder passwordEncoder() {
		return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
	}

}
