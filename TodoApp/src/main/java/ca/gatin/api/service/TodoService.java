package ca.gatin.api.service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import ca.gatin.api.exception.PermissionDeniedException;
import ca.gatin.api.exception.ValidationException;
import ca.gatin.api.response.FieldErrorBean;
import ca.gatin.api.response.ResponseStatus;
import ca.gatin.api.response.ServiceResponse;
import ca.gatin.dao.service.TodoItemPersistenceService;
import ca.gatin.dao.service.TodoListPersistenceService;
import ca.gatin.dao.service.UserPersistenceService;
import ca.gatin.model.request.SimpleStringBean;
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
	
	public ServiceResponse<?> renameList(User user, Long listId, String newName) throws NoSuchMethodException, SecurityException, MethodArgumentNotValidException {
		ServiceResponse<List<TodoList>> serviceResponse = new ServiceResponse<>(ResponseStatus.SYSTEM_UNAVAILABLE);
		
		newName = newName.trim();
		if (StringUtils.isEmpty(newName)) {
			FieldErrorBean fieldError = new FieldErrorBean("getStringVar", "is empty");
			throw new ValidationException(fieldError);
		}
		
		TodoList listItem = todoListPersistenceService.getById(listId);
		if (listItem == null) {
			serviceResponse.setStatus(ResponseStatus.TODOLISTITEM_NOT_FOUND);
			return serviceResponse;
		}
		
		if (listItem.getCreator().getId() == user.getId()) {
			listItem.setName(newName);
			listItem.setDateLastModified(new Date());
			todoListPersistenceService.save(listItem);
			serviceResponse.setStatus(ResponseStatus.SUCCESS);
			
		} else {
			throw new PermissionDeniedException();
		}
			
		return serviceResponse;
	}

	public ServiceResponse<?> deleteList(User user, Long listId) {
		ServiceResponse<List<TodoList>> serviceResponse = new ServiceResponse<>(ResponseStatus.SYSTEM_UNAVAILABLE);
		
		TodoList listItem = todoListPersistenceService.getById(listId);
		if (listItem == null) {
			serviceResponse.setStatus(ResponseStatus.TODOLISTITEM_NOT_FOUND);
			return serviceResponse;
		}
		
		if (listItem.getCreator().getId() == user.getId()) {
			todoListPersistenceService.delele(listId);
			serviceResponse.setStatus(ResponseStatus.SUCCESS);
			
		} else {
			throw new PermissionDeniedException();
		}
			
		return serviceResponse;
	}

}
