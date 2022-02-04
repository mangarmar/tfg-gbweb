package com.gbweb.controller;

import java.util.List;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
	
	
	@RequestMapping("/listarProductos/{idNegocio}")
	public String listarProductos(@PathVariable(value = "idNegocio") Long idNegocio, Model model) {
		List<Producto> productos = negocioService.findNegocioById(idNegocio).getProductos();
		
		model.addAttribute("nombreNegocio", negocioService.findNegocioById(idNegocio).getNombre());
		model.addAttribute("idNegocio", idNegocio);
		model.addAttribute("productos", productos);
		
		return "producto/listaProductos";
		
	}
	
	@RequestMapping("/añadirProducto/{idNegocio}")
	public String añadirProducto(@PathVariable(value="idNegocio") Long idNegocio, Model model) {
		model.addAttribute("idNegocio", idNegocio);
		model.addAttribute("producto", new Producto());
		return "producto/nuevoProducto";
	}
	
	@PostMapping("/añadirProducto/{idNegocio}")
	public String añadirProducto(@PathVariable(value="idNegocio") Long idNegocio,
			@Valid @ModelAttribute("producto") Producto producto, BindingResult result, Model model) {
		
		if (result.hasErrors()) {
			model.addAttribute("producto", producto);
			return "producto/nuevoProducto";
		} else {
			
			negocioService.findNegocioById(Long.valueOf(idNegocio)).getProductos().add(producto);
			productoService.nuevoProducto(producto);
			
		}
		return listarProductos(idNegocio, model);

	}

}
