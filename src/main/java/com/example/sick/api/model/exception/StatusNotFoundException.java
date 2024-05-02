package com.example.sick.api.model.exception;

public class StatusNotFoundException extends StatusException{
    public StatusNotFoundException(long id) {
        super("Cannot find a task with id: " + id);
    }
}
