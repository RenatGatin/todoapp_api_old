package ca.gatin.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ca.gatin.model.security.Authority;

/**
 * Authority JPA persistence
 *
 * @author RGatin
 * @since Apr 17, 2016
 *
 */
public interface AuthorityRepository extends JpaRepository<Authority, Long> {

	@Query(value = "SELECT * FROM authority WHERE LOWER(name) = LOWER(:name) LIMIT 1", nativeQuery = true)
	Authority findOneByName(@Param("name") String name);
}
