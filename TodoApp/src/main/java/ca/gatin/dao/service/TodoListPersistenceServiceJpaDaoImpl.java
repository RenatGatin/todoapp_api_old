package ca.gatin.dao.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.gatin.dao.repository.TodoListRepository;
import ca.gatin.model.security.User;
import ca.gatin.model.todo.TodoList;

@Service
public class TodoListPersistenceServiceJpaDaoImpl implements TodoListPersistenceService {

	@Autowired
	private TodoListRepository todoListRepository;

	@Override
	public List<TodoList> getAll() {
		List<TodoList> list = todoListRepository.findAll();
		return list;
	}

	@Override
	public List<TodoList> getByCreator(User user) {
		List<TodoList> list = todoListRepository.findByCreator(user);
		return list;
	}

	@Override
	public TodoList getById(Long id) {
		TodoList list = todoListRepository.findOne(id);
		return list;
	}

	@Override
	public void save(TodoList listItem) {
		todoListRepository.save(listItem);
	}

	@Override
	public void delele(Long listId) {
		todoListRepository.delete(listId);
	}
}
