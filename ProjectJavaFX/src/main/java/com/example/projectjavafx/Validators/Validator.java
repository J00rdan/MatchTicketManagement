package com.example.projectjavafx.Validators;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}