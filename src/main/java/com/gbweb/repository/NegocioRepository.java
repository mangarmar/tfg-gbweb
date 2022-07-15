package com.gbweb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gbweb.entity.Negocio;

@Repository
public interface NegocioRepository extends JpaRepository<Negocio, Long> {
	
	public Negocio findByNombre(String nombre);
	
	@Query("SELECT id FROM Negocio WHERE USUARIO_ID = :id")
	public List<Long> findNegociosByUserId(@Param ("id") Long id);
	


}
