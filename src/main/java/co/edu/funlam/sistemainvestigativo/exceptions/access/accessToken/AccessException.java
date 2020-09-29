package co.edu.funlam.sistemainvestigativo.exceptions.access.accessToken;

import co.edu.funlam.sistemainvestigativo.exceptions.access.ApplicationException;

public class AccessException extends ApplicationException {
    public AccessException(String message, boolean supressStackTrace) {
        super(message, supressStackTrace);
    }
}
