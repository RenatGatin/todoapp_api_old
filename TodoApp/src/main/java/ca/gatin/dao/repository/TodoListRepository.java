package ca.gatin.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ca.gatin.model.security.User;
import ca.gatin.model.todo.TodoList;

/**
 * User JPA persistence
 * 
 * @author RGatin
 * @since Oct 24, 2018
 */
@Repository
public interface TodoListRepository extends JpaRepository<TodoList, Long> {

	List<TodoList> findByCreator(User user);

	@Query(value = "SELECT * FROM todo_list WHERE LOWER(name) = LOWER(:name) LIMIT 1", nativeQuery = true)
	TodoList findByNameCaseInsensitive(@Param("name") String name);
	
}