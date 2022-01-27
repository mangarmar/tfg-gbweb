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
import com.gbweb.entity.Usuario;

@Controller
public class UserController {
	
		
	@Autowired
	UserService userService;
	
	
	@GetMapping("/")
	public String index() {
		return "inicio/index";
	}
	
	
	@GetMapping("/crearUsuario")
	public String crearUsuario(Model model) {
		model.addAttribute("usuario", new Usuario());
		model.addAttribute("rol_usuario", ROL.CLIENTE);
		return "usuario/formularioUsuario";
	}
	
	
	
	@PostMapping("/crearUsuario")
	public String crearusuario(@Valid @ModelAttribute("usuario") Usuario usuario, BindingResult result, Model model
			,RedirectAttributes redirectAttributes) {


		if (result.hasErrors()) {
			model.addAttribute("usuario", usuario);
			userService.creaUsuario(usuario);
			return "usuario/formularioUsuario";
		} else {

			userService.creaUsuario(usuario);
			
		}
		redirectAttributes.addFlashAttribute("alert", 11);
		return "redirect:";

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
	
}
