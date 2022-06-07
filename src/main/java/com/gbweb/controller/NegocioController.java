package com.gbweb.controller;

import java.util.List;

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

import com.gbweb.entity.Negocio;
import com.gbweb.entity.Producto;
import com.gbweb.entity.Usuario;
import com.gbweb.enums.ROL;
import com.gbweb.service.NegocioService;
import com.gbweb.service.UserService;



@Controller
public class NegocioController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	NegocioService negocioService;
	
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
		return "redirect:";


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
	
//	@GetMapping()
//	public String listarNegociosCliente(Model model) {
//		List<Negocio> negocios = negocioService.findAll();
//		System.out.println("=========================================================================");
//		System.out.println(negocios);
//		model.addAttribute("negocios", negocioService.findAll());
//		
//		return "negocio/listaNegocios";
//		
//	}
	
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
