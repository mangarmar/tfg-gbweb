package com.gbweb.service;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.gbweb.entity.Pedido;
import com.gbweb.entity.Producto;
import com.gbweb.repository.PedidoRepository;

@SpringBootTest
public class PedidoServiceTest {
	
	@MockBean
	PedidoRepository pedidoRepo;
	
	@Autowired
	PedidoService pedidoService;
	
	@Test
	void save() {
		Pedido p = new Pedido();
		
		doReturn(p).when(pedidoRepo).save(p);
		
		Pedido res = pedidoService.nuevoPedido(p);
		
		Assertions.assertEquals(res, p, "El pedido no se ha creado"); 		
	}
	
	@Test
	void borrarProductosPedido() {
		Pedido p = new Pedido();
		Producto pr = new Producto();

		p.setId(1L);
		
		List<Producto> listPr = new ArrayList<>();
		listPr.add(pr);
		p.setProductos(listPr);
		
		Integer numProductos = p.getProductos().size();
		
		doReturn(Optional.of(p)).when(pedidoRepo).findById(1L);
		
		pedidoService.borrarProductosPedido(listPr, 1L);
		
		Assertions.assertNotEquals(numProductos, p.getProductos(), "No se han eliminado los productos"); 		
	}

	@Test
	void findById() {
		Pedido p = new Pedido();
		p.setId(1L);
		
		doReturn(Optional.of(p)).when(pedidoRepo).findById(1L);
		
		Pedido res = pedidoService.findById(1L);
		
		Assertions.assertEquals(res, p, "No hay ninguna mesa con esa ID"); 		

	}
	
}
