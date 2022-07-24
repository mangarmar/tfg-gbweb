package com.gbweb.service;

import static org.mockito.Mockito.doReturn;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.gbweb.entity.Producto;
import com.gbweb.repository.ProductoRepository;

@SpringBootTest
public class ProductoServiceTest {
	
	@MockBean
	ProductoRepository productoRepo;
	
	@Autowired
	ProductoService productoService;
	
	@Test
	void nuevoProducto() {
		Producto p = new Producto();
		
		doReturn(p).when(productoRepo).save(p);
		
		Producto res = productoService.nuevoProducto(p);
		
		Assertions.assertEquals(res, p);
	}
	
	@Test
	void findAll() {
		Producto p = new Producto();
		p.setId(1L);
		
		doReturn(Arrays.asList(p)).when(productoRepo).findAll();
		
		List<Producto> res = productoService.findAll();
		
		Assertions.assertEquals(res.get(0).getId(), p.getId());
	}
	
	@Test
	void findById() {
		Producto p = new Producto();
		p.setId(1L);
		
		doReturn(Optional.of(p)).when(productoRepo).findById(1L);
		
		Producto res = productoService.findById(1L);
		
		Assertions.assertEquals(res, p);
	}
	
	

}
