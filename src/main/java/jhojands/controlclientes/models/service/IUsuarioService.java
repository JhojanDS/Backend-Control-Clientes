/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package jhojands.controlclientes.models.service;

import java.util.List;
import jhojands.controlclientes.models.Role;
import jhojands.controlclientes.models.User;

/**
 *
 * @author JHOJAN L
 */
public interface IUsuarioService {

    public User findById(long codigo);

    public List<User> findByIdRol(Role role);
    
    public User findByEmailAndPassword(String email, String password);

    public User findByEmail(String email);

    public User save(User user);
    
    public void delete(long id);
}
