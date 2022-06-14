package com.gbweb.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.gbweb.entity.Mesa;
import com.gbweb.entity.Negocio;
import com.gbweb.entity.Producto;
import com.gbweb.enums.Estado;
import com.gbweb.service.MesaService;
import com.gbweb.service.NegocioService;

@Controller
@RequestMapping("/mesas")
public class MesaController {
	
	@Autowired
	NegocioService negocioService;
	
	@Autowired
	MesaService mesaService;
	
	
	@RequestMapping("/{idNegocio}")
	public String listarMesas(@PathVariable(value = "idNegocio") Long idNegocio, Model model) {
		Negocio negocio = negocioService.findNegocioById(idNegocio);
		List<Mesa> mesas = negocio.getMesas();
		
		model.addAttribute("nombreNegocio", negocio.getNombre());
		model.addAttribute("idNegocio", idNegocio);
		model.addAttribute("mesas", mesas);

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
		mesaService.actualizarEstado(mesa, idNegocio, Estado.OCUPADA);
		
		return "redirect:/mesas/"+idNegocio;
	}
	
	@GetMapping("/mesaPendiente/{idNegocio}/{idMesa}")
	public String mesaPendiente(@PathVariable(value = "idNegocio") Long idNegocio,@PathVariable(value="idMesa") Long idMesa, Model model) {
					
		Mesa mesa = mesaService.findById(idMesa);
		mesaService.actualizarEstado(mesa, idNegocio, Estado.PENDIENTE);
		
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

}
