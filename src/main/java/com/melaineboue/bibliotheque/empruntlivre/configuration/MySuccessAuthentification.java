package com.melaineboue.bibliotheque.empruntlivre.configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class MySuccessAuthentification extends SimpleUrlAuthenticationSuccessHandler{

	//Pour retourner un code 200 si l'authentification est bien fait
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,HttpServletResponse response, Authentication authentication)
	{
		response.setStatus(HttpStatus.OK.value());
	}
	
}
