package com.gbweb.service;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.client.RestTemplate;

import com.gbweb.entity.Localizacion;
import com.gbweb.entity.Mesa;
import com.gbweb.entity.Negocio;
import com.gbweb.entity.Producto;
import com.gbweb.entity.Usuario;
import com.gbweb.repository.NegocioRepository;

@SpringBootTest
public class NegocioServiceTest {
	
	@MockBean
	NegocioRepository negocioRepo;
	
	@Autowired
	NegocioService negocioService;
	
	@MockBean
    RestTemplate restTemplate;
	

	

	
//	@Test
//	@WithMockUser(username = "u")
//	void creaNegocio() {
//		Usuario u = new Usuario();
//		u.setUsername("u");
//		Localizacion l = new Localizacion();
//		l.setLat("1");
//		l.setLon("1");
//		u.setNegocios(new ArrayList<>());
//		
//		Negocio n = new Negocio();
//		n.setLatitud("1");
//		n.setLongitud("1");
//		n.setUsuario(u);
//		
//	
//		doReturn(n).when(negocioRepo).save(n);
//		
//		Negocio res = negocioService.creaNegocio(n);
//		
//		Assertions.assertEquals(res, n);
//	}
	
	@Test
	@DisplayName("Test get localización")
	void testGetLocalizacion() {
		Localizacion l = new Localizacion();
		l.setLat("1000");
		Localizacion[] locs = new Localizacion[1];
		locs[0] = l;
		ResponseEntity<Localizacion[]> response = ResponseEntity.ok(locs);
		
	 	doReturn(response).when(restTemplate).getForEntity(anyString(), same(Localizacion[].class));

	 	Localizacion loc = negocioService.getLocalizacion("Calle", "Numero", "Ciudad", "Provincia");

	 	Assertions.assertEquals("1000",loc.getLat(),"La localización no se ha encontrado");
	}
	
	@Test
	void save() {
		Negocio n = new Negocio();
		
		doReturn(n).when(negocioRepo).save(n);
		
		Negocio res = negocioService.save(n);
		
		Assertions.assertEquals(res, n);
	}
	
	@Test
	void findAll() {
		Negocio n = new Negocio();
		n.setId(1L);
		
		doReturn(Arrays.asList(n)).when(negocioRepo).findAll();
		
		List<Negocio> res = negocioService.findAll();
		
		Assertions.assertEquals(res.get(0).getId(), n.getId());
	}
	
	@Test
	void findNegocioById() {
		Negocio n = new Negocio();
		n.setId(1L);
		
		doReturn(Optional.of(n)).when(negocioRepo).findById(1L);
		
		Negocio res = negocioService.findNegocioById(1L);
		
		Assertions.assertEquals(res, n);
	}
	
	@Test
	void findNegociosByUserId() {
		Negocio n = new Negocio();
		Negocio n2 = new Negocio();
		
		Usuario u = new Usuario();
		Usuario u2 = new Usuario();
		
		u.setId(1L);
		u2.setId(2L);
		
		n.setId(1L);
		n.setUsuario(u);
		
		n2.setId(2L);
		n2.setUsuario(u2);
		
		
		
		doReturn(Arrays.asList(n,n2)).when(negocioRepo).findAll();
		
		System.out.println(n.getUsuario().getId());
		System.out.println(n2.getUsuario().getId());

		
		List<Negocio> res = negocioService.findNegociosByUserId(1L);
		
		Assertions.assertEquals(res.get(0), n);
		
	}
	
//	@Test
//	@WithMockUser
//	void editarNegocio() {
//		Negocio negocio = new Negocio();
//		negocio.setCalle("Calle Prim");
//		negocio.setNumero("24");
//		negocio.setCiudad("Carmona");
//		negocio.setProvincia("Sevilla");
//		negocio.setCapacidad(100);
//		negocio.setCif("A12345678");
//		negocio.setMesas(Arrays.asList(new Mesa()));
//		negocio.setUsuario(new Usuario());
//		negocio.setNombre("nombre");
//		negocio.setEmail("email@gmail.com");
//		
//		Negocio negocioAct = new Negocio();
//		negocioAct.setId(1L);
//		
//		
//		doReturn(Optional.of(negocioAct)).when(negocioRepo).findById(1L);
//		Negocio res = negocioService.editarNegocio(negocio, 1L);
//		
//		Assertions.assertEquals(negocio.getCalle(), "Calle Prim");
//
	//}

}
