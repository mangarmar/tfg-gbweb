package com.gbweb.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gbweb.entity.Producto;
import com.gbweb.repository.ProductoRepository;

@Service
public class ProductoService {
	
	@Autowired
	ProductoRepository productoRepo;

	public void nuevoProducto(@Valid Producto producto) {
		productoRepo.save(producto);
	}
	
	public List<Producto> findAll(){
		
		return (List<Producto>) productoRepo.findAll();
	}


}
