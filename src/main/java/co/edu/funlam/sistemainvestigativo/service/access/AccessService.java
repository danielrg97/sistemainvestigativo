package co.edu.funlam.sistemainvestigativo.service.access;

import co.edu.funlam.sistemainvestigativo.exceptions.access.accessToken.WrongCredentialsException;
import co.edu.funlam.sistemainvestigativo.model.dto.access.CredentialsDto;
import co.edu.funlam.sistemainvestigativo.model.entities.access.AccessToken;

public interface AccessService {
    AccessToken authenticate(CredentialsDto credentials) throws WrongCredentialsException;
}
