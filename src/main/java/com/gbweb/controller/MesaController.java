package com.gbweb.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gbweb.entity.LineaPedido;
import com.gbweb.entity.Mesa;
import com.gbweb.entity.Negocio;
import com.gbweb.entity.Pedido;
import com.gbweb.entity.Producto;
import com.gbweb.entity.Usuario;
import com.gbweb.enums.Estado;
import com.gbweb.service.MesaService;
import com.gbweb.service.NegocioService;
import com.gbweb.service.UserService;

@Controller
@RequestMapping("/mesas")
public class MesaController {
	
	@Autowired
	NegocioService negocioService;
	
	@Autowired
	MesaService mesaService;
	
	@Autowired
	UserService userService;
	
	
	@RequestMapping("/{idNegocio}")
	public String listarMesas(@PathVariable(value = "idNegocio") Long idNegocio, Model model) {
		Negocio negocio = negocioService.findNegocioById(idNegocio);
		List<Mesa> mesas = negocio.getMesas();
		Map<Mesa, Pedido> pedidosPorMesa = new LinkedHashMap<>();
		for(int i = 0;i<mesas.size();i++) {
			Pedido pedido = mesas.get(i).getPedidos().stream().filter(x->x.getEstadoPedido().toString().equals("ACTIVO")).findFirst().orElse(null);
			pedidosPorMesa.put(mesas.get(i), pedido);
		}
		
		model.addAttribute("nombreNegocio", negocio.getNombre());
		model.addAttribute("idNegocio", idNegocio);
		model.addAttribute("pedidosPorMesa", pedidosPorMesa);

		return "negocio/listarMesas";

	}
	
	@RequestMapping("/libres/{idNegocio}")
	public String listarMesasLibres(@PathVariable(value = "idNegocio") Long idNegocio, Model model) {
		Negocio negocio = negocioService.findNegocioById(idNegocio);
		List<Mesa> mesas = negocio.getMesas();
		Map<Mesa, Pedido> pedidosPorMesa = new LinkedHashMap<>();
		for(int i = 0;i<mesas.size();i++) {
			if(mesas.get(i).getEstado().toString().equals("LIBRE")) {
				Pedido pedido = mesas.get(i).getPedidos().stream().filter(x->x.getEstadoPedido().toString().equals("ACTIVO")).findFirst().orElse(null);
				pedidosPorMesa.put(mesas.get(i), pedido);
			}
		}
		
		model.addAttribute("nombreNegocio", negocio.getNombre());
		model.addAttribute("idNegocio", idNegocio);
		model.addAttribute("pedidosPorMesa", pedidosPorMesa);

		return "negocio/listarMesas";

	}
	
	@RequestMapping("/estado/{idNegocio}")
	public String miMesa(@PathVariable(value = "idNegocio") Long idNegocio, Model model) {
		Negocio negocio = negocioService.findNegocioById(idNegocio);
		List<Mesa> mesas = negocio.getMesas();
		Map<Mesa, Pedido> pedidosPorMesa = new LinkedHashMap<>();
		Usuario usuario = usuarioActual();

		for(int i = 0;i<mesas.size();i++) {
			if(usuario.getMesa().getCodigo().equals(mesas.get(i).getCodigo())) {
				Pedido pedido = mesas.get(i).getPedidos().stream().filter(x->x.getEstadoPedido().toString().equals("ACTIVO")).findFirst().orElse(null);
				pedidosPorMesa.put(mesas.get(i), pedido);
			}
		}
		
		model.addAttribute("nombreNegocio", negocio.getNombre());
		model.addAttribute("idNegocio", idNegocio);
		model.addAttribute("pedidosPorMesa", pedidosPorMesa);
		model.addAttribute("usuario", usuario);

		return "negocio/listarMesas";

	}
	
	
	@GetMapping("/añadirMesa/{idNegocio}")
	public String añadirMesa(@PathVariable(value = "idNegocio") Long idNegocio, Model model) {
		model.addAttribute("idNegocio", idNegocio);
		model.addAttribute("mesa", new Mesa());
		return "negocio/añadirMesa";
	}
	
	
	@PostMapping("/añadirMesa/{idNegocio}")
	public String añadirMesa(@PathVariable(value = "idNegocio") Long idNegocio,
			@Valid @ModelAttribute("mesa") Mesa mesa, BindingResult result, Model model) {

		if (result.hasErrors()) {
			model.addAttribute("mesa", mesa);
			return "negocio/añadirMesa";
		} else {
			
			mesaService.añadirMesa(mesa, idNegocio);

		}
	
		return "redirect:/mesas/"+idNegocio;

	}
	
