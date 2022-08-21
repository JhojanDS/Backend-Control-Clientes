/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jhojands.controlclientes.auth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jhojands.controlclientes.auth.SimpleGrantedAuthorityMixin;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author JHOJAN L
 */
//@Component//marcamos la clase como un obj spring
@Service//service extiende de component
public class JwtServiceImpl implements IJwtService {

    public static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);////ponemos como costante la secret key
    public static final long EXPIRATION_DATE = 3600000L;//ponemos como costante la expiracion
    public static final String TOKEN_PREFIX = "Bearer ";//el prefijo del token  
    public static final String HEADER_STRING = "Authorization";//ponemos como costante  el header llamado Authorization

    @Override
    public String create(Authentication auth) throws IOException {
        //obtenemos el username
        String username = ((User) auth.getPrincipal()).getUsername();//otra manera es authResult.getName()

        Collection<? extends GrantedAuthority> roles = auth.getAuthorities();//obtenemos los roles

        Claims claims = Jwts.claims();//es necesario para anexar otros datos al token[en este caso el rol no es recomendable pasar password o info sensible ya que esta parte del token no es segura  y se puede desifrar con un script]
        claims.put("authorities", new ObjectMapper().writeValueAsString(roles));//convertimos a json

        String token = Jwts.builder()
                .setClaims(claims)//ponemos los roles mediante claims
                .setSubject(username)//setSubject para estblecer nombre del usuario
                .signWith(SECRET_KEY)//establecer secret key
                .setIssuedAt(new Date())//fecha de creacion del token
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_DATE))//establecemos la expiracion 3600000 corresponde a una hr en ms
                .compact();

        return token;
    }

    @Override
    public boolean validate(String token) {

        try {//si esta todo bien se asignan los tokens y se valida el mismo
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public Claims getClaims(String token) {//claims son los datos del token

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(resolve(token)).getBody();//usamos resolve

        return claims;
    }

    @Override
    public String getUsername(String token) {//obtenemos el username a partir de los claims
        return getClaims(token).getSubject();
    }

    @Override
    public Collection<? extends GrantedAuthority> getRoles(String token) throws IOException {

        Object roles = getClaims(token).get("authorities");//obtenemos los roles mediante el token de acuredo a como se pusieron los claims en la autenticacion

        Collection<? extends GrantedAuthority> authorities
                = Arrays.asList(new ObjectMapper()
                        .addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityMixin.class)//agregamos el mixin
                        .readValue(roles.toString().getBytes(), SimpleGrantedAuthority[].class)
                );
        return authorities;
    }

    @Override
    public String resolve(String token) {
        if (token != null && token.startsWith(TOKEN_PREFIX)) {
            return token.replace(TOKEN_PREFIX, "");//remplaza el bearer del token para poder leerlo
        } else {
            return null;
        }
    }

}
