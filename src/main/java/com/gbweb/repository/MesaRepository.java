package com.gbweb.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gbweb.entity.Mesa;

@Repository
public interface MesaRepository extends CrudRepository<Mesa, Long> {


}
