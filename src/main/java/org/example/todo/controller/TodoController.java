package org.example.todo.controller;

import org.example.todo.dto.TodoRequest;
import org.example.todo.dto.TodoResponse;
import org.example.todo.dto.mapper.TodoMapper;
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
    List<TodoResponse> index() {
        return TodoMapper.toResponses(todoService.index());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TodoResponse create(@RequestBody @Validated TodoRequest todoRequest) {
        return TodoMapper.toResponse(todoService.create(todoRequest));
    }

    @PutMapping("/{id}")
    public TodoResponse update(@PathVariable String id, @RequestBody @Validated TodoRequest todoRequest) {
        return TodoMapper.toResponse(todoService.update(id, todoRequest));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        todoService.delete(id);
    }
}