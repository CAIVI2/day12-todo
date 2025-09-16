package org.example.todo.controller;

import org.example.todo.entity.Todo;
import org.example.todo.exception.InvalidTextTodoException;
import org.example.todo.service.TodoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    List<Todo> index() {
        return todoService.index();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Todo create(@RequestBody Todo todo) {
        todo.setId(null);
        if (todo.getText() == null || todo.getText().isEmpty()) {
            throw new InvalidTextTodoException("Text is required");
        }
        return todoService.create(todo);
    }

    @PutMapping("/{id}")
    public Todo update(@PathVariable String id, @RequestBody Todo todo) {
        return todoService.update(id, todo);
    }
}