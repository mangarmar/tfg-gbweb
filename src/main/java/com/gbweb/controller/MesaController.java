package com.gbweb.controller;

import java.io.ByteArrayInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.gbweb.entity.LineaPedido;
import com.gbweb.entity.Mesa;
import com.gbweb.entity.Negocio;
import com.gbweb.entity.Pedido;
import com.gbweb.entity.Usuario;
import com.gbweb.enums.Estado;
import com.gbweb.enums.EstadoPedido;
import com.gbweb.enums.ROL;
import com.gbweb.service.LineaPedidoService;
import com.gbweb.service.MesaService;
import com.gbweb.service.NegocioService;
import com.gbweb.service.PedidoService;
import com.gbweb.service.UserService;
import com.gbweb.util.GeneratePdfReport;

@Controller
@RequestMapping("/mesas")
public class MesaController {
	
	private static final DecimalFormat df = new DecimalFormat("0.00");

	
	@Autowired
	LineaPedidoService lineaPedidoService;
	
	@Autowired
	NegocioService negocioService;
	
	@Autowired
	MesaService mesaService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	PedidoService pedidoService;
	
	

	
	@RequestMapping("/{idNegocio}")
	public String listarMesas(@PathVariable(value = "idNegocio") Long idNegocio, Model model) {
		Negocio negocio = negocioService.findNegocioById(idNegocio);
		List<Mesa> mesas = negocio.getMesas().stream().filter(x->x.getActiva().equals(true)).collect(Collectors.toList());
		Map<Mesa, Pedido> pedidosPorMesa = new LinkedHashMap<>();
		for(int i = 0;i<mesas.size();i++) {
			Pedido pedido = mesas.get(i).getPedidos().stream().filter(x->(x.getEstadoPedido().toString().equals("ACTIVO")) || x.getEstadoPedido().toString().equals("PENDIENTE_PAGO_TARJETA") || x.getEstadoPedido().toString().equals("PENDIENTE_PAGO_EFECTIVO")).findFirst().orElse(null);
			pedidosPorMesa.put(mesas.get(i), pedido);
		}
		
		model.addAttribute("nombreNegocio", negocio.getNombre());
		model.addAttribute("idNegocio", idNegocio);
		model.addAttribute("pedidosPorMesa", pedidosPorMesa);
		if(model.getAttribute("message")!=null) {
			model.addAttribute("message");
		}
		

		return "negocio/listarMesas";

	}
	
	@RequestMapping("/eliminarMesa/{idNegocio}")
	public String listarMesasParaEliminar(@PathVariable(value = "idNegocio") Long idNegocio, Model model) {
		Negocio negocio = negocioService.findNegocioById(idNegocio);
		List<Mesa> mesas = negocio.getMesas().stream().filter(x->x.getActiva().equals(true)).collect(Collectors.toList());
		
		model.addAttribute("nombreNegocio", negocio.getNombre());
		model.addAttribute("idNegocio", idNegocio);
		model.addAttribute("mesas", mesas);
		if(model.getAttribute("message")!=null) {
			model.addAttribute("message");
		}
		
		return "negocio/eliminarMesas";

	}
	
