package com.gbweb.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import com.gbweb.enums.Estado;
import com.gbweb.enums.EstadoPedido;
import com.gbweb.enums.ROL;
import com.gbweb.service.MesaService;
import com.gbweb.service.NegocioService;
import com.gbweb.service.PedidoService;
import com.gbweb.service.UserService;
import com.google.common.base.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MesaControllerTest {
	
	@Autowired
	protected MesaController mesaController;
	
    @Autowired  
    private MockMvc mockMvc;
    
    @MockBean
    private MesaService mesaService;
    
    @MockBean
    private PedidoService pedidoService;
    
    @MockBean
    private UserService userService;
    
    @MockBean
    private NegocioService negocioService;
    
    @Test
    void contextLoads() throws Exception {
        assertThat(mesaController).isNotNull();
    }
    
    @WithMockUser(authorities = {"CLIENTE"})
    @Test
    void testListarMesas() throws Exception{
    	
    	Negocio n = new Negocio();
    	Mesa m = new Mesa();
    	Pedido p = new Pedido();
    	
    	p.setId(1L);
    	p.setEstadoPedido(EstadoPedido.ACTIVO);
    	m.setId(1L);
    	m.setEstado(Estado.LIBRE);
    	m.setNegocio(n);
    	m.setPedidos(Arrays.asList(p));
    	n.setId(1L);
    	n.setMesas(Arrays.asList(m));

    	
    	doReturn(n).when(negocioService).findNegocioById(1L);
    	
        mockMvc.perform(get("/mesas/{idNegocio}",1)).andExpect(status().isOk())
        .andExpect(view().name("negocio/listarMesas"));

    }

    @WithMockUser(authorities = {"CLIENTE"})
    @Test
    void testListarMesasLibres() throws Exception{
    	
    	Negocio n = new Negocio();
    	Mesa m = new Mesa();
    	Pedido p = new Pedido();
    	
    	p.setId(1L);
    	p.setEstadoPedido(EstadoPedido.ACTIVO);
    	m.setId(1L);
    	m.setEstado(Estado.LIBRE);
    	m.setNegocio(n);
    	m.setPedidos(Arrays.asList(p));
    	n.setId(1L);
    	n.setMesas(Arrays.asList(m));

    	
    	doReturn(n).when(negocioService).findNegocioById(1L);
    	
        mockMvc.perform(get("/mesas/libres/{idNegocio}",1)).andExpect(status().isOk())
        .andExpect(view().name("negocio/listarMesas"));

    }
    
    @WithMockUser(authorities = {"CLIENTE"})
    @Test
    void testMiMesa() throws Exception{
    	
    	Mesa m = new Mesa();
    	Negocio n = new Negocio();
    	Usuario u = new Usuario();
    	Pedido p = new Pedido();
    	
    	p.setId(1L);
    	p.setEstadoPedido(EstadoPedido.ACTIVO);
    	
    	u.setPermisos("Aceptado"); 	
    	u.setRol(ROL.CLIENTE);
    	u.setMesa(m);
    	
    	n.setId(1L);
    	n.setMesas(Arrays.asList(m));

    	m.setId(1L);
    	m.setNegocio(n);
    	m.setCodigo("AAA");
    	m.setPedidos(Arrays.asList(p));
    	m.setEstado(Estado.OCUPADA);
    	
    	
    	doReturn(n).when(negocioService).findNegocioById(1L);
    	doReturn(u).when(userService).usuarioActual();

    	
    	
    	
        mockMvc.perform(get("/mesas/estado/{idNegocio}",1)).andExpect(status().isOk())
        .andExpect(view().name("negocio/listarMesas"));

    }
    
    @WithMockUser(authorities = "CLIENTE")
    @Test
    void testMiMesaNegativo() throws Exception{
    	
    	Negocio n = new Negocio();
    	Usuario u = new Usuario();
    	
    	doReturn(n).when(negocioService).findNegocioById(1L);
    	doReturn(u).when(userService).usuarioActual();

        mockMvc.perform(get("/mesas/estado/{idNegocio}",1)).andExpect(status().isOk())
        .andExpect(view().name("error/mesaLiberada"));

    }
  
    @WithMockUser(authorities = {"GERENTE"})
    @Test
    void testAñadirMesa() throws Exception{ 	
    	Negocio n = new Negocio();
    	n.setId(1L);
    	
        mockMvc.perform(get("/mesas/añadirMesa/{idNegocio}",1)).andExpect(status().isOk())
        .andExpect(view().name("negocio/añadirMesa"));

    }
    

    
    @WithMockUser(authorities = "GERENTE")
    @Test
    void testEliminarMesa() throws Exception{
    	
    	Negocio n = new Negocio();
    	n.setId(1L);
    	
    	Mesa m = new Mesa();
    	m.setId(1L);
    	m.setNegocio(n);
    	
    	List<Mesa> mList = new ArrayList<>();
    	mList.add(m);
    	n.setMesas(mList);
    	
    	doReturn(m).when(mesaService).findById(1L);
    	doReturn(n).when(negocioService).findNegocioById(1L);
    	
        mockMvc.perform(get("/mesas/eliminarMesa/{idNegocio}/{idMesa}",1,1)).andExpect(status().is3xxRedirection());
    }
    
    @WithMockUser(authorities = "CLIENTE")
    @Test
    void testSolicitarMesa() throws Exception{
    	
    	Usuario u = new Usuario();
    	u.setId(1L);

    	Mesa m = new Mesa();
    	m.setId(1L);

    	
    	doReturn(u).when(userService).usuarioActual();
    	doReturn(u).when(userService).save(u);
    	doReturn(m).when(mesaService).findById(1L);
    	
        mockMvc.perform(get("/mesas/solicitar/{idNegocio}/{idMesa}",1,1)).andExpect(status().is3xxRedirection());
    }
    
    @WithMockUser(authorities = "CLIENTE")
    @Test
    void testSolicitarNegativoMesa() throws Exception{
    	
    	Usuario u = new Usuario();
    	u.setId(1L);

    	Mesa m = new Mesa();
    	m.setId(1L);
    	
    	u.setMesa(m);
    	
    	doReturn(u).when(userService).usuarioActual();
    	doReturn(u).when(userService).save(u);
    	doReturn(m).when(mesaService).findById(1L);
    	
        mockMvc.perform(get("/mesas/solicitar/{idNegocio}/{idMesa}",1,1)).andExpect(status().is3xxRedirection());
    }
    
    @WithMockUser(authorities = {"GERENTE"})
    @Test
    void testMesaLibreGerente() throws Exception{
    	
    	Usuario u = new Usuario();
    	u.setUsername("username");
    	u.setPassword("username");
    	u.setRol(ROL.GERENTE);
    	
    	Usuario u2 = new Usuario();
    	u2.setUsername("username2");
    	u2.setPassword("username2");
    	u2.setPermisos("Aceptado");
    	
    	u2.setRol(ROL.CLIENTE);
    
    	
    	Pedido p = new Pedido();
    	p.setEstadoPedido(EstadoPedido.ACTIVO);
    	p.setId(1L);
    	

    	Negocio n = new Negocio();
    	n.setId(1L);
    	
    	Mesa m = new Mesa();
    	m.setId(1L);
    	m.setPedidos(Arrays.asList(p));
    	m.setNegocio(n);
    	m.setCodigo("AAA");
    	
    	
    	u2.setMesa(m);

    	n.setMesas(Arrays.asList(m));
    	
    	doReturn(u).when(userService).usuarioActual();
    	doReturn(m).when(mesaService).findById(1L);
    	doReturn(p).when(pedidoService).nuevoPedido(p);
    	doReturn(u).when(userService).findByMesa("AAA");
    	
        mockMvc.perform(get("/mesas/mesaLibre/{idNegocio}/{idMesa}",1,1)).andExpect(status().is3xxRedirection());

    }
    
    @WithMockUser(authorities = {"GERENTE"})
    @Test
    void testMesaLibreCliente() throws Exception{
    	
    	Usuario u2 = new Usuario();
    	u2.setUsername("username2");
    	u2.setPassword("username2");
    	u2.setPermisos("Aceptado");
    	
    	u2.setRol(ROL.CLIENTE);
    
    	
    	Pedido p = new Pedido();
    	p.setEstadoPedido(EstadoPedido.ACTIVO);
    	p.setId(1L);
    	

    	Negocio n = new Negocio();
    	n.setId(1L);
    	
    	Mesa m = new Mesa();
    	m.setId(1L);
    	m.setPedidos(Arrays.asList(p));
    	m.setNegocio(n);
    	m.setCodigo("AAA");
    	
    	
    	u2.setMesa(m);

    	n.setMesas(Arrays.asList(m));
    	
    	doReturn(u2).when(userService).usuarioActual();
    	doReturn(m).when(mesaService).findById(1L);
    	doReturn(p).when(pedidoService).nuevoPedido(p);
    	doReturn(u2).when(userService).findByMesa("AAA");
    	
        mockMvc.perform(get("/mesas/mesaLibre/{idNegocio}/{idMesa}",1,1)).andExpect(status().is3xxRedirection());

    }
    
    @WithMockUser(authorities = {"GERENTE"})
    @Test
    void testMesaOcupada() throws Exception{

    	Negocio n = new Negocio();
    	n.setId(1L);
    	
    	Mesa m = new Mesa();
    	m.setId(1L);
    	m.setNegocio(n);
    	m.setCodigo("AAA");

    	Usuario u = new Usuario();
    	u.setRol(ROL.CLIENTE);
    	u.setMesa(m);

    	n.setMesas(Arrays.asList(m));
    	
    	doReturn(u).when(userService).save(u);
    	doReturn(u).when(userService).usuarioActual();
    	doReturn(Arrays.asList(u)).when(userService).findAllUsers();
    	doReturn(m).when(mesaService).findById(1L);
    	
        mockMvc.perform(get("/mesas/mesaOcupada/{idNegocio}/{idMesa}",1,1)).andExpect(status().is3xxRedirection());

    }
    
    @WithMockUser(authorities = "CLIENTE")
    @Test
    void cuenta() throws Exception{
     	Negocio n = new Negocio();
     	n.setNombre("negocio");
    	n.setId(1L);
    	
    	Mesa m = new Mesa();
    	m.setId(1L);
    	
    	Pedido p = new Pedido();
    	p.setId(1L);
    	p.setEstadoPedido(EstadoPedido.ACTIVO);
    	m.setPedidos(Arrays.asList(p));
    	
    	LineaPedido lp = new LineaPedido();
    	lp.setCantidad(1);
    	lp.setId(1L);
    	lp.setNombre("nombre");
    	lp.setPedido(p);
    	lp.setPrecio(12.0);
    	lp.setProducto(new Producto());
    	lp.setServido(true);
    	
    	LineaPedido lp2 = new LineaPedido();
    	lp2.setCantidad(1);
    	lp2.setId(2L);
    	lp2.setNombre("nombre");
    	lp2.setPedido(p);
    	lp2.setPrecio(12.0);
    	lp2.setProducto(new Producto());
    	lp2.setServido(false);
    	
    	p.setLineaPedidos(Arrays.asList(lp, lp2));
    	
    	doReturn(m).when(mesaService).findById(1L);
    	doReturn(n).when(negocioService).findNegocioById(1L);

    	
        mockMvc.perform(get("/mesas/pedido/{idNegocio}/{idMesa}",1,1)).andExpect(status().isOk());

    	
    }
    
    @WithMockUser(authorities = "CLIENTE")
    @Test
    void cuentaPDF() throws Exception{
     	Negocio n = new Negocio();
     	n.setNombre("negocio");
    	n.setId(1L);
    	
    	Mesa m = new Mesa();
    	m.setId(1L);
    	
    	Pedido p = new Pedido();
    	p.setId(1L);
    	p.setEstadoPedido(EstadoPedido.ACTIVO);
    	m.setPedidos(Arrays.asList(p));
    	
    	LineaPedido lp = new LineaPedido();
    	lp.setCantidad(1);
    	lp.setId(1L);
    	lp.setNombre("nombre");
    	lp.setPedido(p);
    	lp.setPrecio(12.0);
    	lp.setProducto(new Producto());
    	lp.setServido(true);
    	
    	LineaPedido lp2 = new LineaPedido();
    	lp2.setCantidad(1);
    	lp2.setId(2L);
    	lp2.setNombre("nombre");
    	lp2.setPedido(p);
    	lp2.setPrecio(12.0);
    	lp2.setProducto(new Producto());
    	lp2.setServido(false);
    	
    	p.setLineaPedidos(Arrays.asList(lp, lp2));
    	
    	doReturn(p).when(pedidoService).findById(1L);
    	doReturn(n).when(negocioService).findNegocioById(1L);

    	
        mockMvc.perform(get("/mesas/cuenta/{idPedido}",1)).andExpect(status().isOk());

    	
    }
    
    @WithMockUser(authorities = "CLIENTE")
    @Test
    void descargarFactura() throws Exception{
     	Negocio n = new Negocio();
     	n.setNombre("negocio");
    	n.setId(1L);
    	
    	Mesa m = new Mesa();
    	m.setId(1L);
    	
    	Pedido p = new Pedido();
    	p.setId(1L);
    	p.setEstadoPedido(EstadoPedido.ACTIVO);
    	m.setPedidos(Arrays.asList(p));
    	
    	LineaPedido lp = new LineaPedido();
    	lp.setCantidad(1);
    	lp.setId(1L);
    	lp.setNombre("nombre");
    	lp.setPedido(p);
    	lp.setPrecio(12.0);
    	lp.setProducto(new Producto());
    	lp.setServido(true);
    	
    	LineaPedido lp2 = new LineaPedido();
    	lp2.setCantidad(1);
    	lp2.setId(2L);
    	lp2.setNombre("nombre");
    	lp2.setPedido(p);
    	lp2.setPrecio(12.0);
    	lp2.setProducto(new Producto());
    	lp2.setServido(false);
    	
    	p.setLineaPedidos(Arrays.asList(lp, lp2));
    	
    	doReturn(p).when(pedidoService).findById(1L);
    	doReturn(n).when(negocioService).findNegocioById(1L);

    	
        mockMvc.perform(get("/mesas/factura/{idPedido}",1)).andExpect(status().isOk());

    	
    }
    
    @WithMockUser(authorities = "CLIENTE")
    @Test
    void solicitarCuenta() throws Exception{
    	
    	Usuario u = new Usuario();
    	u.setId(1L);
    	
     	Negocio n = new Negocio();
     	n.setNombre("negocio");
    	n.setId(1L);
    	
    	Mesa m = new Mesa();
    	m.setId(1L);
    	
    	Pedido p = new Pedido();
    	p.setId(1L);
    	p.setEstadoPedido(EstadoPedido.ACTIVO);
    	m.setPedidos(Arrays.asList(p));
    	
    	
    	doReturn(m).when(mesaService).findById(1L);
    	doReturn(p).when(pedidoService).findById(1L);
    	doReturn(u).when(userService).usuarioActual();

    	
        mockMvc.perform(get("/mesas/solicitar/cuenta/{idMesa}/{idPedido}",1,1)).andExpect(status().is3xxRedirection());

    	
    }
    
    @WithMockUser(authorities = "CLIENTE")
    @Test
    void solicitarCuentaEspera() throws Exception{
    	
    	
    	Pedido p = new Pedido();
    	p.setId(1L);
    	p.setEstadoPedido(EstadoPedido.ACTIVO);
    	
    	
    	doReturn(p).when(pedidoService).findById(1L);

    	
        mockMvc.perform(get("/mesas/espera/cuenta/{idMesa}/{idPedido}",1,1)).andExpect(status().isOk());
    	
    }
    
    @WithMockUser(authorities = "CLIENTE")
    @Test
    void cancelarCuenta() throws Exception{
    	
    	Usuario u = new Usuario();
    	u.setId(1L);
    	
     	Negocio n = new Negocio();
     	n.setNombre("negocio");
    	n.setId(1L);
    	
    	Mesa m = new Mesa();
    	m.setId(1L);
    	m.setNegocio(n);
    	
    	Pedido p = new Pedido();
    	p.setId(1L);
    	p.setEstadoPedido(EstadoPedido.ACTIVO);
    	m.setPedidos(Arrays.asList(p));
    	
    	
    	doReturn(m).when(mesaService).findById(1L);
    	doReturn(p).when(pedidoService).findById(1L);
    	doReturn(u).when(userService).usuarioActual();

    	
        mockMvc.perform(get("/mesas/cancelar/cuenta/{idMesa}/{idPedido}",1,1)).andExpect(status().is3xxRedirection());

    	
    }
    
    @WithMockUser(authorities = "CLIENTE")
    @Test
    void pagoCuenta() throws Exception{
    	
    	Usuario u = new Usuario();
    	u.setId(1L);
    	
     	Negocio n = new Negocio();
     	n.setNombre("negocio");
    	n.setId(1L);
    	
    	Mesa m = new Mesa();
    	m.setId(1L);
    	m.setNegocio(n);
    	
    	Pedido p = new Pedido();
    	p.setId(1L);
    	p.setEstadoPedido(EstadoPedido.ACTIVO);
    	m.setPedidos(Arrays.asList(p));
    	
    	
    	doReturn(m).when(mesaService).findById(1L);
    	doReturn(p).when(pedidoService).findById(1L);
    	doReturn(u).when(userService).usuarioActual();

    	
        mockMvc.perform(get("/mesas/pago/cuenta/{idMesa}/{idPedido}",1,1)).andExpect(status().is3xxRedirection());

    	
    }
}
