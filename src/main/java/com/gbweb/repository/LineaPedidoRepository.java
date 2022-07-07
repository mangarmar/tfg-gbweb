package com.gbweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gbweb.entity.LineaPedido;

@Repository
public interface LineaPedidoRepository extends JpaRepository<LineaPedido, Long> {

}
