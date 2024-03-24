package dev.projectfitness.ipfitness.exceptions;

import org.springframework.http.HttpStatus;

// Status Code 403 - Forbidden
public class ForbiddenException extends HttpException {
    public ForbiddenException() {
        super(HttpStatus.FORBIDDEN, null);
    }

    public ForbiddenException(Object data) {
        super(HttpStatus.FORBIDDEN, data);
    }

}