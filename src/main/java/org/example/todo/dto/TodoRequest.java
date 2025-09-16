package org.example.todo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TodoRequest {
    private String id;
    @NotBlank(message = "Text cannot be null")
    private String text;
    @NotNull(message = "Done cannot be null")
    private Boolean done;
}