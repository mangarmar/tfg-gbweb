package com.gbweb.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.UniqueElements;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gbweb.enums.Estado;

@Entity
@Table(name = "mesas")
public class Mesa implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Pattern(regexp = "[A-Z]{3}-[0-9]{4}", message = "Patrón correcto: 3 letras mayúsculas, un guión y 4 números")
	@NotEmpty(message="Introduzca el código de la mesa")
	private String codigo;

	private Boolean activa = true;
	
	private Estado estado;
	
	@ManyToOne
	@JsonIgnore
    @JoinColumn(name="negocio_id", nullable=false)
    private Negocio negocio;
	
    @OneToMany(mappedBy="mesa")
    private List<Pedido> pedidos;

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

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public Negocio getNegocio() {
		return negocio;
	}
	

	public Boolean getActiva() {
		return activa;
	}

	public void setActiva(Boolean activa) {
		this.activa = activa;
	}

	public void setNegocio(Negocio negocio) {
		this.negocio = negocio;
	}
	
	public int compareTo(Mesa o) {
		// TODO Auto-generated method stub
		return 0;
	}


}
