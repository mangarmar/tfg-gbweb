package com.gbweb.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gbweb.entity.Negocio;
import com.gbweb.service.NegocioService;

@Controller
public class NegocioController {
	
	@Autowired
	NegocioService negocioService;
	
	@GetMapping("/crearNegocio")
	public String crearNegocio(@ModelAttribute("id") String id, Model model) {
		model.addAttribute("negocio", new Negocio());
		model.addAttribute("id", id);
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
}
