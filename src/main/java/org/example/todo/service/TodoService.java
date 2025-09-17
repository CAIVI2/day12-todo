package org.example.todo.service;

import org.example.todo.dto.TodoRequest;
import org.example.todo.dto.mapper.TodoMapper;
import org.example.todo.entity.Todo;
import org.example.todo.repository.TodoRepository;
import org.springframework.ai.tool.annotation.Tool;
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

    @Tool(name = "findAllTodo", description = "Find all todo")
    public List<Todo> index() {
        return todoRepository.findAll();
    }

    @Tool(name = "createTodo", description = "Create a new todo")
    public Todo create(TodoRequest todoRequest) {
        todoRequest.setId(null);
        return todoRepository.save(TodoMapper.toEntity(todoRequest));
    }

    @Tool(name = "updateTodo", description = "Update a todo")
    public Todo update(String id, TodoRequest todoRequest) {
        todoRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo id: " + id + " not found"));
        todoRequest.setId(id);
        return todoRepository.save(TodoMapper.toEntity(todoRequest));
    }

    @Tool(name = "deleteTodo", description = "Delete a todo")
    public void delete(String id) {
        todoRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo id: " + id + " not found"));
        todoRepository.deleteById(id);
    }
}