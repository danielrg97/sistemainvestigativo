package co.edu.funlam.sistemainvestigativo.exceptions.access.accessToken;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class TokenNotFoundException extends AccessException{
    public TokenNotFoundException(String message, boolean supressStackTrace) {
        super(message, supressStackTrace);
    }
}
