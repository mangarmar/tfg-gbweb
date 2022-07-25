package com.gbweb.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.gbweb.entity.Negocio;
import com.gbweb.service.NegocioService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class NegocioAPIControllerTest {
	
	@Autowired
	protected NegocioAPIController negocioAPIController;
	
    @Autowired  
    private MockMvc mockMvc;
	
    @MockBean
    private NegocioService negocioService;
    
    @Test
    void contextLoads() throws Exception {
        assertThat(negocioAPIController).isNotNull();
    }
    
    @WithMockUser
    @Test
    void getNegocios() throws Exception{
    	Negocio n = new Negocio();
    	
    	doReturn(Arrays.asList(n)).when(negocioService).findAll();
    	
		mockMvc.perform(get("/api/negocios")).andExpect(status().isOk())
		.andExpect(jsonPath("$.size()", Matchers.is(1)));
    }

}
