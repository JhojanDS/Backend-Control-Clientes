/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package jhojands.controlclientes.auth.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.io.IOException;
import java.util.Collection;

/**
 *
 * @author JHOJAN L
 * interface para tener una estructura de los metodos a usar en el manejo de jwt 
 * hacemos una interface como buena practica
 */
public interface IJwtService {
    //Crear el token
    public String create(Authentication auth) throws IOException;
    //validar token
    public boolean validate(String token);
    //Obtener claims
    public Claims getClaims(String token);
    //Obtener usernamee  
    public String getUsername(String token);
    //Obtener authorities o roles
    public Collection<? extends GrantedAuthority> getRoles (String token)  throws IOException;
    //Metodo para quitar el Bearer puesto en el token
    public String resolve (String token);
}
