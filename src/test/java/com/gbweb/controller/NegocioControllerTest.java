package com.gbweb.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.gbweb.entity.LineaPedido;
import com.gbweb.entity.Mesa;
import com.gbweb.entity.Negocio;
import com.gbweb.entity.Pedido;
import com.gbweb.entity.Producto;
import com.gbweb.entity.Usuario;
import com.gbweb.enums.EstadoPedido;
import com.gbweb.enums.ROL;
import com.gbweb.service.LineaPedidoService;
import com.gbweb.service.MesaService;
import com.gbweb.service.NegocioService;
import com.gbweb.service.PedidoService;
import com.gbweb.service.ProductoService;
import com.gbweb.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class NegocioControllerTest {
	
	@Autowired
	protected NegocioController negocioController;
	
    @Autowired  
    private MockMvc mockMvc;
    
    @MockBean
    private MesaService mesaService;
    
    @MockBean
    private LineaPedidoService lineaPedidoService;
    
    @MockBean
    private PedidoService pedidoService;
    
    @MockBean
    private ProductoService productoService;
    
    @MockBean
    private UserService userService;
    
    @MockBean
    private NegocioService negocioService;
    
    @Test
    void contextLoads() throws Exception {
        assertThat(negocioController).isNotNull();
    }

    @WithMockUser(authorities = {"GERENTE"})
    @Test
    void testCrearNegocio() throws Exception{
       mockMvc.perform(get("/crearNegocio")).andExpect(status().isOk());

    }
    
    @WithMockUser(authorities = {"GERENTE"})
    @Test
    void testEditarNegocio() throws Exception{

    	Negocio n = new Negocio();
    	n.setId(1L);
    	
    	doReturn(n).when(negocioService).findNegocioById(1L);
    	
       mockMvc.perform(get("/editarNegocio/{idNegocio}",1)).andExpect(status().isOk());

    }
    
    @WithMockUser(authorities = {"CLIENTE"})
    @Test
    void testListarNegocioCliente() throws Exception{
    	
    	Negocio n = new Negocio();
    	n.setId(1L);
    	n.setProductos(new ArrayList<>());
    	
    	Usuario u = new Usuario();
    	u.setRol(ROL.CLIENTE);
    	
    	doReturn(Arrays.asList(n)).when(negocioService).findAll();
    	doReturn(u).when(userService).usuarioActual();
    	
        mockMvc.perform(get("/listarNegocios")).andExpect(status().isOk())
        .andExpect(view().name("negocio/listaNegociosClientes"));

    }
    
    @WithMockUser(authorities = {"GERENTE"})
    @Test
    void testListarNegocioGerente() throws Exception{
    	
    	Negocio n = new Negocio();
    	n.setId(1L);
    	n.setProductos(new ArrayList<>());
    	
    	Usuario u = new Usuario();
    	u.setRol(ROL.GERENTE);
    	
    	doReturn(Arrays.asList(n)).when(negocioService).findAll();
    	doReturn(u).when(userService).usuarioActual();
    	
        mockMvc.perform(get("/listarNegocios")).andExpect(status().isOk())
        .andExpect(view().name("negocio/listaNegocios"));

    }
    
    @WithMockUser()
    @Test
    void testListarNegocioClienteNoLogueado() throws Exception{
    	
    	Negocio n = new Negocio();
    	n.setId(1L);
    	n.setProductos(new ArrayList<>());
 
    	
    	doReturn(Arrays.asList(n)).when(negocioService).findAll();
    	doReturn(null).when(userService).usuarioActual();
    	
        mockMvc.perform(get("/listarNegocios")).andExpect(status().isOk())
        .andExpect(view().name("negocio/listaNegociosClientes"));

    }
    
    @WithMockUser()
    @Test
    void testListarPedidosPorNegocioPorPedido() throws Exception{
    	
    	Negocio n = new Negocio();
    	n.setId(1L);
    	Mesa m = new Mesa();
    	m.setId(1L);
    	Pedido p = new Pedido();
    	p.setId(1l);
    	LineaPedido lp = new LineaPedido();
    	lp.setId(1L);
    	lp.setCantidad(1);
    	lp.setPrecio(10.0);
    	lp.setProducto(new Producto());
    	p.setLineaPedidos(Arrays.asList(lp));
    	p.setEstadoPedido(EstadoPedido.CANCELADO);
    	m.setPedidos(Arrays.asList(p));
    	p.setMesa(m);
    	n.setProductos(new ArrayList<>());
    	n.setMesas(Arrays.asList(m));
 
    	
    	doReturn(p).when(pedidoService).findById(1L);
    	
        mockMvc.perform(get("/pedidos/{idNegocio}/{idPedido}",1,1)).andExpect(status().isOk())
        .andExpect(view().name("negocio/listaProductosPedido"));

    }
    
    @WithMockUser()
    @Test
    void testListarPedidosPorNegocio() throws Exception{
    	
    	Negocio n = new Negocio();
    	n.setId(1L);
    	Mesa m = new Mesa();
    	m.setId(1L);
    	Pedido p = new Pedido();
    	p.setId(1l);
    	p.setEstadoPedido(EstadoPedido.CANCELADO);
    	m.setPedidos(Arrays.asList(p));
    	p.setMesa(m);
    	n.setProductos(new ArrayList<>());
    	n.setMesas(Arrays.asList(m));
 
    	
    	doReturn(n).when(negocioService).findNegocioById(1L);
    	
        mockMvc.perform(get("/pedidos/{idNegocio}",1)).andExpect(status().isOk())
        .andExpect(view().name("negocio/listaPedidos"));

    }
    
}
