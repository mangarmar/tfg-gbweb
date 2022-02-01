package com.gbweb.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.gbweb.entity.Usuario;
import com.gbweb.enums.ROL;
import com.gbweb.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private BCryptPasswordEncoder passw;
	
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

	@Transactional
	public void creaCliente(@Valid Usuario cliente) {
		
		String password = passw.encode(cliente.getPassword());
		cliente.setPassword(password);
		cliente.setNegocios(null);
		cliente.setRol(ROL.CLIENTE);
		userRepo.save(cliente);

	}

	@Transactional
	public void creaGerente(@Valid Usuario gerente) {
		
		String password = passw.encode(gerente.getPassword());
		gerente.setPassword(password);
		gerente.setRol(ROL.GERENTE);
		userRepo.save(gerente);

	}

	public List<Usuario> findAllUsers() {

		return (List<Usuario>) userRepo.findAll();

	}

	public List<Usuario> findAllUsuarios() {

		List<Usuario> l = new ArrayList<Usuario>();
		l = (List<Usuario>) userRepo.findAll();

		return l;

	}

	public Usuario findById(Long id) {
		return userRepo.findById(id).orElse(null);
	}

	public void save(Usuario u) {
		this.userRepo.save(u);
	}

	public void eliminarUsuario(Usuario u) {
		userRepo.delete(u);
	}

	public Usuario usuarioActual() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserDetails userDetails = null;
		Usuario us = null;
		if (principal instanceof UserDetails) {
			userDetails = (UserDetails) principal;
			String userName = userDetails.getUsername();
			us = this.findByUsername(userName);
		} else {
			us = null;
		}

		return us;

	}

}
