package com.gbweb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gbweb.entity.Negocio;
import com.gbweb.entity.Usuario;

@Repository
public interface NegocioRepository extends CrudRepository<Negocio, Long> {
	
	public Negocio findByNombre(String nombre);
	
	@Query("SELECT id FROM Negocio WHERE USUARIO_ID = :id")
	public List<Long> findNegociosByUserId(@Param ("id") Long id);
	


}
