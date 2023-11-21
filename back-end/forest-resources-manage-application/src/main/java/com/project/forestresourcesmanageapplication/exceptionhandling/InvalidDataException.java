package com.project.forestresourcesmanageapplication.exceptionhandling;

// Lỗi dữ liệu không hợp lệ
public class InvalidDataException extends RuntimeException{
    public InvalidDataException(String message){
        super(message);
    }
}
