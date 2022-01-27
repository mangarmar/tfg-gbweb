package com.gbweb.service;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gbweb.entity.Usuario;
import com.gbweb.enums.ROL;
import com.gbweb.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

	@Autowired 
	UserRepository userRepo;
	
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = userRepo.findByUsername(username);  
       if (usuario == null) {
           throw new UsernameNotFoundException("No se puede encontrar el usuario");
    }       
       return usuario;
	}
	
    public Usuario findByUsername(String username) {
    	return userRepo.findByUsername(username);
    }

	public void creaUsuario(@Valid Usuario usuario) {
		usuario.setRol(ROL.CLIENTE);
		userRepo.save(usuario);
		
	}





}
