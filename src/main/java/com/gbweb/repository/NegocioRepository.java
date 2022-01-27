package com.gbweb.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gbweb.entity.Negocio;
import com.gbweb.entity.Usuario;

@Repository
public interface NegocioRepository extends CrudRepository<Negocio, Long> {
	
	public Negocio findByNombre(String nombre);

}
