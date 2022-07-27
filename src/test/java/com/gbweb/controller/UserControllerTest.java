package com.gbweb.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.gbweb.entity.Usuario;
import com.gbweb.service.LineaPedidoService;
import com.gbweb.service.MesaService;
import com.gbweb.service.NegocioService;
import com.gbweb.service.PedidoService;
import com.gbweb.service.ProductoService;
import com.gbweb.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
	
	@Autowired
	protected UserController userController;
	
    @Autowired  
    private MockMvc mockMvc;
    
    @MockBean
    private UserService userService;
    
    @Test
    void contextLoads() throws Exception {
        assertThat(userController).isNotNull();
    }
    
    @WithMockUser
    @Test
    void index() throws Exception {
    	
    	mockMvc.perform(get("/")).andExpect(status().isOk())
         .andExpect(view().name("negocio/listaNegociosClientes"));
    }
    
    @WithMockUser
    @Test
    void crearCliente() throws Exception {
    	
    	mockMvc.perform(get("/crearCliente")).andExpect(status().isOk())
         .andExpect(view().name("cliente/formularioCliente"));
    }
    
    @WithMockUser
    @Test
    void crearGerente() throws Exception {
    	
    	Usuario u = new Usuario();
    	
    	doReturn(u).when(userService).usuarioActual();
    	
    	mockMvc.perform(get("/crearGerente")).andExpect(status().isOk())
         .andExpect(view().name("gerente/formularioGerente"));
    }
}
