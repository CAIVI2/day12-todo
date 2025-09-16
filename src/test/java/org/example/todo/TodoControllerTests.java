package org.example.todo;

import org.example.todo.entity.Todo;
import org.example.todo.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.not;

@SpringBootTest
@AutoConfigureMockMvc
public class TodoControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TodoRepository todoRepository;

    @BeforeEach
    public void setup() {
        todoRepository.deleteAll();
    }

    @Test
    void should_response_empty_list_when_index_with_no_any_todo() throws Exception {
        MockHttpServletRequestBuilder request = get("/todos")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void should_response_one_todo_when_index_with_one_todo() throws Exception {
        Todo todo = new Todo(null, "Buy milk", false);
        todoRepository.save(todo);

        MockHttpServletRequestBuilder request = get("/todos")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].text").value(todo.getText()))
                .andExpect(jsonPath("$[0].done").value(todo.getDone()));
    }

    @Test
    void should_response_201_when_create_todo() throws Exception {
        String newTodoJson = """
                {
                    "text": "Buy milk",
                    "done": false
                }
                """;

        MockHttpServletRequestBuilder request = post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newTodoJson);

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.text").value("Buy milk"))
                .andExpect(jsonPath("$.done").value(false));
    }

    @Test
    void should_response_422_when_create_todo_with_missing_field_text() throws Exception {
        String newTodoJson = """
              {
                    "text": "",
                    "done": false
                }
                """;

        MockHttpServletRequestBuilder request = post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newTodoJson);

        mockMvc.perform(request)
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void should_response_422_when_create_todo_with_required_field_text() throws Exception {
        String newTodoJson = """
              {
                    "done": false
                }
                """;

        MockHttpServletRequestBuilder request = post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newTodoJson);

        mockMvc.perform(request)
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void should_response_201_and_ignore_id_when_create_todo_with_id() throws Exception {
        String newTodoJson = """
                {
                    "id": "client-sent",
                    "text": "Buy milk",
                    "done": false
                }
                """;

        MockHttpServletRequestBuilder request = post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newTodoJson);

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(not("client-sent")))
                .andExpect(jsonPath("$.text").value("Buy milk"))
                .andExpect(jsonPath("$.done").value(false));
    }

    @Test
    void should_response_200_when_update_todo() throws Exception {
        Todo todo = new Todo("123", "Buy milk", false);
        todo = todoRepository.save(todo);

        String updateTodoJson = """
                {
                    "text": "Buy snacks",
                    "done": true
                }
                """;

        MockHttpServletRequestBuilder request = put("/todos/" + todo.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateTodoJson);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(todo.getId()))
                .andExpect(jsonPath("$.text").value("Buy snacks"))
                .andExpect(jsonPath("$.done").value(true));
    }

    @Test
    void should_response_200_and_ignore_surplus_id_when_update_todo() throws Exception {
        Todo todo = new Todo("123", "Buy milk", false);
        todo = todoRepository.save(todo);

        String updateTodoJson = """
                {
                    "id": "456",
                    "text": "Buy snacks",
                    "done": true
                }
                """;

        MockHttpServletRequestBuilder request = put("/todos/" + todo.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateTodoJson);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(todo.getId()))
                .andExpect(jsonPath("$.id").value(not("456")))
                .andExpect(jsonPath("$.text").value("Buy snacks"))
                .andExpect(jsonPath("$.done").value(true));
    }

    @Test
    void should_response_404_when_update_todo_with_not_found_id() throws Exception {
        String updateTodoJson = """
                {
                    "text": "Buy snacks",
                    "done": true
                }
                """;

        MockHttpServletRequestBuilder request = put("/todos/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateTodoJson);

        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    void should_response_422_when_update_todo_with_incomplete_payload() throws Exception {
        Todo todo = new Todo("123", "Buy milk", false);
        todo = todoRepository.save(todo);

        String updateTodoJson = """
                {
                }
                """;

        MockHttpServletRequestBuilder request = put("/todos/" + todo.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateTodoJson);

        mockMvc.perform(request)
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void should_response_204_when_delete_todo() throws Exception {
        Todo todo = new Todo("123", "Buy milk", false);
        todo = todoRepository.save(todo);

        MockHttpServletRequestBuilder request = delete("/todos/" + todo.getId())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isNoContent());
    }

    @Test
    void should_response_204_when_delete_todo_with_not_found_id() throws Exception {
        MockHttpServletRequestBuilder request = delete("/todos/999")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    void should_allow_cors() throws Exception {
        MockHttpServletRequestBuilder request = options("/todos")
                .header("Access-Control-Request-Method", "GET", "POST", "PUT", "DELETE")
                .header("Origin", "http://localhost:3000");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist())
                .andExpect(result -> {
                    String allowOrigin = result.getResponse().getHeader("Access-Control-Allow-Origin");
                    if (allowOrigin == null || !allowOrigin.equals("*")) {
                        throw new AssertionError("Access-Control-Allow-Origin header is missing or incorrect");
                    }
                });
    }
}