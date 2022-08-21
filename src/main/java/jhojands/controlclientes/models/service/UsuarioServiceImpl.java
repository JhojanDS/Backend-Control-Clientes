/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jhojands.controlclientes.models.service;

import java.util.List;
import jhojands.controlclientes.models.Role;
import jhojands.controlclientes.models.dao.IUsuarioDao;
import jhojands.controlclientes.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author JHOJAN L
 */
@Service
public class UsuarioServiceImpl implements IUsuarioService {

    @Autowired
    private IUsuarioDao usuarioDao;

    @Override
    @Transactional
    public User save(User user) {
        return usuarioDao.save(user);
    }
    
    @Override
    @Transactional(readOnly = true)
    public User findById(long codigo) {
        return usuarioDao.findById(codigo).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public User findByEmailAndPassword(String email, String password) {
        return usuarioDao.findByEmailAndPassword(email, password);

    }

    @Override
    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return usuarioDao.findByEmail(email);

    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findByIdRol(Role role) {
         return usuarioDao.findByIdRole(role);
    }

    @Override
    @Transactional
    public void delete(long id) {
        usuarioDao.deleteById(id);
    }

    

}
