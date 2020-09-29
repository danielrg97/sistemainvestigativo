package co.edu.funlam.sistemainvestigativo.filters;

import co.edu.funlam.sistemainvestigativo.annotations.AllowedRole;
import co.edu.funlam.sistemainvestigativo.exceptions.access.accessToken.TokenNotFoundException;
import co.edu.funlam.sistemainvestigativo.model.dao.access.AccessTokenDao;
import io.jsonwebtoken.*;
import lombok.SneakyThrows;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

import static co.edu.funlam.sistemainvestigativo.utils.Constants.*;
import static co.edu.funlam.sistemainvestigativo.utils.Utils.getEndpoint;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {
    @Autowired
    private Reflections reflections;

    @Autowired
    private AccessTokenDao accessTokenDao;

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String token = null;
        try {
            //Por si el accessTokenDao tiene problemas de inyeccion al levantar el ambiente
            if (accessTokenDao == null) {
                ServletContext context = httpServletRequest.getSession().getServletContext();
                SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, context);
            }

            token = httpServletRequest.getHeader(HEADER);

            if (accessTokenDao.findAccessTokenByToken(token).isPresent()) {
                Claims claims = getTokenData(token);
                if (claims.get(AUTHORITIES_HEADER) != null) {
                    if (hasAccessToEndpoint(httpServletRequest, claims)) {
                        return;
                    } else {
                        //Al limpiar el contexto, hace que la peticion sea rechazada
                        SecurityContextHolder.clearContext();
                    }
                } else {
                    //Al limpiar el contexto, hace que la peticion sea rechazada
                    SecurityContextHolder.clearContext();
                }
            } else {
                //Al limpiar el contexto, hace que la peticion sea rechazada
                SecurityContextHolder.clearContext();
            }
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e){
            //Eliminacion de token si ya expiro
            if(e instanceof ExpiredJwtException && !token.equals(null) && accessTokenDao.findAccessTokenByToken(token).isPresent()){
                accessTokenDao.delete(
                        accessTokenDao.findAccessTokenByToken(token).orElseThrow(() -> new TokenNotFoundException("No hay token que eliminar", true))
                );
            }
            httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
        }
    }

    private Claims getTokenData(String token){
        return Jwts.parser()
                .setSigningKey(SIGNING_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
    /**
     * Metodo para validar que el usuario tenga permisos para acceder al endpoint
     *
     * @param httpRequest
     * @param claims
     * @return
     * @throws Exception
     */
    private boolean hasAccessToEndpoint(HttpServletRequest httpRequest, Claims claims) throws Exception {
        Map<String, Method> methodsAnnotated = getMethodsAnotatedAs(AllowedRole.class);
        if(null != methodsAnnotated.get(getEndpoint(httpRequest))){
            return methodsAnnotated.get(getEndpoint(httpRequest))
                    .getAnnotation(AllowedRole.class)
                    .role()
                    .equals(((List)claims.get("authorities")).stream().findFirst().orElse(""));
        }else{
            //el endpoint no esta anotado con AllowedRole
            return true;
        }
    }

    /**
     * Obtiene los metodos anotados con la anotacion que se le pase por parametros
     * @param type
     * @return
     * @throws Exception
     */
    private Map<String, Method> getMethodsAnotatedAs(Class<? extends Annotation> type) throws Exception {
        Set<Method> methodList = reflections.getMethodsAnnotatedWith(type);
        Map<String, Method> methodAndEndpoint = new HashMap<>();
        for(Method p : methodList) {
            methodAndEndpoint.put(getMapping(p), p);
        }
        return methodAndEndpoint;
    }

    /**
     * Metodo para obtener el mapping del endpoint
     * Se obtiene de las anotaciones de spring
     * @param method
     * @return
     * @throws Exception
     */
    private String getMapping(Method method) throws Exception {
        if(null != method.getAnnotation(RequestMapping.class)){
            return Arrays.stream(method.getAnnotation(RequestMapping.class).path()).findFirst().orElse("");
        }else if(null != method.getAnnotation(PostMapping.class)){
            return Arrays.stream(method.getAnnotation(PostMapping.class).path()).findFirst().orElse("");
        }else if(null != method.getAnnotation(GetMapping.class)){
            return Arrays.stream(method.getAnnotation(GetMapping.class).path()).findFirst().orElse("");
        }else if(null != method.getAnnotation(PutMapping.class)){
            return Arrays.stream(method.getAnnotation(PutMapping.class).path()).findFirst().orElse("");
        }else if(null != method.getAnnotation(DeleteMapping.class)){
            return Arrays.stream(method.getAnnotation(DeleteMapping.class).path()).findFirst().orElse("");
        }else{
            throw new Exception("Metodo no aceptado");
        }
    }
}
