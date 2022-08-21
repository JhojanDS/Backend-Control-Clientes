/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jhojands.controlclientes.models.service;

import jhojands.controlclientes.models.Role;
import jhojands.controlclientes.models.dao.IRolDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author JHOJAN L
 */
@Service
public class RolServiceImpl implements IRolService{

    @Autowired
    IRolDao rolDao;
    
    @Override
    public Role findById(long id) {
        return rolDao.findById(id).orElse(null);
    }
    
}
