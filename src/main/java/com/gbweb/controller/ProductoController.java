package com.gbweb.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
import com.gbweb.entity.Usuario;
import com.gbweb.enums.ROL;
import com.gbweb.enums.TipoProducto;
import com.gbweb.service.MesaService;
import com.gbweb.service.NegocioService;
import com.gbweb.service.ProductoService;
import com.gbweb.service.UserService;

@Controller
public class ProductoController {

	@Autowired
	ProductoService productoService;

	@Autowired
	NegocioService negocioService;
	
	@Autowired
	MesaService mesaService;
	
	
	@Autowired
	UserService userService;

	@RequestMapping("/listarProductos/{idNegocio}")
	public String listarProductos(@PathVariable(value = "idNegocio") Long idNegocio, Model model) {
		List<Producto> productos = negocioService.findNegocioById(idNegocio).getProductos();
		List<Producto> refrescos = productos.stream().filter(x->x.getTipo() ==  TipoProducto.Refresco).collect(Collectors.toList());
		List<Producto> alcohol =  productos.stream().filter(x->x.getTipo() ==  TipoProducto.Alcohol).collect(Collectors.toList());
		List<Producto> racion =  productos.stream().filter(x->x.getTipo() ==  TipoProducto.Ración).collect(Collectors.toList());
		List<Producto> media =  productos.stream().filter(x->x.getTipo() ==  TipoProducto.Media).collect(Collectors.toList());
		List<Producto> tapa =  productos.stream().filter(x->x.getTipo() ==  TipoProducto.Tapa).collect(Collectors.toList());
		List<Producto> snack =  productos.stream().filter(x->x.getTipo() ==  TipoProducto.Snack).collect(Collectors.toList());

		
		model.addAttribute("nombreNegocio", negocioService.findNegocioById(idNegocio).getNombre());
		model.addAttribute("idNegocio", idNegocio);
		model.addAttribute("refrescos", refrescos);
		model.addAttribute("alcohol", alcohol);
		model.addAttribute("racion", racion);
		model.addAttribute("media", media);
		model.addAttribute("tapa", tapa);
		model.addAttribute("snack", snack);
		model.addAttribute("usuario", usuarioActual());

		return "producto/listaProductos";

	}
	
	@RequestMapping("/pedir/{idNegocio}/mesa/{idMesa}")
	public String listarProductosParaPedir(@PathVariable(value = "idNegocio") Long idNegocio,@PathVariable(value = "idMesa") Long idMesa,
			Model model) {
		List<Producto> productos = negocioService.findNegocioById(idNegocio).getProductos();
		List<Producto> refrescos = productos.stream().filter(x->x.getTipo() ==  TipoProducto.Refresco).collect(Collectors.toList());
		List<Producto> alcohol =  productos.stream().filter(x->x.getTipo() ==  TipoProducto.Alcohol).collect(Collectors.toList());
		List<Producto> racion =  productos.stream().filter(x->x.getTipo() ==  TipoProducto.Ración).collect(Collectors.toList());
		List<Producto> media =  productos.stream().filter(x->x.getTipo() ==  TipoProducto.Media).collect(Collectors.toList());
		List<Producto> tapa =  productos.stream().filter(x->x.getTipo() ==  TipoProducto.Tapa).collect(Collectors.toList());
		List<Producto> snack =  productos.stream().filter(x->x.getTipo() ==  TipoProducto.Snack).collect(Collectors.toList());

		
		model.addAttribute("nombreNegocio", negocioService.findNegocioById(idNegocio).getNombre());
		model.addAttribute("idNegocio", idNegocio);
		model.addAttribute("refrescos", refrescos);
		model.addAttribute("alcohol", alcohol);
		model.addAttribute("racion", racion);
		model.addAttribute("media", media);
		model.addAttribute("tapa", tapa);
		model.addAttribute("snack", snack);
		model.addAttribute("usuario", usuarioActual());
		model.addAttribute("codigoMesa", mesaService.findById(idMesa).getCodigo());
		model.addAttribute("idNegocio", idNegocio);
		
		System.out.println(usuarioActual().getMesa());
		System.out.println(mesaService.findById(idMesa).getCodigo());


		return "producto/carta";

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
		return "redirect:/listarProductos/"+idNegocio;

	}

	@GetMapping("/eliminarProducto/{idNegocio}/{idProducto}")
	public String eliminarProducto(@PathVariable(value = "idProducto") Long idProducto,
			@PathVariable(value = "idNegocio") Long idNegocio, Model model) {

		Producto productoAEliminar = productoService.findById(idProducto);
		negocioService.findNegocioById(idNegocio).getProductos().remove(productoAEliminar);
		productoService.remove(productoAEliminar);
		
		return "redirect:/listarProductos/"+idNegocio;

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
		return "redirect:/listarProductos/"+idNegocio;

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

		return "redirect:/listarProductos/"+idNegocio;

	}

	
	public Usuario usuarioActual() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserDetails userDetails = null;
		Usuario user = null;
		if (principal instanceof UserDetails) {
			userDetails = (UserDetails) principal;
			String userName = userDetails.getUsername();
			user = this.userService.findByUsername(userName);
		} else {
			user = null;
		}
		return user;
	}
	
		


}
