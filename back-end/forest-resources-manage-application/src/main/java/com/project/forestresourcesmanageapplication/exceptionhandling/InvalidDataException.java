package com.project.forestresourcesmanageapplication.exceptionhandling;


public class InvalidDataException extends RuntimeException{
    public InvalidDataException(String message){
        super(message);
    }
}
