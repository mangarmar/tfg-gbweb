package com.gbweb.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.gbweb.entity.Negocio;
import com.gbweb.entity.Producto;
import com.gbweb.service.NegocioService;
import com.gbweb.service.ProductoService;

@Controller
public class ProductoController {
	
	@Autowired
	ProductoService productoService;
	
	@Autowired
	NegocioService negocioService;
	
	@GetMapping("/crearProducto")
	public String crearProducto(Model model) {
		model.addAttribute("producto", new Producto());
//		model.addAttribute("id", negocioId);
		return "producto/nuevoProducto";
	}
	
	
	@PostMapping("/crearProducto")
	public String guardarProducto(@Valid @ModelAttribute("producto") Producto producto, BindingResult result, Model model) {
		
		if (result.hasErrors()) {
			model.addAttribute("producto", producto);
			return "producto/nuevoProducto";
		} else {
			
//			negocioService.findNegocioById(Long.valueOf(negocioId)).getProductos().add(producto);
			productoService.nuevoProducto(producto);
			
		}
		return "redirect:";

	}
	
	@GetMapping("/listarProductos")
	public String listarProductos(Model model) {
		
		List<Producto> productos = productoService.findAll();
		model.addAttribute("productos", productos);
		
		return "producto/listaProductos";
		
	}

}
