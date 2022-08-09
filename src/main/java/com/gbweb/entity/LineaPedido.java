package com.gbweb.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "linea_pedido")
public class LineaPedido {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String nombre;
	
	private Double precio;
	
	private Integer cantidad;
	
	private Boolean servido;
	
	@ManyToOne
    @JoinColumn(name="pedido_id")
    private Pedido pedido;
	
	@ManyToOne
    @JoinColumn(name="producto_id", nullable = true)
    private Producto producto;

	
	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public Boolean getServido() {
		return servido;
	}

	public void setServido(Boolean servido) {
		this.servido = servido;
	}
	
	

}
