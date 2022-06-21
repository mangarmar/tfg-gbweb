package com.gbweb.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.gbweb.enums.TipoProducto;



@Entity
@Table(name = "productos")
public class Producto {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private TipoProducto tipo;
	
	@NotEmpty(message="Indique el nombre del producto")
	private String nombre;
	
	@NotNull(message = "Indique el precio del producto")
	private Double precio;
	
	private Boolean visibilidad;
	
	@ManyToMany(mappedBy = "productos")
	private List<Pedido> pedidos;
	
	@ManyToMany(mappedBy = "productos")
	private List<Negocio> negocios;

	
	
	public List<Pedido> getPedidos() {
		return pedidos;
	}

	public void setPedidos(List<Pedido> pedidos) {
		this.pedidos = pedidos;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TipoProducto getTipo() {
		return tipo;
	}

	public void setTipo(TipoProducto tipo) {
		this.tipo = tipo;
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

	public List<Negocio> getNegocios() {
		return negocios;
	}

	public void setNegocios(List<Negocio> negocios) {
		this.negocios = negocios;
	}

	public Boolean getVisibilidad() {
		return visibilidad;
	}

	public void setVisibilidad(Boolean visibilidad) {
		this.visibilidad = visibilidad;
	}




	
	
	
	

}
