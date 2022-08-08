package com.gbweb.entity;

import java.util.List;
import java.util.Objects;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gbweb.enums.TipoProducto;



@Entity
@Table(name = "productos")
public class Producto implements Comparable<Producto> {
	
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
	@JsonIgnore
	private List<Negocio> negocios;

    @OneToMany(mappedBy="producto")
    @JsonIgnore
    private List<LineaPedido> lineaPedidos;
	
	
	public List<LineaPedido> getLineaPedidos() {
		return lineaPedidos;
	}

	public void setLineaPedidos(List<LineaPedido> lineaPedidos) {
		this.lineaPedidos = lineaPedidos;
	}

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

	@Override
	public int hashCode() {
		return Objects.hash(nombre);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Producto other = (Producto) obj;
		return Objects.equals(nombre, other.nombre);
	}

	@Override
	public int compareTo(Producto o) {
		// TODO Auto-generated method stub
		return 0;
	}




	
	
	
	

}
