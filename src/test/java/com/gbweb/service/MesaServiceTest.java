package com.gbweb.service;

import static org.mockito.Mockito.doReturn;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.gbweb.entity.Mesa;
import com.gbweb.entity.Negocio;
import com.gbweb.entity.Usuario;
import com.gbweb.enums.Estado;
import com.gbweb.repository.MesaRepository;
import com.gbweb.repository.NegocioRepository;
import com.gbweb.repository.UserRepository;

@SpringBootTest
public class MesaServiceTest {
	
	@MockBean
	MesaRepository mesaRepo;
	
	@Autowired
	MesaService mesaService;
	
	@MockBean
	NegocioService negocioService;
	
	@Test
	void save() {
		Mesa m = new Mesa();
		
		doReturn(m).when(mesaRepo).save(m);
		
		Mesa res = mesaService.save(m);
		
		Assertions.assertEquals(res, m, "La mesa no se ha creado"); 		
	}
	
	@Test
	void findById() {
		Mesa m = new Mesa();
		m.setId(1L);
		
		doReturn(Optional.of(m)).when(mesaRepo).findById(1L);
		Mesa res = mesaService.findById(1L);
		
		Assertions.assertEquals(res, m, "No hay ninguna mesa con esa ID"); 		

	}
	
	@Test
	void actualizarEstado() {
		
		Mesa m = new Mesa();
		m.setEstado(Estado.LIBRE);
		
		mesaService.actualizarEstado(m, 1L, Estado.OCUPADA);
		
		Assertions.assertEquals(m.getEstado().toString(), "OCUPADA");
		
	}
	
	@Test
	void añadirMesa() {
		Mesa m = new Mesa();
		
		Negocio n = new Negocio();
		n.setId(1L);
		n.setMesas(new ArrayList<>());
		
		doReturn(n).when(negocioService).findNegocioById(1L);

		Integer numMesas = n.getMesas().size();
		mesaService.añadirMesa(m, 1L);
		Assertions.assertNotEquals(numMesas, n.getMesas());

	}

}
