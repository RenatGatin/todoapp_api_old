package ca.gatin.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ca.gatin.model.todo.TodoList;

/**
 * User JPA persistence
 * 
 * @author RGatin
 * @since Oct 24, 2018
 */
@Repository
public interface TodoListRepository extends JpaRepository<TodoList, Long> {
	
}