	@RequestMapping("/libres/{idNegocio}")
	public String listarMesasLibres(@PathVariable(value = "idNegocio") Long idNegocio, Model model) {
		Negocio negocio = negocioService.findNegocioById(idNegocio);
		List<Mesa> mesas = negocio.getMesas().stream().filter(x->x.getActiva().equals(true)).collect(Collectors.toList());
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
		Usuario usuario = userService.usuarioActual();
		Boolean ok = false;
		
		if(usuario.getMesa()!=null) {
			for(int i = 0;i<mesas.size();i++) {
				if(usuario.getMesa().getCodigo().equals(mesas.get(i).getCodigo())) {
					Pedido pedido = mesas.get(i).getPedidos().stream().filter(x->x.getEstadoPedido().toString().equals("ACTIVO")).findFirst().orElse(null);
					pedidosPorMesa.put(mesas.get(i), pedido);
				}
			}
			model.addAttribute("ok", ok);
			model.addAttribute("nombreNegocio", negocio.getNombre());
			model.addAttribute("idNegocio", idNegocio);
			model.addAttribute("pedidosPorMesa", pedidosPorMesa);
			model.addAttribute("usuario", usuario);
			
			
			return "negocio/listarMesas";

		}else {
			model.addAttribute("message", "Han rechazado tu solicitud, lo sentimos");
			return "comun/index";
		}
		



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
			if(!mesaService.añadirMesa(mesa, idNegocio)) {
				model.addAttribute("message", "Este código ya existe");
				return "negocio/añadirMesa";
			}else {
				mesaService.añadirMesa(mesa, idNegocio);
				return "redirect:/mesas/"+idNegocio;
			}
			

		}
	

	}
	
	@GetMapping("/mesaLibre/{idNegocio}/{idMesa}")
	public String mesaLibre(@PathVariable(value = "idNegocio") Long idNegocio,@PathVariable(value="idMesa") Long idMesa, Model model) {
		Mesa mesa = mesaService.findById(idMesa);
		Pedido pedidoActivo = mesa.getPedidos().stream().filter(x->x.getEstadoPedido().toString().equals("ACTIVO")).findFirst().orElse(null);	
		if(pedidoActivo!=null) {
			pedidoActivo.setEstadoPedido(EstadoPedido.CANCELADO);
			pedidoService.nuevoPedido(pedidoActivo);
		}	
		mesaService.save(mesa);
		mesaService.actualizarEstado(mesa, idNegocio, Estado.LIBRE);
		if(userService.usuarioActual().getRol().equals(ROL.GERENTE)) {
			Usuario usuario =  userService.findByMesa(mesa.getCodigo());
			usuario.setPermisos(null);
			usuario.setMesa(null);
			userService.save(usuario);
		}else {
			userService.usuarioActual().setPermisos(null);	
			userService.usuarioActual().setMesa(null);
			userService.save(userService.usuarioActual());
		}

		
		return "redirect:/mesas/"+idNegocio;
	}
	
	@GetMapping("/mesaOcupada/{idNegocio}/{idMesa}")
	public String mesaOcupada(@PathVariable(value = "idNegocio") Long idNegocio,@PathVariable(value="idMesa") Long idMesa, Model model) {
		
		Mesa mesa = mesaService.findById(idMesa);
		Usuario user = userService.findAllUsers().stream().filter(x->x.getMesa()!=null).filter(x->x.getMesa().getCodigo().equals(mesa.getCodigo())).findFirst().get();
		
		user.setPermisos("Aceptado");
		userService.save(userService.usuarioActual());
		
		mesa.setNegocio(negocioService.findNegocioById(idNegocio));
		mesaService.actualizarEstado(mesa, idNegocio, Estado.OCUPADA);
		
		return "redirect:/mesas/"+idNegocio;
	}
	
	
	@GetMapping("/eliminarMesa/{idNegocio}/{idMesa}")
	public String eliminarMesa(@PathVariable(value = "idMesa") Long idMesa,
			@PathVariable(value = "idNegocio") Long idNegocio, Model model) {

		
		Mesa mesaAEliminar = mesaService.findById(idMesa);
		Pedido pedidoActivo = mesaAEliminar.getPedidos().stream().filter(x->x.getEstadoPedido().toString().equals("ACTIVO")).findFirst().orElse(null);	
		
		if(pedidoActivo==null) {
			mesaAEliminar.setActiva(false);
			mesaService.save(mesaAEliminar);
			return "redirect:/mesas/eliminarMesa/"+idNegocio;
		}else {
			model.addAttribute("message", "No se pueden borrar mesas con pedidos activos");
			return listarMesasParaEliminar(idNegocio, model);
		}
		

	}
	
	@GetMapping("/solicitar/{idNegocio}/{idMesa}")
	public String solicitarMesa(@PathVariable(value = "idMesa") Long idMesa,
			@PathVariable(value = "idNegocio") Long idNegocio, Model model) {

		if(userService.usuarioActual().getMesa()==null) {
		Mesa mesa = mesaService.findById(idMesa);
		mesaService.actualizarEstado(mesa, idNegocio, Estado.PENDIENTE);
		userService.usuarioActual().setMesa(mesa);
		userService.save(userService.usuarioActual());
		}else {
			return "redirect:/mesas/libres/"+idNegocio;
		}
		
		return "redirect:/mesas/estado/"+idNegocio;

	}
	
	@RequestMapping("/pedido/{idNegocio}/{idMesa}")
	public String cuenta(@PathVariable(value = "idNegocio") Long idNegocio,@PathVariable(value = "idMesa") Long idMesa,
			Model model) {
		Boolean ok = false;
		Pedido pedidoActivo = mesaService.findById(idMesa).getPedidos().stream().filter(x->(x.getEstadoPedido().toString().equals("ACTIVO")) || x.getEstadoPedido().toString().equals("PENDIENTE_PAGO")).findFirst().orElse(null);
		if(pedidoActivo!=null) {
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
		model.addAttribute("ok", true);
		model.addAttribute("nombreNegocio", negocioService.findNegocioById(idNegocio).getNombre());
		model.addAttribute("productosNoServidos", productosNoServidos);
		model.addAttribute("productosServidos",productosServidos);
		model.addAttribute("precioTotalNoServido", df.format(precioTotalNoServido));
		model.addAttribute("precioTotal", df.format(precioTotalServido));
		model.addAttribute("usuario", userService.usuarioActual());
		model.addAttribute("idNegocio", idNegocio);
		model.addAttribute("idMesa", idMesa);

		return "negocio/cuenta";
		}else {
			model.addAttribute("ok",ok);	
			return "redirect:/mesas/"+idNegocio;
			}

	}

	 @RequestMapping(value = "/cuenta/{idPedido}", method = RequestMethod.GET,
	            produces = MediaType.APPLICATION_PDF_VALUE)
	    public ResponseEntity<InputStreamResource> cuentaPDF(@PathVariable(value = "idPedido") Long idPedido, Model model) {
		 	
		 	Pedido pedidoActivo = pedidoService.findById(idPedido);
		 	List<LineaPedido> lineaPedidos = pedidoActivo.getLineaPedidos();
			List<LineaPedido> productosServidos = new ArrayList<LineaPedido>();
			Double precioTotalServido = 0.0;
			for(int i = 0; i<lineaPedidos.size();i++){
				LineaPedido lp = lineaPedidos.get(i);
				if(lp.getServido()!=null) {
					 if(lp.getServido()==true) {
						precioTotalServido+=lineaPedidos.get(i).getPrecio()*lineaPedidos.get(i).getCantidad();
						productosServidos.add(lp);
					}
				}
			
			}
			
			Negocio negocio = pedidoActivo.getMesa().getNegocio();
			Mesa mesa = pedidoActivo.getMesa();
	        ByteArrayInputStream bis = GeneratePdfReport.cuentaPDF(productosServidos, negocio, mesa, precioTotalServido);

	        HttpHeaders headers = new HttpHeaders();
	        headers.add("Content-Disposition", "inline; filename=cuenta.pdf");

			 return ResponseEntity
		                .ok()
		                .headers(headers)
		                .contentType(MediaType.APPLICATION_PDF)
		                .body(new InputStreamResource(bis));
	    }
	 
	 @RequestMapping(value = "/factura/{idPedido}", method = RequestMethod.GET,
	            produces = MediaType.APPLICATION_PDF_VALUE)
	    public ResponseEntity<InputStreamResource> descargarFactura(@PathVariable(value = "idPedido") Long idPedido, Model model) {
		 
		Pedido pedidoActivo = pedidoService.findById(idPedido);
		 List<LineaPedido> lineaPedidos = pedidoActivo.getLineaPedidos();
			List<LineaPedido> productosServidos = new ArrayList<LineaPedido>();
			Double precioTotalServido = 0.0;
			for(int i = 0; i<lineaPedidos.size();i++){
				LineaPedido lp = lineaPedidos.get(i);
				if(lp.getServido()!=null) {
					 if(lp.getServido()==true) {
						precioTotalServido+=lineaPedidos.get(i).getPrecio()*lineaPedidos.get(i).getCantidad();
						productosServidos.add(lp);
					}
				}
			
			}
			
			Negocio negocio = pedidoActivo.getMesa().getNegocio();
			Mesa mesa = pedidoActivo.getMesa();
	        ByteArrayInputStream bis = GeneratePdfReport.facturaPDF(pedidoActivo, productosServidos, negocio, mesa, precioTotalServido);

	        HttpHeaders headers = new HttpHeaders();
	        headers.add("Content-Disposition", "inline; filename=cuenta.pdf");

			 return ResponseEntity
		                .ok()
		                .headers(headers)
		                .contentType(MediaType.APPLICATION_PDF)
		                .body(new InputStreamResource(bis));
	    }

	 
	 @GetMapping("/solicitar/cuenta/tarjeta/{idMesa}/{idPedido}")
	    public String solicitarCuentaTarjeta(@PathVariable(value = "idMesa") Long idMesa,@PathVariable(value = "idPedido") Long idPedido, Model model) {
		 Mesa mesa = mesaService.findById(idMesa);
		 Pedido pedido = pedidoService.findById(idPedido);
		 pedido.setEstadoPedido(EstadoPedido.PENDIENTE_PAGO_TARJETA);
		 pedidoService.nuevoPedido(pedido);
		 userService.usuarioActual().setPermisos(null);
		 userService.usuarioActual().setMesa(null);
		 userService.save(userService.usuarioActual());
//		 mesaService.actualizarEstado(mesa, mesa.getNegocio().getId(), Estado.LIBRE);
		 model.addAttribute("idMesa", idMesa);
		 model.addAttribute("pedido", pedido);
		 model.addAttribute("idPedido", idPedido);
		 
		return "redirect:/mesas/espera/cuenta/"+idMesa+"/"+idPedido;
		 
	    }
	 
	 @GetMapping("/solicitar/cuenta/efectivo/{idMesa}/{idPedido}")
	    public String solicitarCuentaEfectivo(@PathVariable(value = "idMesa") Long idMesa,@PathVariable(value = "idPedido") Long idPedido, Model model) {
		 Mesa mesa = mesaService.findById(idMesa);
		 Pedido pedido = pedidoService.findById(idPedido);
		 pedido.setEstadoPedido(EstadoPedido.PENDIENTE_PAGO_EFECTIVO);
		 pedidoService.nuevoPedido(pedido);
		 userService.usuarioActual().setPermisos(null);
		 userService.usuarioActual().setMesa(null);
		 userService.save(userService.usuarioActual());
//		 mesaService.actualizarEstado(mesa, mesa.getNegocio().getId(), Estado.LIBRE);
		 model.addAttribute("idMesa", idMesa);
		 model.addAttribute("pedido", pedido);
		 model.addAttribute("idPedido", idPedido);
		 
		return "redirect:/mesas/espera/cuenta/"+idMesa+"/"+idPedido;
		 
	    }
	 
	 @GetMapping("/espera/cuenta/{idMesa}/{idPedido}")
	    public String solicitarCuentaEspera(@PathVariable(value = "idMesa") Long idMesa,@PathVariable(value = "idPedido") Long idPedido, Model model) {
		 Pedido pedido = pedidoService.findById(idPedido);

		 model.addAttribute("idMesa", idMesa);
		 model.addAttribute("pedido", pedido);
		 model.addAttribute("idPedido", idPedido);
		 
		return "negocio/finPedido";
		
	 }
	 
	 @GetMapping("/cancelar/cuenta/{idMesa}/{idPedido}")
	    public String cancelarCuenta(@PathVariable(value = "idMesa") Long idMesa,@PathVariable(value = "idPedido") Long idPedido, Model model) {
		 Pedido pedido = pedidoService.findById(idPedido);
		 Mesa mesa = mesaService.findById(idMesa);
		 pedido.setEstadoPedido(EstadoPedido.ACTIVO);
		 pedidoService.nuevoPedido(pedido);
		 userService.usuarioActual().setPermisos("Aceptado");
		 userService.usuarioActual().setMesa(mesa);
		 userService.save(userService.usuarioActual());
		 mesaService.actualizarEstado(mesa, mesa.getNegocio().getId(), Estado.OCUPADA);
		 model.addAttribute("idMesa", idMesa);
		 model.addAttribute("idPedido", idPedido);
		 
		return "redirect:/pedir/"+mesa.getNegocio().getId()+"/mesa/"+idMesa;
		 
	    }
	 
	 @GetMapping("/pago/cuenta/{idMesa}/{idPedido}")
	    public String pagoCuenta(@PathVariable(value = "idMesa") Long idMesa,@PathVariable(value = "idPedido") Long idPedido, Model model) {
		 Mesa mesa = mesaService.findById(idMesa);
		 Pedido pedido = pedidoService.findById(idPedido);
		 pedido.setEstadoPedido(EstadoPedido.PAGADO);
		 mesaService.actualizarEstado(mesa, mesa.getNegocio().getId(), Estado.LIBRE);
		 model.addAttribute("idMesa", idMesa);
		 model.addAttribute("idPedido", idPedido);
		 
		return "redirect:/mesas/"+mesa.getNegocio().getId();
		 
	    }

	 
	


}
