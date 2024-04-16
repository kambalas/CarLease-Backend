package com.example.sick.api.model.exception;

public class ApplicationNotFoundException extends ApplicationException{
    public ApplicationNotFoundException(long id) {
        super("Cannot find a task with id: " + id);
    }
}
