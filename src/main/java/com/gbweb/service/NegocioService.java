package com.gbweb.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.gbweb.entity.Localizacion;
import com.gbweb.entity.Negocio;
import com.gbweb.entity.Usuario;
import com.gbweb.repository.NegocioRepository;
import com.gbweb.repository.UserRepository;
import com.itextpdf.text.log.SysoCounter;

@Service
public class NegocioService {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired 
	NegocioRepository negocioRepo;
	
	@Autowired
	UserService userService;
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
	    return builder.build();
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
	
	public void creaNegocio(@Valid Negocio negocio) {
		
		usuarioActual().getNegocios().add(negocio);
		negocio.setUsuario(usuarioActual());
		Localizacion localizacion = getLocalizacion(negocio.getCalle(), negocio.getNumero(), negocio.getCiudad(), negocio.getProvincia());
		negocio.setLatitud(localizacion.getLat());
		negocio.setLongitud(localizacion.getLon());
		negocioRepo.save(negocio);
		
	}
	
	public void editarNegocio(@Valid Negocio negocio, Long idNegocio) {
		
		Negocio negocioAct = findNegocioById(idNegocio);
		negocioAct.setCapacidad(negocio.getCapacidad());
		negocioAct.setCif(negocio.getCif());
		negocioAct.setDescripcion(negocio.getDescripcion());
		negocioAct.setEmail(negocio.getEmail());
		negocioAct.setCalle(negocio.getCalle());
		negocioAct.setNumero(negocio.getNumero());
		negocioAct.setCiudad(negocio.getCiudad());
		negocioAct.setProvincia(negocio.getProvincia());
		negocioAct.setNombre(negocio.getNombre());

		Localizacion localizacion = getLocalizacion(negocio.getCalle(), negocio.getNumero(), negocio.getCiudad(), negocio.getProvincia());
		System.out.println(localizacion.getLat());
		negocioAct.setLatitud(localizacion.getLat());
        negocioAct.setLongitud(localizacion.getLon());
		
		negocioAct.setTipo(negocio.getTipo());
		negocioAct.setUsuario(usuarioActual());
		negocioRepo.save(negocioAct);
		
	}

	public List<Negocio> findNegociosByUserId(Long id){
		List<Negocio> negocios  = (List<Negocio>) this.negocioRepo.findAll();
		List<Negocio> negociosPorUsuario = negocios.stream().filter(x->x.getUsuario().getId().equals(id)).collect(Collectors.toList());
		return negociosPorUsuario;
		
	}
	
	public Negocio findNegocioById(Long idNegocio) {
		return negocioRepo.findById(idNegocio).get();
	}
	
	public List<Negocio> findAll(){
		return negocioRepo.findAll();
	}

	
    public Localizacion getLocalizacion(String calle, String numero, String ciudad, String provincia){
        
        String direccion = calle + "," + numero + "," + ciudad + "," + provincia;
        System.out.println(direccion);
        ResponseEntity<Localizacion[]> response = restTemplate.getForEntity("https://geocode.maps.co/search?q=" + direccion, Localizacion[].class);
        Localizacion[] localizaciones = response.getBody();
        List<Localizacion> l = Arrays.asList(localizaciones);
        Localizacion localizacion = l.get(0);    
        
    return  localizacion;
}
    
}