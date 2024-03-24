package dev.projectfitness.ipfitness.exceptions;

import org.springframework.http.HttpStatus;

// Status Code 409 - Conflict
public class ConflictException extends HttpException {
    public ConflictException() {
        super(HttpStatus.CONFLICT, null);
    }

    public ConflictException(Object data) {
        super(HttpStatus.CONFLICT, data);
    }
}

