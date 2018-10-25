package ca.gatin.dao.service;

import java.util.List;

import ca.gatin.model.security.Authorities;
import ca.gatin.model.security.User;
import ca.gatin.model.todo.TodoItem;

public interface TodoItemPersistenceService {

	/**
	 * Gets all
	 * 
	 * @return List<TodoItem>
	 */
	List<TodoItem> getAll();
	
}
