package com.project.forestresourcesmanageapplication.exceptionhandling;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExceptionDetail {
    private LocalDateTime timestamp;
	private String messages;
    private String description;
}
