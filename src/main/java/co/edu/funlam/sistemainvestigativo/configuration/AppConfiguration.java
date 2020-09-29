package co.edu.funlam.sistemainvestigativo.configuration;

import co.edu.funlam.sistemainvestigativo.filters.JWTAuthFilter;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class AppConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors().and()
                .addFilterAfter(new JWTAuthFilter(), UsernamePasswordAuthenticationFilter.class)
                //.addFilterBefore(new Object()/*TODO: Añadir filtro de cabeceras*/, ChannelProcessingFilter.class)
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .antMatchers(HttpMethod.POST, "/register").permitAll()
                //permitir acceso a la consola h2 evitando problemas de CORS
                .antMatchers("/h2-console/**").permitAll()
                .anyRequest().authenticated();
    }
    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }
    /**
     *Agrego al IoT el encoder de contraseñas para encriptar y desencriptar las contraseñas
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(6);
    }

    /**
     * Reflection para buscar endpoints anotados con AllowedRole y poder asegurar el alcance a ciertos endpoints
     */
    @Bean
    public Reflections reflections(){
        return new Reflections(
                new ConfigurationBuilder().setUrls(
                        ClasspathHelper.forPackage("co.edu.funlam.sistemainvestigativo.controller")
                ).setScanners(new MethodAnnotationsScanner())
        );
    }

    /**
     * configuracion para transferencia de recursos de origen cruzado (CORS) entre Front-End y Back-End
     */
    @SuppressWarnings("deprecation")
    @Bean
    public WebMvcConfigurer corsConfigurer(){
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000")
                        .allowedMethods("GET", "POST", "PUT", "DELETE");
            }
        };
    }
}
