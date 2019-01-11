package ca.gatin.dao.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.gatin.dao.repository.TodoListRepository;
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

	
}