package com.project.forestresourcesmanageapplication.exceptionhandling;

// Lỗi không tìm thấy dữ liệu
public class DataNotFoundException extends RuntimeException{
    public DataNotFoundException(String message){
        super(message);
    }
}
