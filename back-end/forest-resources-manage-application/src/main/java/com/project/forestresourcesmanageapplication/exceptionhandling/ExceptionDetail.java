package com.project.forestresourcesmanageapplication.exceptionhandling;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

// Chi tiết ngoại lệ , trả về dối tượng này cho client khi có ngoại lệ xảy ra
@Data
@AllArgsConstructor
public class ExceptionDetail {
    private LocalDateTime timestamp;
	private String messages;
    private String description;
}
