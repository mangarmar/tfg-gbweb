package com.gbweb.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gbweb.entity.Usuario;

@Repository
public interface UserRepository extends CrudRepository<Usuario, Long> {
	
	public Usuario findByUsername(String username);
	

}
