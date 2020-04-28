package com.melaineboue.bibliotheque.empruntlivre.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter{
	
	@Autowired
	MyAccessDeniedHandler accessDeniedHandler;
	
	@Autowired
	MySuccessAuthentification  successAuthentification;
	
	@Autowired
	MyUserDetailsService userDetailsService;
	
	@Autowired
	RestAuthenticationEntryPoint restAuthenticationEntryPoint; 

	private SimpleUrlAuthenticationFailureHandler myFailureHandler = new SimpleUrlAuthenticationFailureHandler();
	
	public void configure(HttpSecurity http) throws Exception {
		http.headers().frameOptions().disable().and()
		.csrf().disable()
		.exceptionHandling()
		.authenticationEntryPoint(restAuthenticationEntryPoint).and()
		.authorizeRequests()
		.antMatchers("/favicon.ico").permitAll()
		.antMatchers("/users/**").permitAll()
		.antMatchers("/loans/**").permitAll()
		.antMatchers("/books/**").permitAll()
		.antMatchers("/h2-console/**").permitAll()
		.anyRequest().authenticated()
		.and()
		.formLogin()
		.successHandler(successAuthentification)
		.failureHandler(myFailureHandler)
		.and()
		.logout()
		.logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK));
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception 
	{
		auth.userDetailsService(this.userDetailsService);
	}
}
