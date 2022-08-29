package com.gbweb.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import com.gbweb.enums.Tipo;
import com.gbweb.enums.TipoProducto;
import com.gbweb.service.LineaPedidoService;
import com.gbweb.service.MesaService;
import com.gbweb.service.NegocioService;
import com.gbweb.service.PedidoService;
import com.gbweb.service.ProductoService;
import com.gbweb.service.UserService;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProductoControllerTest {

	@Autowired
	protected ProductoController productoController;
	
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
        assertThat(productoController).isNotNull();
    }
	
    @WithMockUser(authorities = {"CLIENTE"})
    @Test
    void testListarProductos() throws Exception{
    	
    	Negocio n = new Negocio();
    	n.setId(1L);
    	n.setProductos(new ArrayList<>());
    	
    	Usuario u = new Usuario();
    	u.setMesa(new Mesa());
    	
    	doReturn(n).when(negocioService).findNegocioById(1L);
    	doReturn(u).when(userService).usuarioActual();
    	
        mockMvc.perform(get("/listarProductos/{idNegocio}",1)).andExpect(status().isOk())
        .andExpect(view().name("producto/listaProductos"));

    }
    
    @WithMockUser(authorities = {"CLIENTE"})
    @Test
    void testListarProductosParaPedir() throws Exception{
    	
    	Negocio n = new Negocio();
    	n.setId(1L);
    	n.setProductos(new ArrayList<>());
    	
    	
    	Producto pr = new Producto();
    	pr.setNombre("Nombre");
    	pr.setPrecio(1.0);
    	pr.setId(1L);
    	
    	
    	Mesa m = new Mesa();
    	m.setId(1L);
  
    	
    	Pedido p = new Pedido();
    	p.setProductos(Arrays.asList(pr));
    	p.setEstadoPedido(EstadoPedido.ACTIVO);
    	p.setMesa(m);
    	m.setPedidos(Arrays.asList(p));
    	
    	Usuario u = new Usuario();
    	u.setMesa(m);
    	
    	doReturn(m).when(mesaService).findById(1L);
    	doReturn(n).when(negocioService).findNegocioById(1L);
    	doReturn(u).when(userService).usuarioActual();
    	
        mockMvc.perform(get("/pedir/{idNegocio}/mesa/{idMesa}",1,1)).andExpect(status().isOk());

    }
    
    @WithMockUser(authorities = {"CLIENTE"})
    @Test
    void testListarProductosParaPedirPedidoNoActivo() throws Exception{
    	
    	Negocio n = new Negocio();
    	n.setId(1L);
    	n.setProductos(new ArrayList<>());
    	
    	Mesa m = new Mesa();
    	m.setId(1L);
    	
    	Pedido p = new Pedido();
    	p.setEstadoPedido(EstadoPedido.PENDIENTE_PAGO_EFECTIVO);
    	p.setMesa(m);
    	m.setPedidos(Arrays.asList(p));
    	
    	Usuario u = new Usuario();
    	u.setMesa(m);
    	
    	doReturn(m).when(mesaService).findById(1L);
    	doReturn(n).when(negocioService).findNegocioById(1L);
    	doReturn(u).when(userService).usuarioActual();
    	
        mockMvc.perform(get("/pedir/{idNegocio}/mesa/{idMesa}",1,1)).andExpect(status().isOk());

    }
    
    @WithMockUser(authorities = {"CLIENTE"})
    @Test
    void testListarPedido() throws Exception{
    	
    	Negocio n = new Negocio();
    	n.setId(1L);
    	n.setProductos(new ArrayList<>());
    	
    	Mesa m = new Mesa();
    	m.setId(1L);
    	
    	Pedido p = new Pedido();
    	p.setEstadoPedido(EstadoPedido.ACTIVO);
    	p.setMesa(m);
    	m.setPedidos(Arrays.asList(p));
    	
    	LineaPedido lp = new LineaPedido();
    	lp.setCantidad(1);
    	lp.setId(1L);
    	lp.setNombre("nombre");
    	lp.setPedido(p);
    	lp.setPrecio(12.0);
    	lp.setProducto(new Producto());
    	lp.setServido(true);
    	
    	p.setLineaPedidos(Arrays.asList(lp));
    	
    	Producto pr = new Producto();
    	pr.setId(1L);
    	pr.setNombre("nombre");
    	pr.setPrecio(10.00);

    	
    	p.setProductos(Arrays.asList(pr));

    	
    	Usuario u = new Usuario();
    	u.setMesa(m);
    	
    	doReturn(m).when(mesaService).findById(1L);
    	doReturn(n).when(negocioService).findNegocioById(1L);
    	doReturn(u).when(userService).usuarioActual();
    	
        mockMvc.perform(get("/pedido/negocio/{idNegocio}/mesa/{idMesa}",1,1)).andExpect(status().isOk())
        .andExpect(view().name("negocio/comanda"));

    }
    
    @WithMockUser(authorities = {"CLIENTE"})
    @Test
    void testConfirmarComanda() throws Exception{
    	
    	Mesa m = new Mesa();
    	m.setId(1L);
    	m.setNegocio(new Negocio());
    	
    	Pedido p = new Pedido();
    	p.setEstadoPedido(EstadoPedido.ACTIVO);
    	p.setMesa(m);
    	m.setPedidos(Arrays.asList(p));
    	
    	LineaPedido lp = new LineaPedido();
    	lp.setCantidad(1);
    	lp.setId(1L);
    	lp.setNombre("nombre");
    	lp.setPedido(p);
    	lp.setPrecio(12.0);
    	lp.setProducto(new Producto());
    	lp.setServido(true);
    	
    	LineaPedido lp1 = new LineaPedido();
    	lp1.setCantidad(1);
    	lp1.setId(2L);
    	lp1.setNombre("nombre");
    	lp1.setPedido(p);
    	lp1.setPrecio(12.0);
    	lp1.setProducto(new Producto());
    	lp1.setServido(null);
    	
    	p.setLineaPedidos(Arrays.asList(lp));
    	
    	doReturn(m).when(mesaService).findById(1L);
    	doReturn(Arrays.asList(lp,lp1)).when(lineaPedidoService).findTodosPorPedido(p);
    	
    	
       mockMvc.perform(get("/pedido/confirmar/negocio/{idNegocio}/mesa/{idMesa}",1,1)).andExpect(status().is3xxRedirection());

    }
    
    @WithMockUser(authorities = {"CLIENTE"})
    @Test
    void testSumarProductoAComanda() throws Exception{
    	
    	Mesa m = new Mesa();
    	m.setId(1L);
    	
    	Negocio n = new Negocio();
    	n.setId(1L);
    	m.setNegocio(n);
    	
    	Pedido p = new Pedido();
    	p.setEstadoPedido(EstadoPedido.ACTIVO);
    	p.setProductos(new ArrayList<>());
    	p.setMesa(m);
    	
    	LineaPedido lp = new LineaPedido();
    	lp.setCantidad(1);
    	lp.setId(1L);
    	lp.setNombre("nombre");
    	lp.setPedido(p);
    	lp.setPrecio(12.0);
    	lp.setProducto(new Producto());
    	lp.setServido(true);
    	
    	p.setLineaPedidos(Arrays.asList(lp));
    	
    	doReturn(lp).when(lineaPedidoService).findById(1L);
    	
    	
       mockMvc.perform(get("/pedido/sumar/{idPedido}",1)).andExpect(status().is3xxRedirection());

    }
    
    @WithMockUser(authorities = {"CLIENTE"})
    @Test
    void testRestarProductoAComanda() throws Exception{
    	
    	Mesa m = new Mesa();
    	m.setId(1L);
    	
    	Negocio n = new Negocio();
    	n.setId(1L);
    	m.setNegocio(n);
    	
    	Pedido p = new Pedido();
    	p.setEstadoPedido(EstadoPedido.ACTIVO);
    	p.setProductos(new ArrayList<>());
    	p.setMesa(m);

    	LineaPedido lp = new LineaPedido();
    	lp.setCantidad(1);
    	lp.setId(1L);
    	lp.setNombre("nombre");
    	lp.setPedido(p);
    	lp.setPrecio(12.0);
    	lp.setProducto(new Producto());
    	lp.setServido(true);
    	
    	p.setLineaPedidos(Arrays.asList(lp));
    	
    	doReturn(lp).when(lineaPedidoService).findById(1L);
    	
    	
       mockMvc.perform(get("/pedido/restar/{idPedido}",1)).andExpect(status().is3xxRedirection());

    }
    
    @WithMockUser(authorities = {"CLIENTE"})
    @Test
    void testCuenta() throws Exception{
    	
    	Usuario u = new Usuario();
    	u.setId(1L);
    	
    	Mesa m = new Mesa();
    	m.setId(1L);
    	u.setPermisos("Aceptado");
    	u.setMesa(m);
    	
    	Negocio n = new Negocio();
    	n.setId(1L);
    	m.setNegocio(n);
    	
    	Pedido p = new Pedido();
    	p.setEstadoPedido(EstadoPedido.ACTIVO);
    	p.setProductos(new ArrayList<>());
    	p.setMesa(m);
    	
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
    	doReturn(u).when(userService).usuarioActual();

    	
    	
       mockMvc.perform(get("/pedido/cuenta/{idNegocio}/mesa/{idMesa}",1,1)).andExpect(status().isOk())
       .andExpect(view().name("negocio/cuenta"));

    }
    
    
    @WithMockUser(authorities = {"CLIENTE"})
    @Test
    void testServirProducto() throws Exception{
    	
    	LineaPedido lp = new LineaPedido();
    	lp.setCantidad(1);
    	lp.setId(1L);
    	lp.setNombre("nombre");
    	lp.setPedido(new Pedido());
    	lp.setPrecio(12.0);
    	lp.setProducto(new Producto());
    	lp.setServido(true);
    	
    	
    	doReturn(lp).when(lineaPedidoService).findById(1L);
    	
    	
       mockMvc.perform(get("/pedido/servir/{idNegocio}/{idMesa}/{idProducto}",1,1,1)).andExpect(status().is3xxRedirection());

    }
    
    @WithMockUser(authorities = {"CLIENTE"})
    @Test
    void testSumarRestarYEliminarProducto() throws Exception{
    	
    	LineaPedido lp = new LineaPedido();
    	lp.setCantidad(1);
    	lp.setId(1L);
    	lp.setNombre("nombre");
    	lp.setPedido(new Pedido());
    	lp.setPrecio(12.0);
    	lp.setProducto(new Producto());
    	lp.setServido(true);
    	
    	
    	doReturn(lp).when(lineaPedidoService).findById(1L);
    	
    	
       mockMvc.perform(get("/pedido/sumar/{idNegocio}/{idMesa}/{idProducto}",1,1,1)).andExpect(status().is3xxRedirection());
       mockMvc.perform(get("/pedido/eliminar/{idNegocio}/{idMesa}/{idProducto}",1,1,1)).andExpect(status().is3xxRedirection());
       mockMvc.perform(get("/pedido/restar/{idNegocio}/{idMesa}/{idProducto}",1,1,1)).andExpect(status().is3xxRedirection());

    }
    
    
    @WithMockUser(authorities = {"CLIENTE"})
    @Test
    void testRestarProductoA0() throws Exception{
    	
    	LineaPedido lp = new LineaPedido();
    	lp.setCantidad(0);
    	lp.setId(1L);
    	lp.setNombre("nombre");
    	lp.setPedido(new Pedido());
    	lp.setPrecio(12.0);
    	lp.setProducto(new Producto());
    	lp.setServido(true);
    	
    	
    	doReturn(lp).when(lineaPedidoService).findById(1L);
    	
    	
       mockMvc.perform(get("/pedido/restar/{idNegocio}/{idMesa}/{idProducto}",1,1,1)).andExpect(status().is3xxRedirection());

    }
    
    @WithMockUser(authorities = {"GERENTE"})
    @Test
    void testAñadirProducto() throws Exception{

    	Negocio n = new Negocio();
    	n.setId(1L);
       mockMvc.perform(get("/añadirProducto/{idNegocio}",1)).andExpect(status().isOk());

    }
    

    
    @WithMockUser(authorities = {"GERENTE"})
    @Test
    void testEliminarProducto() throws Exception{

    	Negocio n = new Negocio();
    	n.setId(1L);
    	
    	Producto p = new Producto();
    	p.setId(1L);
    	List<Producto> pList = new ArrayList<Producto>();
    	pList.add(p);
    	n.setProductos(pList);
    	
    	LineaPedido lp = new LineaPedido();
    	p.setLineaPedidos(Arrays.asList(lp));
    	
    	doReturn(p).when(productoService).findById(1L);
    	doReturn(n).when(negocioService).findNegocioById(1L);
    	

       mockMvc.perform(get("/eliminarProducto/{idNegocio}/{idProducto}",1,1)).andExpect(status().is3xxRedirection());

    }
    
    @WithMockUser(authorities = {"GERENTE"})
    @Test
    void testEditarProducto() throws Exception{

    	Negocio n = new Negocio();
    	n.setId(1L);
    	
    	Producto p = new Producto();
    	p.setId(1L);
    	List<Producto> pList = new ArrayList<Producto>();
    	pList.add(p);
    	n.setProductos(pList);
    	
    	doReturn(p).when(productoService).findById(1L);

    	
       mockMvc.perform(get("/editarProducto/{idNegocio}/{idProducto}",1,1)).andExpect(status().isOk());

    }
    
    @WithMockUser(authorities = {"GERENTE"})
    @Test
    void testCambiarVisibilidadATrue() throws Exception{

    	Negocio n = new Negocio();
    	n.setId(1L);
    	
    	Producto p = new Producto();
    	p.setVisibilidad(true);
    	p.setId(1L);
    	List<Producto> pList = new ArrayList<Producto>();
    	pList.add(p);
    	n.setProductos(pList);
    	
    	doReturn(p).when(productoService).findById(1L);

    	
       mockMvc.perform(get("/cambiarVisibilidad/{idNegocio}/{idProducto}",1,1)).andExpect(status().is3xxRedirection());

    }
    
    @WithMockUser(authorities = {"GERENTE"})
    @Test
    void testCambiarVisibilidadAFalse() throws Exception{

    	Negocio n = new Negocio();
    	n.setId(1L);
    	
    	Producto p = new Producto();
    	p.setVisibilidad(false);
    	p.setId(1L);
    	List<Producto> pList = new ArrayList<Producto>();
    	pList.add(p);
    	n.setProductos(pList);
    	
    	doReturn(p).when(productoService).findById(1L);

    	
       mockMvc.perform(get("/cambiarVisibilidad/{idNegocio}/{idProducto}",1,1)).andExpect(status().is3xxRedirection());

    }
    

    
}
