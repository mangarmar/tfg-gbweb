package com.gbweb.service;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gbweb.entity.Pedido;
import com.gbweb.entity.Producto;
import com.gbweb.repository.PedidoRepository;

@Service
public class PedidoService {
	
	@Autowired
	PedidoRepository pedidoRepo;
	
	public void nuevoPedido(@Valid Pedido pedido) {
		pedidoRepo.save(pedido);
	}
	

}
