/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Controller.java to edit this template
 */
package jhojands.controlclientes.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.Valid;

import jhojands.controlclientes.models.Role;
import jhojands.controlclientes.models.User;
import jhojands.controlclientes.models.service.IUsuarioService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static jhojands.controlclientes.SecurityConfig.passwordEncoder;

/**
 * @author JHOJAN L
 */
@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class UsuarioRestController {

    private final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private IUsuarioService usuarioService;

    @PostMapping("/usuario/crear")
    public ResponseEntity<?> create(@Valid @RequestBody User user, BindingResult result) {

        logger.info("usuario recibido : " + user.toString());
        Map<String, Object> response = new HashMap<>();

        if (result.hasErrors()) {

            List<String> errors = getErrors(result);

            response.put("mensaje", errors);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        } else {

            setPasswordEncrypt(user);
            User userNew = usuarioService.save(user);
            response.put("mensaje", "Se ha creado el usuario con exito");
            response.put("user", userNew);
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        }

    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<?> readByCodigo(@PathVariable long id) {
        logger.info("entrando a readByCodigo");
        User u = usuarioService.findById(id);

        if (u == null) {

            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "El usuario no existe");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        } else {

            return new ResponseEntity<>(u, HttpStatus.OK);

        }
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/clientes")
    public List<User> readByCliente() {
        return usuarioService.findByIdRol(new Role(2L));
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/administradores")
    public List<User> readByAdministrador() {
        return usuarioService.findByIdRol(new Role(1L));
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    @PutMapping("/usuario")
    public ResponseEntity<?> update(@Valid @RequestBody User user, BindingResult result) {
        Map<String, Object> response = new HashMap<>();

        if (result.hasErrors()) {

            List<String> errors = getErrors(result);
            response.put("mensaje", errors);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        } else {

            updatePassword(user);
            User userUpdate = usuarioService.save(user);
            response.put("mensaje", "Se ha actualizado el usuario con exito!");
            response.put("user", userUpdate);

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/usuario/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {

        User c = usuarioService.findById(id);
        if (c == null) {//si es null es porque no existe

            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "No existe un usuario con el id: " + id + " para eliminar ");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        } else {
            usuarioService.delete(id);

            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "El usuario con el id: " + id + " ha sido eliminado ");
            return new ResponseEntity<>(response, HttpStatus.OK);

        }
    }

    /*si no se valida el unique mediante las excepciones, se usan estos metodos
    private boolean updateEmail(User user) {
        User userDb = usuarioService.findById(user.getId());
        if (user.getEmail().equals(userDb.getEmail())) {//analizamos si el email es el mismo que se encuentra registrado en la db
            return true;//si es el mismo indica que es un email valido y se mantendra
        } else {//si no es el mismo pasamos a validar el email
            return validateEmail(user.getEmail());
        }
    }

    private boolean validateEmail(String email) {//valida que el email proporcionado no se encuentre ya registrado
        User u = usuarioService.findByEmail(email);
        if (u == null) {
            return true;
        }
        return false;
    }*/

    private void updatePassword(User user) {//indicara si se debe codificar la password
        User userDb = usuarioService.findById(user.getId());
        if (!user.getPassword().equals(userDb.getPassword())) {//Si son iguales no se encripta y se deja la que esta en la db
            setPasswordEncrypt(user);
        }

    }


    private void setPasswordEncrypt(User user) {
        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder().encode(user.getPassword()));  //codificamos la password
        }
    }

    public List<String> getErrors(BindingResult result) {//obtiene un listado de errores a partir de @Valid

        return result.getFieldErrors()
                .stream()
                .map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
                .collect(Collectors.toList());
    }

}
