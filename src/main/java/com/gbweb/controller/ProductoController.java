
package com.gbweb.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.gbweb.entity.LineaPedido;
import com.gbweb.entity.Mesa;
import com.gbweb.entity.Pedido;
import com.gbweb.entity.Producto;
import com.gbweb.entity.Usuario;
import com.gbweb.enums.EstadoPedido;
import com.gbweb.enums.TipoProducto;
import com.gbweb.service.LineaPedidoService;
import com.gbweb.service.MesaService;
import com.gbweb.service.NegocioService;
import com.gbweb.service.PedidoService;
import com.gbweb.service.ProductoService;
import com.gbweb.service.UserService;


@Controller
public class ProductoController {
	
	@Autowired
	LineaPedidoService lineaPedidoService;

	@Autowired
	ProductoService productoService;

	@Autowired
	NegocioService negocioService;
	
	@Autowired
	MesaService mesaService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	PedidoService pedidoService;

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
		model.addAttribute("usuario", userService.usuarioActual());

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

		Boolean ok = false;
		Usuario usuario = userService.usuarioActual();
		Mesa mesa = mesaService.findById(idMesa);
		boolean pedidoActivo = mesa.getPedidos().stream().anyMatch(x->x.getEstadoPedido().equals(EstadoPedido.ACTIVO));
		
