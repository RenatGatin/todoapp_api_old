package ca.gatin.dao.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.gatin.dao.repository.UserRepository;
import ca.gatin.model.security.User;

@Service
public class UserPersistenceServiceJpaDaoImpl implements UserPersistenceService {

	@Autowired
	UserRepository userRepository;

	@Override
	public List<User> getAll() {
		List<User> allUsers = userRepository.findAll();
		return allUsers;
	}

	@Override
	public User getUserById(Long id) {
		User user = userRepository.findOne(id);
		return user;
	}
	
	@Override
	public User getUserByUsername(String username) {
		User user = userRepository.findByUsernameCaseInsensitive(username);
		return user;
	}
	
	@Override
	public User getUserByEmail(String email) {
		User user = userRepository.findByEmailCaseInsensitive(email);
		return user;
	}

	@Override
	public User saveUser(User User) {
		User savedUser = userRepository.save(User);
		return savedUser;
	}

	@Override
	public boolean existsById(Long id) {
		return userRepository.exists(id);
	}
	
	@Override
	public boolean existsByUsername(String username) {
		return userRepository.countByUsername(username) > 0;
	}
	
	@Override
	public boolean existsByEmail(String email) {
		return userRepository.countByEmail(email) > 0;
	}

	@Override
	public boolean deleteUser(Long id) {
		boolean deleted = true;
		try {
			userRepository.delete(id);
		} catch (Exception e) {
			deleted = false;
		}
		return deleted;
	}

	@Override
	public List<User> getActivated() {
		List<User> users = userRepository.findByActivated(true);
		return users;
	}

	@Override
	public List<User> getNotActivated() {
		List<User> users = userRepository.findByActivated(false);
		return users;
	}

	@Override
	public List<User> getEnabled() {
		List<User> users = userRepository.findByEnabled(true);
		return users;
	}

	@Override
	public List<User> getDisabled() {
		List<User> users = userRepository.findByEnabled(false);
		return users;
	}

	@Override
	public boolean activate(Long id) {
		return userRepository.activate(true, id) > 0;
	}
	
	@Override
	public boolean deactivate(Long id) {
		return userRepository.activate(false, id) > 0;
	}

	@Override
	public boolean enable(Long id) {
		return userRepository.enable(true, id) > 0;
	}

	@Override
	public boolean disable(Long id) {
		return userRepository.enable(false, id) > 0;
	}

}
