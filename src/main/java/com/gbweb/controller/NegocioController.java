package com.gbweb.controller;

import java.util.ArrayList;
import java.util.List;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gbweb.entity.LineaPedido;
import com.gbweb.entity.Mesa;
import com.gbweb.entity.Negocio;
import com.gbweb.entity.Pedido;
import com.gbweb.entity.Usuario;
import com.gbweb.enums.ROL;
import com.gbweb.service.MesaService;
import com.gbweb.service.NegocioService;
import com.gbweb.service.PedidoService;
import com.gbweb.service.UserService;




@Controller
public class NegocioController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	NegocioService negocioService;
	
	@Autowired
	MesaService mesaService;
	
	@Autowired
	PedidoService pedidoService;
	
	@Autowired
	ProductoController productoController;
	
	

	
	@GetMapping("/crearNegocio")
	public String crearNegocio(Model model) {
		model.addAttribute("negocio", new Negocio());
		return "negocio/formularioNegocio";
	}
	
	
	@PostMapping("/crearNegocio")
	public String nuevoNegocio(@Valid @ModelAttribute("negocio") Negocio negocio, BindingResult result, Model model
			,RedirectAttributes redirectAttributes) {
		
		if (result.hasErrors()) {
			model.addAttribute("negocio", negocio);
			return "negocio/formularioNegocio";
		} else {
			try {
				negocioService.creaNegocio(negocio);
				return "redirect:/listarNegocios";
			} catch (Exception e) {
				model.addAttribute("message", "Lo sentimos, no se ha podido encontrar la localización indicada");
				return "negocio/formularioNegocio";
			}
		
		}
		


	}
	
	
	@GetMapping("/editarNegocio/{idNegocio}")
	public String editarNegocio(@PathVariable(value = "idNegocio") Long idNegocio, Model model) {

		model.addAttribute("negocio", negocioService.findNegocioById(idNegocio));
		return "negocio/editarNegocio";
	}

	@PostMapping("/editarNegocio/{idNegocio}")
	public String editar(@PathVariable(value = "idNegocio") Long idNegocio,
			@Valid @ModelAttribute("negocio") Negocio negocio,
			BindingResult result, Model model) {

		if (result.hasErrors()) {
			model.addAttribute("negocio", negocio);
			return "negocio/editarNegocio";
		} else {
			try {
				negocioService.editarNegocio(negocio, idNegocio);
				return listarNegocios(model);
			} catch (Exception e) {
				model.addAttribute("message", "Lo sentimos, no se ha podido encontrar la localización indicada");
				return "negocio/editarNegocio";
			}

		}
		

	}
	
	@GetMapping("/listarNegocios")
	public String listarNegocios(Model model) {
		
		if(userService.usuarioActual()==null) {
			List<Negocio> negocios = negocioService.findAll();
			model.addAttribute("negocios", negocios);
			return "negocio/listaNegociosClientes";
		}else if(userService.usuarioActual().getRol().equals(ROL.CLIENTE) || userService.usuarioActual().getRol().equals(ROL.ADMIN)) {
			List<Negocio> negocios = negocioService.findAll();
			model.addAttribute("negocios", negocios);
			return "negocio/listaNegociosClientes";
		}else{
			List<Negocio> negociosPorUsuario = negocioService.findNegociosByUserId(userService.usuarioActual().getId());
			model.addAttribute("negocios", negociosPorUsuario);
			return "negocio/listaNegocios";
		}
			
	}
	
	
	@GetMapping("/pedidos/{idNegocio}")
	public String listarPedidosPorNegocio(@PathVariable(value = "idNegocio") Long idNegocio, Model model) {
		
		Negocio negocio = negocioService.findNegocioById(idNegocio);
		List<Mesa> mesas = negocio.getMesas();
		List<Pedido> pedidos = new ArrayList<>();
		for(int i = 0; i<mesas.size();i++) {
			pedidos.addAll(mesas.get(i).getPedidos());
		}
		
		model.addAttribute("negocio", negocio);		
		model.addAttribute("pedidos", pedidos.stream().filter(x->!x.getEstadoPedido().toString().equals("ACTIVO")).collect(Collectors.toList()));
		
		return "negocio/listaPedidos";
	}
	
	@GetMapping("/pedidos/{idNegocio}/{idPedido}")
	public String listarPedidosPorNegocioPorPedido(@PathVariable(value = "idNegocio") Long idNegocio,
			@PathVariable(value = "idPedido") Long idPedido, Model model) {
		
		Pedido pedido = pedidoService.findById(idPedido);
		List<LineaPedido> productos = pedido.getLineaPedidos();
		Double precioTotal = 0.0;
		
		for(int i = 0; i<productos.size();i++) {
			precioTotal+=productos.get(i).getCantidad()*productos.get(i).getPrecio();
		}
		
		model.addAttribute("precioTotal", precioTotal);
		model.addAttribute("idPedido", idPedido);		
		model.addAttribute("productos",productos);
		
		return "negocio/listaProductosPedido";
	}
	
	

	
	
	
}
