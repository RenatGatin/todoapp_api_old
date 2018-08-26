package ca.gatin.dao.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.gatin.dao.repository.PseudoUserRepository;
import ca.gatin.model.security.PseudoUser;

@Service
public class PseudoUserPersistenceServiceJpaDaoImpl implements PseudoUserPersistenceService {

	@Autowired
	private PseudoUserRepository pseudoUserRepository;

	@Override
	public List<PseudoUser> getAll() {
		List<PseudoUser> allUsers = pseudoUserRepository.findAll();
		return allUsers;
	}

	@Override
	public PseudoUser getById(Long id) {
		PseudoUser user = pseudoUserRepository.findOne(id);
		return user;
	}
	
	@Override
	public PseudoUser getByUsername(String username) {
		PseudoUser user = pseudoUserRepository.findByUsernameCaseInsensitive(username);
		return user;
	}
	
	@Override
	public PseudoUser getByActivationKey(String activationKey) {
		PseudoUser user = pseudoUserRepository.findByActivationKeyCaseInsensitive(activationKey);
		return user;
	}
	
	@Override
	public PseudoUser getByEmail(String email) {
		PseudoUser user = pseudoUserRepository.findByEmailCaseInsensitive(email);
		return user;
	}

	@Override
	public PseudoUser save(PseudoUser user) {
		PseudoUser savedUser = pseudoUserRepository.save(user);
		return savedUser;
	}

	@Override
	public boolean existsByUsername(String username) {
		return pseudoUserRepository.countByUsername(username) > 0;
	}
	
	@Override
	public boolean existsByActivationKey(String activationKey) {
		return pseudoUserRepository.countByActivationKey(activationKey) > 0;
	}
	
	@Override
	public boolean existsByEmail(String email) {
		return pseudoUserRepository.countByEmail(email) > 0;
	}

	@Override
	public boolean delete(Long id) {
		boolean deleted = true;
		try {
			pseudoUserRepository.delete(id);
		} catch (Exception e) {
			deleted = false;
		}
		return deleted;
	}

	@Override
	public List<PseudoUser> getActivated() {
		List<PseudoUser> users = pseudoUserRepository.findByActivated(true);
		return users;
	}

	@Override
	public List<PseudoUser> getNotActivated() {
		List<PseudoUser> users = pseudoUserRepository.findByActivated(false);
		return users;
	}

	@Override
	public boolean activate(Long id) {
		return pseudoUserRepository.activate(true, id) > 0;
	}
	
	@Override
	public boolean deactivate(Long id) {
		return pseudoUserRepository.activate(false, id) > 0;
	}

}
