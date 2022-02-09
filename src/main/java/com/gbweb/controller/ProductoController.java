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
	public String añadirProducto(@PathVariable(value = "idNegocio") Long idNegocio, Model model) {
		model.addAttribute("idNegocio", idNegocio);
		model.addAttribute("producto", new Producto());
		return "producto/nuevoProducto";
	}

	@PostMapping("/añadirProducto/{idNegocio}")
	public String añadirProducto(@PathVariable(value = "idNegocio") Long idNegocio,
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

	@GetMapping("/eliminarProducto/{idNegocio}/{idProducto}")
	public String eliminarProducto(@PathVariable(value = "idProducto") Long idProducto,
			@PathVariable(value = "idNegocio") Long idNegocio, Model model) {

		Producto productoAEliminar = productoService.findById(idProducto);
		negocioService.findNegocioById(idNegocio).getProductos().remove(productoAEliminar);
		productoService.remove(productoAEliminar);
		return listarProductos(idNegocio, model);

	}

	@GetMapping("/editarProducto/{idNegocio}/{idProducto}")
	public String editarProducto(@PathVariable(value = "idProducto") Long idProducto,
			@PathVariable(value = "idNegocio") Long idNegocio, Model model) {

		model.addAttribute("idNegocio", idNegocio);
		model.addAttribute("producto", productoService.findById(idProducto));
		return "producto/nuevoProducto";
	}

	@PostMapping("/editarProducto/{idNegocio}/{idProducto}")
	public String editarProducto(@PathVariable(value = "idProducto") Long idProducto,
			@PathVariable(value = "idNegocio") Long idNegocio, @Valid @ModelAttribute("producto") Producto producto,
			BindingResult result, Model model) {

		if (result.hasErrors()) {
			model.addAttribute("producto", producto);
			return "producto/nuevoProducto";
		} else {

			productoService.nuevoProducto(producto);

		}
		return listarProductos(idNegocio, model);

	}

	@GetMapping("/cambiarVisibilidad/{idNegocio}/{idProducto}")
	public String cambiarVisibilidad(@PathVariable(value = "idProducto") Long idProducto,
			@PathVariable(value = "idNegocio") Long idNegocio, Model model) {

		Producto producto = productoService.findById(idProducto);

		if (producto.getVisibilidad() == false) {
			producto.setVisibilidad(true);
			productoService.nuevoProducto(producto);
		} else {
			producto.setVisibilidad(false);
			productoService.nuevoProducto(producto);
		}

		return listarProductos(idNegocio, model);

	}

}
