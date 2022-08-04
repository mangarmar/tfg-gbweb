package com.gbweb.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gbweb.enums.EstadoPedido;

@Entity
@Table(name = "pedidos")
public class Pedido implements Serializable {
	

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@JoinTable(name = "prodPedido", joinColumns = @JoinColumn(name = "idPedido"), inverseJoinColumns = @JoinColumn(name = "idProducto"))
	@ManyToMany()
	private List<Producto> productos;
	
	@ManyToOne
    @JoinColumn(name="mesa_id", nullable=true)
    private Mesa mesa;
		
	private EstadoPedido estadoPedido;
	
	@OneToMany(mappedBy="pedido")
    private List<LineaPedido> lineaPedidos;
	
	private Boolean porServir;
	
	public List<LineaPedido> getLineaPedidos() {
		return lineaPedidos;
	}

	public void setLineaPedidos(List<LineaPedido> lineaPedidos) {
		this.lineaPedidos = lineaPedidos;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Mesa getMesa() {
		return mesa;
	}

	public void setMesa(Mesa mesa) {
		this.mesa = mesa;
	}

	public EstadoPedido getEstadoPedido() {
		return estadoPedido;
	}

	public void setEstadoPedido(EstadoPedido estadoPedido) {
		this.estadoPedido = estadoPedido;
	}

	public List<Producto> getProductos() {
		return productos;
	}

	public void setProductos(List<Producto> productos) {
		this.productos = productos;
	}
	


	public Boolean getPorServir() {
		porServir = this.lineaPedidos.stream().filter(x->x.getServido()!=null).anyMatch(x->x.getServido().equals(false));
		return porServir;
	}

	public void setPorServir(Boolean porServir) {
		this.porServir = porServir;
	}

	public int compareTo(Pedido o) {
		// TODO Auto-generated method stub
		return 0;
	}
	

}
