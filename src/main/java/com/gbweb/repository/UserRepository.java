package com.gbweb.repository;

import org.springframework.data.repository.CrudRepository;

import com.gbweb.entity.Usuario;

public interface UserRepository extends CrudRepository<Usuario, Long> {
	
	public Usuario findByUsername(String username);
	

}
