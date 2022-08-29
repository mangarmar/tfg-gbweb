package com.gbweb.service;

import java.util.List;

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
	
	public Pedido nuevoPedido(@Valid Pedido pedido) {
		return pedidoRepo.save(pedido);
	}
	
	public void borrarProductosPedido(List<Producto> productos, Long idPedido) {
		Pedido pedido = pedidoRepo.findById(idPedido).get();
		pedido.getProductos().clear();
		pedidoRepo.save(pedido);
	}

	public Pedido findById(Long idPedido) {
		return pedidoRepo.findById(idPedido).orElse(null);
	}
	
	public List<Pedido> findAll() {
		return (List<Pedido>) pedidoRepo.findAll();
	}


}
