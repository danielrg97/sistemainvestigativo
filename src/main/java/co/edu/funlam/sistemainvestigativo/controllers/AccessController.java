package co.edu.funlam.sistemainvestigativo.controllers;

import co.edu.funlam.sistemainvestigativo.exceptions.access.accessToken.WrongCredentialsException;
import co.edu.funlam.sistemainvestigativo.model.dto.access.CredentialsDto;
import co.edu.funlam.sistemainvestigativo.model.entities.access.AccessToken;
import co.edu.funlam.sistemainvestigativo.service.access.AccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccessController {
    private AccessService accessService;

    @Autowired
    public AccessController(AccessService accessService){
        this.accessService = accessService;
    }

    @PostMapping(path = "/login")//, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public AccessToken login(@RequestBody CredentialsDto credetials) throws WrongCredentialsException {
        return accessService.authenticate(credetials);
    }
}
