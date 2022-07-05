package com.gbweb.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gbweb.entity.Localizacion;
import com.gbweb.entity.Mesa;
import com.gbweb.entity.Negocio;
import com.gbweb.entity.Usuario;
import com.gbweb.enums.Estado;
import com.gbweb.enums.ROL;
import com.gbweb.service.MesaService;
import com.gbweb.service.NegocioService;
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
			
			negocioService.creaNegocio(negocio);
		
		}
		redirectAttributes.addFlashAttribute("alert", 11);
		return "redirect:/listarNegocios";


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

			negocioService.editarNegocio(negocio, idNegocio);

		}
		return listarNegocios(model);

	}
	
	@GetMapping("/listarNegocios")
	public String listarNegocios(Model model) {
		
		if(usuarioActual()==null) {
			List<Negocio> negocios = negocioService.findAll();
			model.addAttribute("negocios", negocios);
			return "negocio/listaNegociosClientes";
		}else if(usuarioActual().getRol().equals(ROL.CLIENTE) || usuarioActual().getRol().equals(ROL.ADMIN)) {
			List<Negocio> negocios = negocioService.findAll();
			model.addAttribute("negocios", negocios);
			return "negocio/listaNegociosClientes";
		}else{
			List<Negocio> negociosPorUsuario = negocioService.findNegociosByUserId(usuarioActual().getId());
			model.addAttribute("negocios", negociosPorUsuario);
			return "negocio/listaNegocios";
		}
			
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
	
//	@GetMapping("/confirmarMesa/{idNegocio}")
//	public String confirmarMesa(@PathVariable(value = "idNegocio") Long idNegocio, Model model) {
//		model.addAttribute("idNegocio", idNegocio);
//		model.addAttribute("mesa", new Mesa());
//		return "negocio/añadirMesa";
//	}
//	
//	@PostMapping("/confirmarMesa/{idNegocio}")
//	public String confirmarMesa(@PathVariable(value = "idNegocio") Long idNegocio,
//			@Valid @ModelAttribute("mesa") Mesa mesa, BindingResult result, Model model) {
//		
//		List<String> mesasDelRestaurante = negocioService.findNegocioById(idNegocio).getMesas().stream().map(x->x.getCodigo()).collect(Collectors.toList());
//
//		if (!mesasDelRestaurante.contains(mesa.getCodigo())) {
//			model.addAttribute("mesa", mesa);
//			model.addAttribute("message", "Esta mesa no pertenece al restaurante");
//			return "negocio/añadirMesa";
//		} else {
//			Authentication auth = SecurityContextHolder.getContext().getAuthentication();	
//			Set<GrantedAuthority> authorities = new HashSet<>();
//			authorities.add(new SimpleGrantedAuthority("CLIENTE_CONFIRMADO"));
//			Authentication reAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(),auth.getCredentials(),authorities);  
//			SecurityContextHolder.getContext().setAuthentication(reAuth);
//			mesa.setEstado(Estado.PENDIENTE);
//			mesa.setNegocio(negocioService.findNegocioById(idNegocio));
//			mesaService.actualizarEstado(mesa);
//			
//			return "redirect:/listarProductos/"+idNegocio+"/mesa/"+mesa.getCodigo();
//		}
//		
//
//	}
	
//	@GetMapping("/confirmarMesa/{idNegocio}")
//	public String confirmarMesa(@PathVariable(value = "idNegocio") Long idNegocio, Model model) {
//		model.addAttribute("idNegocio", idNegocio);
//		model.addAttribute("mesa", new Mesa());
//		return "negocio/añadirMesa";
//	}
//	
//	@PostMapping("/confirmarMesa/{idNegocio}")
//	public String confirmarMesa(@PathVariable(value = "idNegocio") Long idNegocio,
//			@Valid @ModelAttribute("mesa") Mesa mesa, BindingResult result, Model model) {
//		
//		List<String> mesasDelRestaurante = negocioService.findNegocioById(idNegocio).getMesas().stream().map(x->x.getCodigo()).collect(Collectors.toList());
//
//		if (!mesasDelRestaurante.contains(mesa.getCodigo())) {
//			model.addAttribute("mesa", mesa);
//			model.addAttribute("message", "Esta mesa no pertenece al restaurante");
//			return "negocio/añadirMesa";
//		} else {
//			Authentication auth = SecurityContextHolder.getContext().getAuthentication();	
//			Set<GrantedAuthority> authorities = new HashSet<>();
//			authorities.add(new SimpleGrantedAuthority("CLIENTE_CONFIRMADO"));
//			Authentication reAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(),auth.getCredentials(),authorities);  
//			SecurityContextHolder.getContext().setAuthentication(reAuth);
//			mesa.setEstado(Estado.PENDIENTE);
//			mesa.setNegocio(negocioService.findNegocioById(idNegocio));
//			mesaService.actualizarEstado(mesa);
//			
//			return "redirect:/listarProductos/"+idNegocio+"/mesa/"+mesa.getCodigo();
//		}
//		
//
//	}
	
	
	
	@RequestMapping("/salir/{idNegocio}")
	public String salir(@PathVariable(value = "idNegocio") Long idNegocio, Model model) {
	
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();	
		Set<GrantedAuthority> authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority("CLIENTE"));
		Authentication reAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(),auth.getCredentials(),authorities);  
		SecurityContextHolder.getContext().setAuthentication(reAuth);
	
		return "redirect:/listarProductos/"+idNegocio;
	}
}
