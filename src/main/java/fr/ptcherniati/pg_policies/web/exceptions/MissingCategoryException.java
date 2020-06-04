package fr.ptcherniati.pg_policies.web.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MissingCategoryException extends RuntimeException {

    public MissingCategoryException(String s) {
        super(s);
    }
}
