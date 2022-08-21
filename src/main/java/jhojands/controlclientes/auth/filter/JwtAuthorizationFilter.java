/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jhojands.controlclientes.auth.filter;

import jhojands.controlclientes.auth.service.IJwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static jhojands.controlclientes.auth.service.JwtServiceImpl.HEADER_STRING;
import static jhojands.controlclientes.auth.service.JwtServiceImpl.TOKEN_PREFIX;

/**
 *
 * @author JHOJAN  clase para la autorizacion la cual se ejecutara por cada
 * request
 *
 */
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    //para tener la interface en esta clase con sus metodos
    private IJwtService jwtService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, IJwtService jwtService) {
        super(authenticationManager);
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(HEADER_STRING);//obtenemos el header llamado Authorization

        logger.info("Header en la autorizacion: "+header );
        if (!requiresAuthentication(header)) {//analizamos si requiere autenticacion
            
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = null;

        if (jwtService.validate(header)) {

            //passamos el usernmame y los roles
            authentication = new UsernamePasswordAuthenticationToken(jwtService.getUsername(header), null, jwtService.getRoles(header));
        }
        //establecemos la autenticacion SecurityContextHolder maneja el contexto de seguridad del sistema
        //entonces estableecemos al usuario dentro del contexto, dentro de la peticion
        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(request, response);
    }

    protected boolean requiresAuthentication(String header) {//analiza si necesita autenticacion
        if (header == null || !header.startsWith(TOKEN_PREFIX)) {//Si es nulo o es distinto de Bearer el cual establecimos en el token
            return false;
        }
        return true;
    }

   
     
    

}
