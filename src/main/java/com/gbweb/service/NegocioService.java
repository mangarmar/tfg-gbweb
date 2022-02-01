package com.gbweb.service;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.gbweb.entity.Negocio;
import com.gbweb.entity.Usuario;
import com.gbweb.repository.NegocioRepository;
import com.gbweb.repository.UserRepository;

@Service
public class NegocioService {
	
	@Autowired 
	NegocioRepository negocioRepo;
	
	@Autowired
	UserService userService;

	public Usuario usuarioActual() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserDetails userDetails = null;
		Usuario user = null;
		if (principal instanceof UserDetails) {
			userDetails = (UserDetails) principal;
			String userName = userDetails.getUsername();
			user = this.userService.findByUsername(userName);
		} else {
			user = null;
		}
		return user;
	}
	
	public void creaNegocio(@Valid Negocio negocio) {
		
		usuarioActual().getNegocios().add(negocio);
		negocio.setUsuario(usuarioActual());
		negocioRepo.save(negocio);
		
	}


}
