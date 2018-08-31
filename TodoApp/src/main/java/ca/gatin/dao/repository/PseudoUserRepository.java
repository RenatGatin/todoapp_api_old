package ca.gatin.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ca.gatin.model.security.PseudoUser;

/**
 * User JPA persistence
 * 
 * @author RGatin
 * @since Aug 25, 2018
 */
@Repository
public interface PseudoUserRepository extends JpaRepository<PseudoUser, Long> {

	@Query(value = "SELECT * FROM pseudo_user WHERE LOWER(username) = LOWER(:username) LIMIT 1", nativeQuery = true)
	PseudoUser findByUsernameCaseInsensitive(@Param("username") String username);
	
	@Query(value = "SELECT * FROM pseudo_user WHERE LOWER(activationkey) = LOWER(:activationkey) LIMIT 1", nativeQuery = true)
	PseudoUser findByActivationKeyCaseInsensitive(@Param("activationkey") String username);

	@Query(value = "SELECT * FROM pseudo_user WHERE LOWER(email) = LOWER(:email) LIMIT 1", nativeQuery = true)
	PseudoUser findByEmailCaseInsensitive(@Param("email") String email);

	@Query(value = "SELECT COUNT(*) FROM pseudo_user WHERE LOWER(activationkey) = LOWER(:activationkey)", nativeQuery = true)
	int countByActivationKey(@Param("activationkey") String activationkey);
	
	@Query(value = "SELECT COUNT(*) FROM pseudo_user WHERE LOWER(username) = LOWER(:username)", nativeQuery = true)
	int countByUsername(@Param("username") String username);

	@Query(value = "SELECT COUNT(*) FROM pseudo_user WHERE LOWER(email) = LOWER(:email)", nativeQuery = true)
	int countByEmail(@Param("email") String email);

	List<PseudoUser> findByActivated(boolean activated);

	@Modifying
	@Transactional
	@Query(value = "UPDATE pseudo_user SET activated = :activate WHERE id = :id", nativeQuery = true)
	int activate(@Param("activate") boolean activate, @Param("id") Long id);
}