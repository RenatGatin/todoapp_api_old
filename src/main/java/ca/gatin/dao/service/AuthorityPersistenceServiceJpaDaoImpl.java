package ca.gatin.dao.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.gatin.dao.repository.AuthorityRepository;
import ca.gatin.model.security.Authorities;
import ca.gatin.model.security.Authority;

@Service
public class AuthorityPersistenceServiceJpaDaoImpl implements AuthorityPersistenceService {

	@Autowired
	private AuthorityRepository authorityRepository;

	@Override
	public List<Authority> getAll() {
		List<Authority> allAuthorities = authorityRepository.findAll();
		return allAuthorities;
	}
	
	@Override
	public Authority findOneBy(Authorities authorities) {
		Authority authority = authorityRepository.findOneByName(authorities.name());
		return authority;
	}

}
