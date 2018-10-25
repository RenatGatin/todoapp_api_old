package ca.gatin.dao.service;

import java.util.List;

import ca.gatin.model.todo.TodoList;

public interface TodoListPersistenceService {

	/**
	 * Gets all
	 * 
	 * @return List<TodoList>
	 */
	List<TodoList> getAll();
	
}
