package com.gbweb.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.gbweb.enums.Tipo;

@Entity
@Table(name = "negocios")
public class Negocio {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotEmpty(message="Introduzca el nombre de su negocio")
	private String nombre;
	

	@Size(min = 25, max = 250)
	private String descripcion;
	

	private String imagen = "/resources/static/images/default-image.png";
	

	@NotNull
	@Min(1)
	private Integer capacidad;
	

	@Email
	@NotEmpty
	private String email;

	private Tipo tipo;
	
	@Pattern(regexp = "[A-Za-z]{1}[0-9]{8}", message="Introduzca un CIF válido")
	@NotEmpty(message="Introduzca un CIF válido")
	private String cif;
	
	@NotEmpty(message = "Introduzca la localizacion de su negocio")
	private String localizacion;
	
	@ManyToOne()
	@JoinColumn(name = "usuario_id")
    private Usuario usuario;
	
	@JoinTable(name = "prodNeg", joinColumns = @JoinColumn(name = "idNegocio"), inverseJoinColumns = @JoinColumn(name = "idProducto"))
	@ManyToMany()
	private List<Producto> productos;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	public String getCif() {
		return cif;
	}

	public void setCif(String cif) {
		this.cif = cif;
	}

	public String getLocalizacion() {
		return localizacion;
	}

	public void setLocalizacion(String localizacion) {
		this.localizacion = localizacion;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Producto> getProductos() {
		return productos;
	}

	public void setProductos(List<Producto> productos) {
		this.productos = productos;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public Integer getCapacidad() {
		return capacidad;
	}

	public void setCapacidad(Integer capacidad) {
		this.capacidad = capacidad;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
	

}
