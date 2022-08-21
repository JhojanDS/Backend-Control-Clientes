/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package jhojands.controlclientes.models.dao;

import java.util.List;
import jhojands.controlclientes.models.Role;
import jhojands.controlclientes.models.User;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author JHOJAN L
 */
public interface IUsuarioDao extends CrudRepository<User,Long>{
    public User findByEmail(String email);
    public List<User> findByIdRole(Role role);
    public User findByEmailAndPassword(String email, String password);
    
}
