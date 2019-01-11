package ca.gatin.dao.service;

import java.util.List;

import ca.gatin.model.security.User;
import ca.gatin.model.todo.TodoList;

public interface TodoListPersistenceService {

	/**
	 * Gets all
	 * 
	 * @return List<TodoList>
	 */
	List<TodoList> getAll();
	
	/**
	 * Get all by User
	 * 
	 * @param user
	 * @return
	 */
	List<TodoList> getByCreator(User user);
	
}
