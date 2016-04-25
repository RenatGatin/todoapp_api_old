package ca.gatin.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ca.gatin.model.security.User;

/**
 * User JPA persistence
 * 
 * @author RGatin
 * @since Apr 17, 2016
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@Query(value = "SELECT * FROM user WHERE LOWER(username) = LOWER(:username) LIMIT 1", nativeQuery = true)
	User findByUsernameCaseInsensitive(@Param("username") String username);

	@Query(value = "SELECT * FROM user WHERE LOWER(email) = LOWER(:email) LIMIT 1", nativeQuery = true)
	User findByEmailCaseInsensitive(@Param("email") String email);

	@Query(value = "SELECT COUNT(*) FROM user WHERE LOWER(username) = LOWER(:username)", nativeQuery = true)
	int countByUsername(@Param("username") String username);

	@Query(value = "SELECT COUNT(*) FROM user WHERE LOWER(email) = LOWER(:email)", nativeQuery = true)
	int countByEmail(@Param("email") String email);

	List<User> findByActivated(boolean activated);

	List<User> findByEnabled(boolean enabled);

	@Modifying
	@Query(value = "UPDATE user SET activated = :activate WHERE id = :id", nativeQuery = true)
	int activate(@Param("activate") boolean activate, @Param("id") Long id);

	@Modifying
	@Query(value = "UPDATE user SET enabled = :enable WHERE id = :id", nativeQuery = true)
	int enable(@Param("enable") boolean activate, @Param("id") Long id);

	@Query(value = "SELECT u.* FROM user u, authority a, user_authority ua "
			+ "WHERE u.id = ua.user_id AND a.id = ua.authority_id AND a.name = :roleName "
			+ "ORDER BY u.date_created DESC", nativeQuery = true)
	List<User> findByRole(@Param("roleName") String roleName);

}