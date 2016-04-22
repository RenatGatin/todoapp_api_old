package ca.gatin.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.gatin.model.security.Authority;

/**
 * Authority JPA persistence
 *
 * @author RGatin
 * @since Apr 17, 2016
 *
 */
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
}
