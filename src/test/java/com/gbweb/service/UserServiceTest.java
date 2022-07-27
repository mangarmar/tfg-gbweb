package com.gbweb.service;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Rule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.gbweb.entity.Mesa;
import com.gbweb.entity.Usuario;
import com.gbweb.enums.ROL;
import com.gbweb.repository.UserRepository;

import junit.framework.Assert;

@SpringBootTest
public class UserServiceTest {
	
	@MockBean
	UserRepository userRepo;
	
	@Autowired
	UserService userService;
	
	@Test
	void findAllUsers() {
		Usuario u = new Usuario();
        Usuario u2 = new Usuario();	

		doReturn(Arrays.asList(u,u2)).when(userRepo).findAll();

		List<Usuario> res = userService.findAllUsers();

		Assertions.assertEquals(2, res.size(),"El tamaño de la lista devuelta debe ser 2");
	}
	
	@Test
	void findAllUsuarios() {
		Usuario u = new Usuario();
        Usuario u2 = new Usuario();	

		doReturn(Arrays.asList(u,u2)).when(userRepo).findAll();

		List<Usuario> res = userService.findAllUsuarios();

		Assertions.assertEquals(2, res.size(),"El tamaño de la lista devuelta debe ser 2");
	}
	
	@Test
	void save() {
		Usuario u = new Usuario();
		
		doReturn(u).when(userRepo).save(u);
		
		Usuario res = userService.save(u);
		
		Assertions.assertEquals(res, u, "El usuario no se ha creado"); 		
	}
	
	@Test
	void saveNegativo() {
		Usuario u = new Usuario();
		
		doReturn(u).when(userRepo).save(null);
		
		Usuario res = userService.save(u);
		
		Assertions.assertNotEquals(res, u, "El usuario se ha creado"); 		
	}
	
	@Test
	void findById() {
		Usuario u = new Usuario();
		u.setId(1L);
		
		doReturn(Optional.of(u)).when(userRepo).findById(1L);
		Usuario res = userService.findById(1L);
		
		Assertions.assertEquals(res, u, "No hay ningun usuario con esa ID"); 		

	}
	
	@Test
	void crearCliente() {
		Usuario u = new Usuario();
		u.setPassword("hola");
		u.setNegocios(null);
		u.setRol(ROL.CLIENTE);
		
		doReturn(u).when(userRepo).save(u);
		
		Usuario res = userService.creaCliente(u);
		
		Assertions.assertEquals(res, u, "El usuario cliente no se ha creado"); 		

		
	}
	
	@Test
	void crearGerente() {
		Usuario u = new Usuario();
		u.setPassword("hola");
		u.setRol(ROL.GERENTE);
		
		doReturn(u).when(userRepo).save(u);
		
		Usuario res = userService.creaGerente(u);
		
		Assertions.assertEquals(res, u, "El usuario gerente no se ha creado"); 		
	}
	
	@Test
	void findByUsername() {
		Usuario u = new Usuario();
		u.setNombre("nombre");
		
		doReturn(u).when(userRepo).findByUsername("nombre");
		
		Usuario res = userService.findByUsername("nombre");
		
		Assertions.assertEquals(res, u, "El usuario no se ha encontrado"); 		
	}
	
	@Test
	void findByUsernameNegativo() {
		Usuario u = new Usuario();
		u.setNombre("nombre");
		
		doReturn(u).when(userRepo).findByUsername("nombre");
		
		Usuario res = userService.findByUsername("nombresssssss");
		
		Assertions.assertNotEquals(res, u, "El usuario se ha encontrado"); 		
	}
	
	@Test
	void loadByUsername() {
		Usuario u = new Usuario();
		u.setNombre("nombre");
		
		doReturn(u).when(userRepo).findByUsername("nombre");
		
		Usuario res = (Usuario) userService.loadUserByUsername("nombre");
		
		Assertions.assertEquals(res, u, "El usuario no se ha encontrado"); 		
	}
	
	@Test
	void loadByUsernameNegativo() {
		Usuario u = new Usuario();
		u.setNombre("nombre");
		
		doReturn(u).when(userRepo).findByUsername("nombre");
		
		assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("nombress"));
		
		
	}
	
	@Test
	void findByMesa() {
		Usuario u = new Usuario();
		Mesa m = new Mesa();
		m.setCodigo("AAA");
		u.setId(1L);
		u.setMesa(m);
		
		doReturn(Optional.of(u)).when(userRepo).findById(1L);
		when(userRepo.findAll()).thenReturn(Arrays.asList(u));
		Usuario res = userService.findByMesa("AAA");
		
		Assertions.assertEquals(res, u, "No hay ningun usuario con esa mesa asignada"); 		

	}
	
	@Test
	void findByMesaNegativo() {
		Usuario u = new Usuario();
		Mesa m = new Mesa();
		m.setCodigo("AAA");
		u.setId(1L);
		u.setMesa(m);
		
		doReturn(Optional.of(u)).when(userRepo).findById(1L);
		when(userRepo.findAll()).thenReturn(Arrays.asList(u));
		
		assertThrows(IndexOutOfBoundsException.class, () ->userService.findByMesa("AA"));

	}
	
	
	
	
	
}
