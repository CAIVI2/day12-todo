package org.example.todo.dto.mapper;

import org.example.todo.dto.TodoRequest;
import org.example.todo.dto.TodoResponse;
import org.example.todo.entity.Todo;
import org.springframework.beans.BeanUtils;

import java.util.List;

public class TodoMapper {

    public static Todo toEntity(TodoRequest todoRequest) {
        Todo todo = new Todo();
        BeanUtils.copyProperties(todoRequest, todo);
        return todo;
    }

    public static TodoResponse toResponse(Todo todo) {
        TodoResponse todoResponse = new TodoResponse();
        BeanUtils.copyProperties(todo, todoResponse);
        return todoResponse;
    }

    public static List<TodoResponse> toResponses(List<Todo> todos) {
        return todos.stream().map(TodoMapper::toResponse).toList();
    }
}