package com.gbweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {
	
	
	@GetMapping("/login")
	public String login() {
		return "inicio/login";
	}
	
	@GetMapping("/loginFailure")
	public String loginFailure(Model model) {
		model.addAttribute("message", "El usuario o la contraseña son incorrectos, pruebe de nuevo");		
		return "inicio/login";
	}
	

}