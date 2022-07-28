package com.gbweb.controller;

import javax.validation.Valid;

import org.progressify.spring.annotations.StaleWhileRevalidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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

		if (result.hasErrors()) {
			model.addAttribute("cliente", cliente);
			return "cliente/formularioCliente";
		} else {

			userService.creaCliente(cliente);

		}
		redirectAttributes.addFlashAttribute("alert", 11);
		return "redirect:/login";

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

		if (result.hasErrors()) {
			model.addAttribute("gerente", gerente);
			return "gerente/formularioGerente";
		} else {

			userService.creaGerente(gerente);

		}
		return "redirect:/crearNegocio";

	}

}
