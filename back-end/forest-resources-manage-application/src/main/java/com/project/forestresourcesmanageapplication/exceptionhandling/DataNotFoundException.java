package com.project.forestresourcesmanageapplication.exceptionhandling;

public class DataNotFoundException extends RuntimeException{
    public DataNotFoundException(String message){
        super(message);
    }
}