		if(pedidoActivo==false && usuario.getMesa()!=null) {
			Pedido pedido = new Pedido();
			pedido.setEstadoPedido(EstadoPedido.ACTIVO);
			pedido.setMesa(mesaService.findById(idMesa));
			pedidoService.nuevoPedido(pedido);
		}
		
	
		if(usuario.getMesa()!=null) {
			model.addAttribute("ok", true);
			model.addAttribute("nombreNegocio", negocioService.findNegocioById(idNegocio).getNombre());
			model.addAttribute("idNegocio", idNegocio);
			model.addAttribute("refrescos", refrescos.stream().filter(x->x.getVisibilidad().equals(true)).collect(Collectors.toList()));
			model.addAttribute("alcohol", alcohol.stream().filter(x->x.getVisibilidad().equals(true)).collect(Collectors.toList()));
			model.addAttribute("racion", racion.stream().filter(x->x.getVisibilidad().equals(true)).collect(Collectors.toList()));
			model.addAttribute("media", media.stream().filter(x->x.getVisibilidad().equals(true)).collect(Collectors.toList()));
			model.addAttribute("tapa", tapa.stream().filter(x->x.getVisibilidad().equals(true)).collect(Collectors.toList()));
			model.addAttribute("snack", snack.stream().filter(x->x.getVisibilidad().equals(true)).collect(Collectors.toList()));
			model.addAttribute("usuario", usuario);
			model.addAttribute("codigoMesa", mesaService.findById(idMesa).getCodigo());
			model.addAttribute("idNegocio", idNegocio);
			
			return "producto/carta";
		}else {
			model.addAttribute("ok", ok);
			return "redirect:/";
		}

	}
	
	@GetMapping("/pedido/negocio/{idNegocio}/mesa/{idMesa}")
	public String listarPedido(@PathVariable(value = "idNegocio") Long idNegocio,@PathVariable(value = "idMesa") Long idMesa,
			Model model) {
		Pedido pedidoActivo = mesaService.findById(idMesa).getPedidos().stream().filter(x->x.getEstadoPedido().toString().equals("ACTIVO")).findFirst().orElse(null);
		if(pedidoActivo!=null) {
			lineaPedidoService.borraTodosNoConfirmadosPorPedido(pedidoActivo);
			Double precioTotal = 0.0;
			List<Producto> productos = pedidoActivo.getProductos();
			Map<Producto, Integer> numeroProductos = new HashMap<>();
			List<LineaPedido> lineaPedidos = new ArrayList<LineaPedido>();
			for (int i = 0; i < productos.size(); i++) {
				if(!numeroProductos.containsKey(productos.get(i))) {
					numeroProductos.put(productos.get(i), 1);
					precioTotal+=productos.get(i).getPrecio();
				}else {
					numeroProductos.put(productos.get(i), numeroProductos.get(productos.get(i))+1);
					precioTotal+=productos.get(i).getPrecio();
				}
			}
			List<Producto> listaProductos = numeroProductos.keySet().stream().collect(Collectors.toList());
			System.out.println(listaProductos);
			for(int j = 0; j<listaProductos.size();j++) {
				LineaPedido lp = new LineaPedido();
				lp.setNombre(listaProductos.get(j).getNombre());
				lp.setCantidad(numeroProductos.values().stream().collect(Collectors.toList()).get(j));
				lp.setPrecio(listaProductos.get(j).getPrecio());
				lp.setPedido(pedidoActivo);
				lp.setProducto(listaProductos.get(j));
				lineaPedidos.add(lp);
				lineaPedidoService.save(lp);
			}

			model.addAttribute("lineaPedidos", lineaPedidos);
			model.addAttribute("precioTotal", precioTotal);
			
			return "negocio/comanda";
		} else {
			return "redirect:/";
		}
			
	}
	
	@GetMapping("/pedido/confirmar/negocio/{idNegocio}/mesa/{idMesa}")
	public String confirmarComanda(@PathVariable(value = "idNegocio") Long idNegocio,@PathVariable(value = "idMesa") Long idMesa,
			Model model) {
		Mesa mesa = mesaService.findById(idMesa);
		Pedido pedidoActivo = mesa.getPedidos().stream().filter(x->x.getEstadoPedido().toString().equals("ACTIVO")).findFirst().orElse(null);
		List<LineaPedido> lineaPedidos= lineaPedidoService.findTodosPorPedido(pedidoActivo);
		List<LineaPedido> comanda = new ArrayList<LineaPedido>();
		for (int i = 0; i < lineaPedidos.size(); i++) {
			Boolean servido = lineaPedidos.get(i).getServido();
			if(servido == null) {
				comanda.add(lineaPedidos.get(i));
				lineaPedidos.get(i).setServido(false);
				lineaPedidoService.save(lineaPedidos.get(i));
			}
		}
		pedidoService.borrarProductosPedido(pedidoActivo.getProductos(), pedidoActivo.getId());
		model.addAttribute("idMesa", idMesa);
		return "redirect:/pedir/"+mesa.getNegocio().getId()+"/mesa/"+idMesa;

	}
	
	@GetMapping("/pedido/sumar/{idPedido}")
	public String sumarProductoAComanda(@PathVariable(value = "idPedido") Long idPedido, Model model) {
		
		LineaPedido lineaPedido = lineaPedidoService.findById(idPedido);
		Pedido nuevoPedido = lineaPedido.getPedido();
		nuevoPedido.getProductos().add(lineaPedido.getProducto());
		pedidoService.nuevoPedido(nuevoPedido);
		
		return "redirect:/pedido/negocio/"+lineaPedido.getPedido().getMesa().getNegocio().getId()+"/mesa/"+lineaPedido.getPedido().getMesa().getId();

	}
	
	@GetMapping("/pedido/restar/{idPedido}")
	public String restarProductoAComanda(@PathVariable(value = "idPedido") Long idPedido, Model model) {
		
		LineaPedido lineaPedido = lineaPedidoService.findById(idPedido);
		Pedido nuevoPedido = lineaPedido.getPedido();
		nuevoPedido.getProductos().remove(lineaPedido.getProducto());
		pedidoService.nuevoPedido(nuevoPedido);
		
		return "redirect:/pedido/negocio/"+lineaPedido.getPedido().getMesa().getNegocio().getId()+"/mesa/"+lineaPedido.getPedido().getMesa().getId();

	}

	@RequestMapping("/pedido/cuenta/{idNegocio}/mesa/{idMesa}")
	public String cuenta(@PathVariable(value = "idNegocio") Long idNegocio,@PathVariable(value = "idMesa") Long idMesa,
			Model model) {
		Pedido pedidoActivo = mesaService.findById(idMesa).getPedidos().stream().filter(x->x.getEstadoPedido().toString().equals("ACTIVO")).findFirst().orElse(null);
		Boolean ok = false;
		if(pedidoActivo!=null) {
		Long idPedido = pedidoActivo.getId();
		List<LineaPedido> lineaPedidos = pedidoActivo.getLineaPedidos();
		List<LineaPedido> productosNoServidos = new ArrayList<LineaPedido>();
		List<LineaPedido> productosServidos = new ArrayList<LineaPedido>();
		Double precioTotalServido = 0.0;
		Double precioTotalNoServido = 0.0;
		for(int i = 0; i<lineaPedidos.size();i++){
			LineaPedido lp = lineaPedidos.get(i);
			if(lp.getServido()!=null) {
				if(lp.getServido()==false) {
					precioTotalNoServido+=lineaPedidos.get(i).getPrecio()*lineaPedidos.get(i).getCantidad();
					productosNoServidos.add(lp);
				}else if(lp.getServido()==true) {
					precioTotalServido+=lineaPedidos.get(i).getPrecio()*lineaPedidos.get(i).getCantidad();
					productosServidos.add(lp);
				}
			}
		}
		
		model.addAttribute("ok", true);
		model.addAttribute("nombreNegocio", negocioService.findNegocioById(idNegocio).getNombre());
		model.addAttribute("productosNoServidos", productosNoServidos);
		model.addAttribute("productosServidos",productosServidos);
		model.addAttribute("precioTotalNoServido", precioTotalNoServido);
		model.addAttribute("precioTotal", precioTotalServido);
		model.addAttribute("usuario", userService.usuarioActual());
		model.addAttribute("idNegocio", idNegocio);
		model.addAttribute("idPedido", idPedido);
		
		return "negocio/cuenta";
		}else {	
		return "redirect:/";
		}

		

	}
	
	
	
	@RequestMapping("/pedido/servir/{idNegocio}/{idMesa}/{idProducto}")
	public String servirProducto(@PathVariable(value = "idProducto") Long idProducto,@PathVariable(value = "idNegocio") Long idNegocio,@PathVariable(value = "idMesa") Long idMesa,
			Model model) {
		
		LineaPedido lp = lineaPedidoService.findById(idProducto);
		lp.setServido(true);
		lineaPedidoService.save(lp);	
		return "redirect:/mesas/pedido/"+idNegocio+"/"+idMesa;

	}
	
	@RequestMapping("/pedido/sumar/{idNegocio}/{idMesa}/{idProducto}")
	public String sumarProducto(@PathVariable(value = "idProducto") Long idProducto,@PathVariable(value = "idNegocio") Long idNegocio,@PathVariable(value = "idMesa") Long idMesa,
			Model model) {
		
		LineaPedido lp = lineaPedidoService.findById(idProducto);
		lp.setCantidad(lp.getCantidad()+1);
		lineaPedidoService.save(lp);	
		return "redirect:/mesas/pedido/"+idNegocio+"/"+idMesa;

	}
	
	@RequestMapping("/pedido/restar/{idNegocio}/{idMesa}/{idProducto}")
	public String restarProducto(@PathVariable(value = "idProducto") Long idProducto,@PathVariable(value = "idNegocio") Long idNegocio,@PathVariable(value = "idMesa") Long idMesa,
			Model model) {
		
		LineaPedido lp = lineaPedidoService.findById(idProducto);
		if(lp.getCantidad()>0) {
			lp.setCantidad(lp.getCantidad()-1);
			lineaPedidoService.save(lp);
		}else if(lp.getCantidad()==0) {
			lineaPedidoService.borraLineaPedido(lp);
		}
			
		return "redirect:/mesas/pedido/"+idNegocio+"/"+idMesa;

	}
	
	@RequestMapping("/pedido/eliminar/{idNegocio}/{idMesa}/{idProducto}")
	public String eliminarProducto(@PathVariable(value = "idProducto") Long idProducto,@PathVariable(value = "idNegocio") Long idNegocio,@PathVariable(value = "idMesa") Long idMesa,
			Model model) {
		
		LineaPedido lp = lineaPedidoService.findById(idProducto);
		lineaPedidoService.borraLineaPedido(lp);
			
		return "redirect:/mesas/pedido/"+idNegocio+"/"+idMesa;

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
	
	@PostMapping("/añadirAlPedido/pedir/{idNegocio}/mesa/{idMesa}/producto/{idProducto}")
	public String añadirAlPedido(@Valid @ModelAttribute("cantidad") Long cantidad, @PathVariable(value = "idNegocio") Long idNegocio,@PathVariable(value = "idMesa") Long idMesa,
			@PathVariable(value = "idProducto") Long idProducto,Model model) {
		
		Pedido pedido = mesaService.findById(idMesa).getPedidos().stream().filter(x->x.getEstadoPedido().equals(EstadoPedido.ACTIVO)).findFirst().orElse(null);
		Producto producto = productoService.findById(idProducto);
		
		if(pedido != null) {
			for (int i = 0; i < cantidad; i++) {
				pedido.getProductos().add(producto);
			}	
			pedidoService.nuevoPedido(pedido);
		}
		
		return "redirect:/pedir/"+idNegocio+"/mesa/"+idMesa;

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


	



}
