package com.project.forestresourcesmanageapplication.exceptionhandling;

// Lỗi dữ liệu đã tồn tại 
public class DataAlreadyExistsException extends RuntimeException{
    public DataAlreadyExistsException(String message){
        super(message);
    }    
}
