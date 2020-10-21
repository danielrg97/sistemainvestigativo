package co.edu.funlam.sistemainvestigativo.service.access;

import co.edu.funlam.sistemainvestigativo.exceptions.access.accessToken.WrongCredentialsException;
import co.edu.funlam.sistemainvestigativo.model.dao.access.AccessTokenDao;
import co.edu.funlam.sistemainvestigativo.model.dao.access.UserDao;
import co.edu.funlam.sistemainvestigativo.model.dto.access.CredentialsDto;
import co.edu.funlam.sistemainvestigativo.model.entities.access.AccessToken;
import co.edu.funlam.sistemainvestigativo.model.entities.access.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static co.edu.funlam.sistemainvestigativo.utils.Constants.SIGNING_KEY;

@Service
public class AccessServiceImpl implements AccessService{

    private AccessTokenDao accessTokenDao;
    private UserDao userDao;
    private PasswordEncoder passwordEncoder;

    /**
     * Constructor para inyeccion de dependencias
     * @param accessTokenDao
     */
    @Autowired
    public AccessServiceImpl(AccessTokenDao accessTokenDao, UserDao userDao, PasswordEncoder passwordEncoder){
        this.accessTokenDao = accessTokenDao;
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Metodo para autenticar
     * @param credentials
     * @return
     * @throws WrongCredentialsException
     */
    @Override
    public AccessToken authenticate(CredentialsDto credentials) throws WrongCredentialsException {
        //Busca usuario por nombre de usuario
        User user = userDao.findUserByUserName(credentials.getUserName())
                .orElseThrow(() ->
                        new WrongCredentialsException("Nombre de usuario incorrecto", true)
                );
        //Comprueba contraseña
        if(!passwordEncoder.matches(credentials.getPassword(), user.getPassword()))
            throw new WrongCredentialsException("Contraseña incorrecta", true);
        //Retorna token si es correcto
        return generateAccessToken(user);
    }

    /**
     * Metodo que se encarga de guardar entidad AccessToken y genera token
     * Adicionalmente elimina tokens previos para mantener un unico token por usuario autenticado
     * @param user
     * @return
     */
    private AccessToken generateAccessToken(User user){
        List<AccessToken> accessTokensByUser = accessTokenDao.findAccessTokenByUser(user);
        //Borro todos los tokens anteriores para tener solo un token por usuario
        if(accessTokensByUser.size()>0)
            accessTokensByUser.stream().forEach(accessToken ->
                    accessTokenDao.delete(accessToken)
            );
        AccessToken newToken = new AccessToken();
        newToken.setToken(getJWTToken(user));
        newToken.setCreationDate(new Date(System.currentTimeMillis()));
        newToken.setUser(user);
        accessTokenDao.save(newToken);
        return newToken;
    }

    /**
     * Metodo para generar token de acceso con la signature y claims correspondientes
     * @param user
     * @return
     */
    private String getJWTToken(User user){
        List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(
                user.getRole().getName()
        );

        return Jwts
                .builder()
                .setSubject(user.getUserName())
                .claim(
                        "authorities", authorities.stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList())
                )
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 2400000))
                .signWith(SignatureAlgorithm.HS512, SIGNING_KEY)
                .compact();

    }
}
