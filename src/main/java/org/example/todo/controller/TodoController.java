package org.example.todo.controller;

import org.example.todo.dto.TodoRequest;
import org.example.todo.entity.Todo;
import org.example.todo.service.TodoService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
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
    public Todo create(@RequestBody @Validated TodoRequest todoRequest) {
        return todoService.create(todoRequest);
    }

    @PutMapping("/{id}")
    public Todo update(@PathVariable String id, @RequestBody @Validated TodoRequest todoRequest) {
        return todoService.update(id, todoRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        todoService.delete(id);
    }
}