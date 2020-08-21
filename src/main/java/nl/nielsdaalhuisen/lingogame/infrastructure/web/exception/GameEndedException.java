package nl.nielsdaalhuisen.lingogame.infrastructure.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class GameEndedException extends Exception {
    public GameEndedException(String message) {
        super(message);
    }
}
