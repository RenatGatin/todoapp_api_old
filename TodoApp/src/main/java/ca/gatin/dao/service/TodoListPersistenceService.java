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

	/**
	 * Get one by ID
	 * 
	 * @param id
	 * @return
	 */
	TodoList getById(Long id);
	
	/**
	 * Get one by name
	 * 
	 * @param name
	 * @return
	 */
	TodoList getByName(String name);

	TodoList save(TodoList listItem);

	void delele(Long listId);
	
}
