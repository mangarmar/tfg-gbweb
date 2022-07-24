package com.gbweb.service;

import static org.mockito.Mockito.doReturn;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.gbweb.entity.LineaPedido;
import com.gbweb.entity.Mesa;
import com.gbweb.entity.Pedido;
import com.gbweb.enums.EstadoPedido;
import com.gbweb.repository.LineaPedidoRepository;

@SpringBootTest
public class LineaPedidoServiceTest {
	
@MockBean
LineaPedidoRepository lineaPedidoRepo;

@Autowired
LineaPedidoService lineaPedidoService;

@Test
void save() {
	LineaPedido lp = new LineaPedido();
	
	doReturn(lp).when(lineaPedidoRepo).save(lp);
	
	LineaPedido res = lineaPedidoService.save(lp);
	
	Assertions.assertEquals(res, lp, "La línea de pedido no se ha creado"); 		
}

@Test
void borraTodosNoConfirmadosPorPedido() {
	LineaPedido lp = new LineaPedido();
	lp.setServido(null);
	LineaPedido lp2 = new LineaPedido();
	lp2.setServido(true);
	Pedido pedido = new Pedido();
	pedido.setLineaPedidos(Arrays.asList(lp,lp2));
	lineaPedidoService.borraTodosNoConfirmadosPorPedido(pedido);
	
	Assertions.assertNotEquals(lineaPedidoRepo.findAll().size(), 2, "No se han borrado las lineas de pedido no servidos"); 		

}

@Test
void findTodosPorPedido() {
	LineaPedido lp = new LineaPedido();
	LineaPedido lp1 = new LineaPedido();
	Pedido p = new Pedido();
	lp.setPedido(p);
	lp1.setPedido(p);
	p.setLineaPedidos(Arrays.asList(lp, lp1));
	
	doReturn(Arrays.asList(lp,lp1)).when(lineaPedidoRepo).findAll();
	Integer numLpsPedido = lineaPedidoService.findTodosPorPedido(p).size();
	
	Assertions.assertEquals(numLpsPedido, 2);
}

@Test
void findById() {
	LineaPedido lp = new LineaPedido();
	lp.setId(1L);
	
	doReturn(Optional.of(lp)).when(lineaPedidoRepo).findById(1L);
	
	LineaPedido res = lineaPedidoService.findById(1L);
	
	Assertions.assertEquals(res, lp);
}



}
