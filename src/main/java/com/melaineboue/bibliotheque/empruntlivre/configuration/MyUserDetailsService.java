package com.melaineboue.bibliotheque.empruntlivre.configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.melaineboue.bibliotheque.empruntlivre.entities.User;
import com.melaineboue.bibliotheque.empruntlivre.repositories.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService
{

	@Autowired
	UserRepository userRepository;
	 
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email);
		
		if(user == null)
			throw new UsernameNotFoundException(email);
		UserDetails userDetails = new UserPrincipal(user);
		return userDetails;
	}
	
	public static class UserPrincipal implements UserDetails
	{
		User user;
		private final String DEFAULT_PASSWORD = "123456";
		
		public UserPrincipal(User user) {
			this.user = user;
			this.user.setPassword("{noop}"+ user.getPassword());
		}

		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			List<GrantedAuthority> listeAuthorities = new ArrayList<>();
			listeAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
			return listeAuthorities;
		}

		@Override
		public String getPassword() {
			return this.user.getPassword();
		}

		@Override
		public String getUsername() {
			return this.user.getEmail();
		}

		@Override
		public boolean isAccountNonExpired() {
			return true;
		}

		@Override
		public boolean isAccountNonLocked() {
			return true;
		}

		@Override
		public boolean isCredentialsNonExpired() {
			return true;
		}

		@Override
		public boolean isEnabled() {
			return true;
		}
		
	}

}