	@GetMapping("/mesaLibre/{idNegocio}/{idMesa}")
	public String mesaLibre(@PathVariable(value = "idNegocio") Long idNegocio,@PathVariable(value="idMesa") Long idMesa, Model model) {
					
		Mesa mesa = mesaService.findById(idMesa);
		mesaService.actualizarEstado(mesa, idNegocio, Estado.LIBRE);
		
		return "redirect:/mesas/"+idNegocio;
	}
	
	@GetMapping("/mesaOcupada/{idNegocio}/{idMesa}")
	public String mesaOcupada(@PathVariable(value = "idNegocio") Long idNegocio,@PathVariable(value="idMesa") Long idMesa, Model model) {
		
		Mesa mesa = mesaService.findById(idMesa);
		Usuario user = userService.findAllUsers().stream().filter(x->x.getMesa()!=null).filter(x->x.getMesa().getCodigo().equals(mesa.getCodigo())).findFirst().get();
		
		user.setPermisos("Aceptado");
		userService.save(usuarioActual());
		
		mesa.setNegocio(negocioService.findNegocioById(idNegocio));
		mesaService.actualizarEstado(mesa, idNegocio, Estado.OCUPADA);
		
		return "redirect:/mesas/"+idNegocio;
	}
	
	
	@GetMapping("/eliminarMesa/{idNegocio}/{idMesa}")
	public String eliminarMesa(@PathVariable(value = "idMesa") Long idMesa,
			@PathVariable(value = "idNegocio") Long idNegocio, Model model) {

		Mesa mesaAEliminar = mesaService.findById(idMesa);
		negocioService.findNegocioById(idNegocio).getMesas().remove(mesaAEliminar);
		mesaService.eliminarMesa(mesaAEliminar);
		
		return "redirect:/mesas/"+idNegocio;

	}
	
	@GetMapping("/solicitar/{idNegocio}/{idMesa}")
	public String solicitarMesa(@PathVariable(value = "idMesa") Long idMesa,
			@PathVariable(value = "idNegocio") Long idNegocio, Model model) {

		if(usuarioActual().getMesa()==null) {
		Mesa mesa = mesaService.findById(idMesa);
		mesaService.actualizarEstado(mesa, idNegocio, Estado.PENDIENTE);
		usuarioActual().setMesa(mesa);
		userService.save(usuarioActual());
		}else {
			return "redirect:/mesas/libres/"+idNegocio;
		}
		
		return "redirect:/listarProductos/"+idNegocio;

	}
	
	@RequestMapping("/pedido/{idNegocio}/{idMesa}")
	public String cuenta(@PathVariable(value = "idNegocio") Long idNegocio,@PathVariable(value = "idMesa") Long idMesa,
			Model model) {
		
		Pedido pedidoActivo = mesaService.findById(idMesa).getPedidos().stream().filter(x->x.getEstadoPedido().toString().equals("ACTIVO")).findFirst().orElse(null);
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
		
		model.addAttribute("nombreNegocio", negocioService.findNegocioById(idNegocio).getNombre());
		model.addAttribute("productosNoServidos", productosNoServidos);
		model.addAttribute("productosServidos",productosServidos);
		model.addAttribute("precioTotalNoServido", precioTotalNoServido);
		model.addAttribute("precioTotalServido", precioTotalServido);
		model.addAttribute("usuario", usuarioActual());
		model.addAttribute("idNegocio", idNegocio);
		model.addAttribute("idMesa", idMesa);
		


		return "negocio/cuenta";

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
