package com.gbweb.service;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gbweb.entity.Negocio;
import com.gbweb.repository.NegocioRepository;
import com.gbweb.repository.UserRepository;

@Service
public class NegocioService {
	
	@Autowired 
	NegocioRepository negocioRepo;
	
	@Autowired
	UserService userService;

	public void creaNegocio(@Valid Negocio negocio) {
		//Guardar en la lista de el gerente este negocio que le pertenece
		negocioRepo.save(negocio);
		
	}


}
