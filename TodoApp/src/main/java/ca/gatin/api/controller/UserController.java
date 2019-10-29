package ca.gatin.api.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ca.gatin.api.response.ResponseStatus;
import ca.gatin.api.response.ServiceResponse;
import ca.gatin.api.service.TodoService;
import ca.gatin.api.service.UserService;
import ca.gatin.model.request.CreateToDoItemBean;
import ca.gatin.model.request.CreateToDoListBean;
import ca.gatin.model.request.SimpleStringBean;
import ca.gatin.model.security.User;
import ca.gatin.model.todo.TodoItem;

/**
 * User secured API Controller
 *
 * @author RGatin
 * @since Apr 23, 2016
 */
@RestController
@RequestMapping(value= "/api/user")
public class UserController extends BaseController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	TodoService todoService;
	
	@RequestMapping(value = "/ping", produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceResponse<?> ping() {
		ServiceResponse<Object> serviceResponse = new ServiceResponse<>(ResponseStatus.SUCCESS);
		return serviceResponse;
	}
	
	@RequestMapping(value = "/selfDelete", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.DELETE)
	public ServiceResponse<?> selfDelete(Principal principal) {
		User user = getCurrentUser(principal);
		return userService.selfDelete(user);
	}
	
	@RequestMapping(value= "/todo/list/all", produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceResponse<?> getTodoListAll(Principal principal) {
		User user = getCurrentUser(principal);
		return todoService.getTodoListAll(user);
	}
	
	@RequestMapping(value= "/todo/list/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceResponse<?> getTodoListById(Principal principal, @PathVariable Long id) {
		User user = getCurrentUser(principal);
		return todoService.getTodoListById(user, id);
	}
	
	@RequestMapping(value= "/todo/list/rename/{listId}/", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ServiceResponse<?> getTodoListRename(Principal principal, @PathVariable Long listId, @Validated @RequestBody SimpleStringBean newNameBean) throws NoSuchMethodException, SecurityException, MethodArgumentNotValidException {
		User user = getCurrentUser(principal);
		return todoService.renameList(user, listId, newNameBean.getStringVar());
	}
	
	@RequestMapping(value= "/todo/list/hide-completed/{listId}/{doHide}/", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ServiceResponse<?> setTodoListHideCompleted(Principal principal, @PathVariable Long listId, @PathVariable Boolean doHide) throws NoSuchMethodException, SecurityException, MethodArgumentNotValidException {
		User user = getCurrentUser(principal);
		return todoService.setTodoListHideCompleted(user, listId, doHide);
	}
	
	@RequestMapping(value= "/todo/list/delete/{listId}/", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.DELETE)
	public ServiceResponse<?> getTodoListDelete(Principal principal, @PathVariable Long listId) {
		User user = getCurrentUser(principal);
		return todoService.deleteList(user, listId);
	}
	
	@RequestMapping(value= "/todo/list/create", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ServiceResponse<?> createTodoList(Principal principal, @Validated @RequestBody CreateToDoListBean todoListBean) {
		User user = getCurrentUser(principal);
		return todoService.createTodoList(user, todoListBean);
	}
	
	@RequestMapping(value= "/todo/item/create", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ServiceResponse<?> createTodoItem(Principal principal, @Validated @RequestBody CreateToDoItemBean todoItemBean) {
		User user = getCurrentUser(principal);
		return todoService.createTodoItem(user, todoItemBean);
	}
	
	@RequestMapping(value= "/todo/item/update", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ServiceResponse<?> updateTodoItem(Principal principal, @Validated @RequestBody TodoItem todoItemBean) {
		User user = getCurrentUser(principal);
		return todoService.updateTodoItem(user, todoItemBean);
	}
}