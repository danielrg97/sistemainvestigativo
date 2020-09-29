package co.edu.funlam.sistemainvestigativo.model.dao.access;

import co.edu.funlam.sistemainvestigativo.model.entities.access.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccessTokenDao extends JpaRepository<AccessToken, Long> {
    Optional<AccessToken> findAccessTokenByToken(String token);
}
