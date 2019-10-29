package ca.gatin.dao.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.gatin.dao.repository.TodoItemRepository;
import ca.gatin.model.security.Authorities;
import ca.gatin.model.security.User;
import ca.gatin.model.todo.TodoItem;

@Service
public class TodoItemPersistenceServiceJpaDaoImpl implements TodoItemPersistenceService {

	@Autowired
	private TodoItemRepository todoItemRepository;

	@Override
	public List<TodoItem> getAll() {
		List<TodoItem> allUsers = todoItemRepository.findAll();
		return allUsers;
	}

	@Override
	public TodoItem save(TodoItem todoItem) {
		return todoItemRepository.save(todoItem);
	}

	
}
