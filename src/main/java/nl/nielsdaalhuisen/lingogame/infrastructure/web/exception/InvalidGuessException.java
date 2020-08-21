package nl.nielsdaalhuisen.lingogame.infrastructure.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class InvalidGuessException extends Exception{
    public InvalidGuessException (String message) {
        super(message);
    }
}
