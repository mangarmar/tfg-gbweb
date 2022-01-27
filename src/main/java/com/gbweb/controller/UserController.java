package com.gbweb.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gbweb.enums.ROL;
import com.gbweb.service.UserService;
import com.gbweb.entity.Negocio;
import com.gbweb.entity.Usuario;

@Controller
public class UserController {
	
		
	@Autowired
	UserService userService;
	
	
	@GetMapping("/")
	public String index() {
		return "inicio/index";
	}
	
	
	@GetMapping("/crearCliente")
	public String crearCliente(Model model) {
		model.addAttribute("cliente", new Usuario());
		return "cliente/formularioCliente";
	}
	
	
	@PostMapping("/crearCliente")
	public String nuevoCliente(@Valid @ModelAttribute("cliente") Usuario cliente, BindingResult result, Model model
			,RedirectAttributes redirectAttributes) {


		if (result.hasErrors()) {
			model.addAttribute("cliente", cliente);
			return "cliente/formularioCliente";
		} else {

			userService.creaCliente(cliente);
			
		}
		redirectAttributes.addFlashAttribute("alert", 11);
		return "redirect:";

	}
	
	@GetMapping("/crearGerente")
	public String crearGerente(Model model) {
		model.addAttribute("gerente", new Usuario());
		return "gerente/formularioGerente";
	}
	
	
	@PostMapping("/crearGerente")
	public String nuevoGerente(@Valid @ModelAttribute("gerente") Usuario gerente, BindingResult result, Model model
			,RedirectAttributes redirectAttributes) {
		
		if (result.hasErrors()) {
			model.addAttribute("gerente", gerente);
			return "gerente/formularioGerente";
		} else {
			
			
			userService.creaGerente(gerente);
			
		}

		redirectAttributes.addFlashAttribute("id",gerente.getId());
		return "redirect:/crearNegocio";

	}
	

	}


	
	
//	private Usuario usuarioActual() {
//	Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//	UserDetails userDetails = null;
//	Usuario user = null;
//	if (principal instanceof UserDetails) {
//		userDetails = (UserDetails) principal;
//		String userName = userDetails.getUsername();
//		user = this.userService.findByUsername(userName);
//	} else {
//		user = null;
//	}
//	return user;
//}


