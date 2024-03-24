package dev.projectfitness.ipfitness.exceptions;

import org.springframework.http.HttpStatus;

// Status Code 400 - Bad Request
public class BadRequestException extends HttpException{

    public BadRequestException()
    {
        super(HttpStatus.BAD_REQUEST, null);
    }

    public BadRequestException(Object data)
    {
        super(HttpStatus.BAD_REQUEST, data);
    }
}
