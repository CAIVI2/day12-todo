package org.example.todo.service;

import org.example.todo.entity.Todo;
import org.example.todo.exception.InvalidTextTodoException;
import org.example.todo.repository.TodoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TodoService {
    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<Todo> index() {
        return todoRepository.findAll();
    }

    public Todo create(Todo todo) {
        return todoRepository.save(todo);
    }

    public Todo update(String id, Todo todo) {
        todoRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo id: " + id + " not found"));
        todo.setId(id);
        if (todo.getText() == null || todo.getText().isEmpty()) {
            throw new InvalidTextTodoException("Text is required");
        }
        return todoRepository.save(todo);
    }

    public void delete(String id) {
        todoRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo id: " + id + " not found"));
        todoRepository.deleteById(id);
    }
}