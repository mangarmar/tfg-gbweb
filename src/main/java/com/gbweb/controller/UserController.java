package com.gbweb.controller;

import java.util.List;

import javax.validation.Valid;

import org.progressify.spring.annotations.StaleWhileRevalidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import com.gbweb.service.UserService;
import com.gbweb.entity.Usuario;

@Controller
public class UserController {



	@Autowired
	UserService userService;

	@StaleWhileRevalidate
	@GetMapping("/")
	public String index() {
		return "comun/index";	
	}

	@GetMapping("/crearCliente")
	public String crearCliente(Model model) {
		model.addAttribute("cliente", new Usuario());
		return "cliente/formularioCliente";
	}

	@PostMapping("/crearCliente")
	public String nuevoCliente(@Valid @ModelAttribute("cliente") Usuario cliente, BindingResult result, Model model,
			RedirectAttributes redirectAttributes) {
		
		List<Usuario> usuarios = userService.findAllUsers();
		Boolean existeDNI = usuarios.stream().anyMatch(x->x.getDni().equals(cliente.getDni()));
		Boolean existeUsername = usuarios.stream().anyMatch(x->x.getUsername().equals(cliente.getUsername()));
		Boolean existeEmail = usuarios.stream().anyMatch(x->x.getEmail().equals(cliente.getEmail()));
		
		if(existeDNI) {	
			result.addError(new FieldError("cliente", "dni", "El DNI introducido ya existe"));
		}
		if(existeUsername) {
			result.addError(new FieldError("cliente", "username", "El nombre de usuario introducido ya existe"));
		}
		if(existeEmail) {
			result.addError(new FieldError("cliente", "email","El email introducido ya existe"));
		}
		
		if (result.hasErrors()) {
			System.out.println(result.getAllErrors());
			model.addAttribute("cliente", cliente);
			return "cliente/formularioCliente";
		} else {
			
				userService.creaCliente(cliente);
				redirectAttributes.addFlashAttribute("alert", 11);
				return "redirect:/login";
			}


	}

	@GetMapping("/crearGerente")
	public String crearGerente(Model model) {
		model.addAttribute("actual", userService.usuarioActual());
		model.addAttribute("gerente", new Usuario());
		return "gerente/formularioGerente";
	}

	@PostMapping("/crearGerente")
	public String nuevoGerente(@Valid @ModelAttribute("gerente") Usuario gerente, BindingResult result, Model model,
			RedirectAttributes redirectAttributes) {

		List<Usuario> usuarios = userService.findAllUsers();
		Boolean existeDNI = usuarios.stream().anyMatch(x->x.getDni().equals(gerente.getDni()));
		Boolean existeUsername = usuarios.stream().anyMatch(x->x.getUsername().equals(gerente.getUsername()));
		Boolean existeEmail = usuarios.stream().anyMatch(x->x.getEmail().equals(gerente.getEmail()));
		
		if(existeDNI) {	
			result.addError(new FieldError("gerente", "dni", "El DNI introducido ya existe"));
		}
		if(existeUsername) {
			result.addError(new FieldError("gerente", "username", "El nombre de usuario introducido ya existe"));
		}
		if(existeEmail) {
			result.addError(new FieldError("gerente", "email","El email introducido ya existe"));
		}
		
		if (result.hasErrors()) {
			System.out.println(result.getAllErrors());
			model.addAttribute("gerente", gerente);
			return "gerente/formularioGerente";
		} else {
			
				userService.creaGerente(gerente);
				redirectAttributes.addFlashAttribute("alert", 11);
				return "redirect:/login";
			}


	}

}
