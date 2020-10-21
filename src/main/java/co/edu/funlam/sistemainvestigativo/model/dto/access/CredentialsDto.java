package co.edu.funlam.sistemainvestigativo.model.dto.access;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de credenciales de acceso
 */
@Getter
@Setter
@NoArgsConstructor
public class CredentialsDto {
    private String userName;
    private String password;
}
