/*
 *Clase filtro de autenticacion
 *
 *
 */
package jhojands.controlclientes.auth.filter;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jhojands.controlclientes.auth.service.IJwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static jhojands.controlclientes.auth.service.JwtServiceImpl.HEADER_STRING;
import static jhojands.controlclientes.auth.service.JwtServiceImpl.TOKEN_PREFIX;

/**
 * @author JHOJAN L
 */
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private IJwtService jwtService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, IJwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    // metodo que se encarga de realizar la autenticacion(login)
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        String username = request.getParameter("email");
        // esto es lo mismo que tener:
        // request.getParameter("username"); de esta manera obtenemos los datos pero si son mandados por un form data
        String password = obtainPassword(request);

        if (username != null && password != null) {
            logger.info("username recibido desde request parameter (form data) : " + username);
            logger.info("password recibido desde request parameter (form data) : " + password);

        } else {//si son nulos es decir que se han mandado por formato json
            logger.info("obteniendo datos en formato json ");

            jhojands.controlclientes.models.User u = null;
            try {
                //convertimos los datos en json a un objeto tipo usuario(modelo)
                u = new ObjectMapper().readValue(request.getInputStream(), jhojands.controlclientes.models.User.class);

                username = u.getEmail();
                password = u.getPassword();

                logger.info("username recibido desde request InputStream (raw) : " + username);
                logger.info("password recibido desde request InputStream (raw) : " + password);

            } catch (JsonParseException e) {
                e.printStackTrace();
            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        username = username.trim();
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);

        return authenticationManager.authenticate(authToken);

    }

    //metodo para manejar la autenticacion de forma correcta. Authentication authResult es el mismo authToken pero ya autenticado
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        String token = jwtService.create(authResult);//utilizamos el servicio creado, para obtener el token. para realizarlo de una forma mas limpia

        //importante pasar el token al response. ademas se debe llamar Authorization y "Bearer "+token(el prefijo bearer es un estandar)
        response.addHeader(HEADER_STRING, TOKEN_PREFIX + token);

        Map<String, Object> body = new HashMap<String, Object>();//para pasar otros atributos
        body.put("token", token);
        body.put("user", (User) authResult.getPrincipal());//pasasamos el usuario tipo userDetails
        //body.put("mensaje", String.format("Hola %s, has iniciado sesión con éxito!", ((User) authResult.getPrincipal()).getUsername()));//%s se remplaza por el arg
        body.put("mensaje", "Has iniciado sesión con éxito!");
        //agg el body al response, para ello lo convertimos a json con la clase ObjectMapper()
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(200);//indicamos el estado http
        response.setContentType("application/json"); //indicamos el tipo de contenido
        //super.successfulAuthentication(request, response, chain, authResult);
    }

    //metodo para manejar la autenticacion de forma incorrecta.
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {

        Map<String, Object> body = new HashMap<String, Object>();

        //se recomienda no especificar el error ya sea en el username o password ya que es informacion sensible.
        body.put("mensaje", "Error de autenticación: username o password incorrectos");
        body.put("error", failed.getMessage());

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(401);//401 , 403  cualquiera de los dos | se recomienda 401 en el login 403 despues cuando no tenga acceso de acuerdo a los roles
        response.setContentType("application/json");
    }

}
