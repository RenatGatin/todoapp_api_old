package ca.gatin.api.service;

import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.gatin.api.response.ResponseStatus;
import ca.gatin.api.response.ServiceResponse;
import ca.gatin.dao.service.TodoItemPersistenceService;
import ca.gatin.dao.service.TodoListPersistenceService;
import ca.gatin.dao.service.UserPersistenceService;
import ca.gatin.model.security.User;
import ca.gatin.model.todo.TodoItem;
import ca.gatin.model.todo.TodoList;

/**
 * Service responsible for all Todo items, lists, etc..
 *
 * @author RGatin
 * @since Oct 24, 2018
 */
@Component
public class TodoService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private UserPersistenceService userPersistenceService;
	
	@Autowired
	private TodoItemPersistenceService todoItemPersistenceService;
	
	@Autowired
	private TodoListPersistenceService todoListPersistenceService;
	
	/**
	 * Get list of all todo items from all lists
	 * 
	 * @param role
	 * @return
	 */
	public ServiceResponse<?> testTodoItemAll() {
		ServiceResponse<List<TodoItem>> serviceResponse = new ServiceResponse<>(ResponseStatus.SYSTEM_UNAVAILABLE);
		
		List<TodoItem> todoItems = todoItemPersistenceService.getAll();
		serviceResponse.setStatus(ResponseStatus.SUCCESS);
		serviceResponse.setEntity(todoItems);

		return serviceResponse;
	}
	
	public ServiceResponse<?> testTodoListAll() {
		ServiceResponse<List<TodoList>> serviceResponse = new ServiceResponse<>(ResponseStatus.SYSTEM_UNAVAILABLE);
		
		List<TodoList> todoItems = todoListPersistenceService.getAll();
		serviceResponse.setStatus(ResponseStatus.SUCCESS);
		serviceResponse.setEntity(todoItems);
		
		return serviceResponse;
	}
	
	public ServiceResponse<?> getTodoListAll(User user) {
		ServiceResponse<List<TodoList>> serviceResponse = new ServiceResponse<>(ResponseStatus.SYSTEM_UNAVAILABLE);
		
		List<TodoList> todoItems = todoListPersistenceService.getByCreator(user);
		serviceResponse.setStatus(ResponseStatus.SUCCESS);
		serviceResponse.setEntity(todoItems);
			
		return serviceResponse;
	}

}
