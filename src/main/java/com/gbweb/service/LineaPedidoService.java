package com.gbweb.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gbweb.entity.LineaPedido;
import com.gbweb.entity.Pedido;
import com.gbweb.repository.LineaPedidoRepository;

@Service
public class LineaPedidoService {
	
	@Autowired
	LineaPedidoRepository lineaPedidoRepo;
	
	@Autowired
	MesaService mesaService;

	public void save(LineaPedido lp) {
		
		 lineaPedidoRepo.save(lp);	
	}
	
//	public List<LineaPedido> lineaPedidosPorPedido(Long idMesa){
//		List<LineaPedido> lps = lineaPedidoRepo.findAll();
//		Pedido pedido = mesaService.findById(idMesa).getPedidos().stream().filter(x->x.getEstadoPedido().toString().equals("ACTIVO")).findFirst().orElse(null);
//		
//		return lps.stream().filter(x->x.getPedido().equals(pedido)).collect(Collectors.toList());
//	}
	
	public void borraTodosNoConfirmadosPorPedido(Pedido pedidoActivo) {
		List<LineaPedido> lps = lineaPedidoRepo.findAll();
		List<LineaPedido> borrar = new ArrayList<LineaPedido>();
		for(int i = 0;i<lps.size();i++) {
			Boolean servido = lps.get(i).getServido();
			if(servido == null) {
				borrar.add(lps.get(i));
			}
		}
		
		 lineaPedidoRepo.deleteAll(borrar);
	}
	
	public List<LineaPedido> findTodosPorPedido(Pedido pedidoActivo) {
		List<LineaPedido> lps = lineaPedidoRepo.findAll();
		
		 return lps.stream().filter(x->x.getPedido().equals(pedidoActivo)).collect(Collectors.toList());
	}
	
	
	

}
