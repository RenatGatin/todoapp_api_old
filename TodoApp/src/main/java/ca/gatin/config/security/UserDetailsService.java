package ca.gatin.config.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ca.gatin.dao.service.UserPersistenceService;
import ca.gatin.model.security.Authority;
import ca.gatin.model.security.User;

@Component("userDetailsService")
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

	private final Logger logger = LoggerFactory.getLogger(UserDetailsService.class);

	@Autowired
	private UserPersistenceService userPersistenceService;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(final String login) {

		User userFromDatabase;
		if (login.contains("@")) {
			logger.info("Authenticating by email: {}", login);
			userFromDatabase = userPersistenceService.getUserByEmail(login);
		} else {
			logger.info("Authenticating by username: {}", login);
			userFromDatabase = userPersistenceService.getUserByUsername(login);
		}

		if (userFromDatabase == null) {
			String msg = "User '" + login + "' was not found in the database";
			logger.error(msg);
			throw new UsernameNotFoundException(msg);
			
		} else if (!userFromDatabase.isActivated()) {
			String msg = "User '" + login + "' is not activated";
			logger.error(msg);
			throw new UserNotActivatedException(msg);
		} else if (!userFromDatabase.isEnabled()) {
			String msg = "User '" + login + "' is disabled";
			logger.error(msg);
			throw new UserNotActivatedException(msg);
		}

		Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		Set<Authority> authorities = userFromDatabase.getAuthorities();
		logger.info("User authorities: " + authorities.toString());
		
		for (Authority authority : authorities) {
			GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority.getName());
			grantedAuthorities.add(grantedAuthority);
		}

		return new org.springframework.security.core.userdetails.User(userFromDatabase.getUsername(), userFromDatabase.getPassword(), grantedAuthorities);
	}

}
