package com.gbweb.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gbweb.entity.Producto;

@Repository
public interface ProductoRepository extends CrudRepository<Producto, Long> {

//	@Query("SELECT id_producto FROM prod_neg WHERE id_negocio = :idNegocio")
//	public List<Long> idProductoPorNegocio(Long idNegocio);

}
