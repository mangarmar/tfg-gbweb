package com.gbweb.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

	public List<Negocio> findNegociosByUserId(Long id){
		List<Negocio> negocios  = (List<Negocio>) this.negocioRepo.findAll();
		List<Negocio> negociosPorUsuario = negocios.stream().filter(x->x.getUsuario().getId().equals(id)).collect(Collectors.toList());
		return negociosPorUsuario;
		
	}
	
	public Negocio findNegocioById(Long idNegocio) {
		return negocioRepo.findById(idNegocio).get();
	}
}
