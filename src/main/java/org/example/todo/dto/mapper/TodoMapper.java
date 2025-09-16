package org.example.todo.dto.mapper;

import org.example.todo.dto.TodoRequest;
import org.example.todo.entity.Todo;
import org.springframework.beans.BeanUtils;

public class TodoMapper {

    public static Todo toEntity(TodoRequest todoRequest) {
        Todo todo = new Todo();
        BeanUtils.copyProperties(todoRequest, todo);
        return todo;
    }
}