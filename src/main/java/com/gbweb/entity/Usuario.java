package com.gbweb.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import com.gbweb.enums.ROL;

@Entity
@Table(name = "usuarios")
public class Usuario implements UserDetails {
	
	private static final long serialVersionUID = 1L;


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotEmpty(message = "Se debe introducir un nombre de usuario")
	private String username;
	
	@NotEmpty(message = "Debes escribir una contraseña")
	@Pattern(regexp = "^(?=.{8,}$)(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).*$",
	message = "La contraseña debe tener al menos 8 caracteres,un dígito,una minúscula y una mayúscula.")
	private String password;
	
	@NotEmpty(message = "Porfavor, introduzca un nombre")
	@Length(max=41, message = "Este campo no puede contener mas de 41 caractéres") // El máximo es 41 caractéres por que la persona con el nombre mas largo del mundo tiene ese número
	private String nombre;
	
	@NotEmpty(message = "Porfavor, introduzca los apellidos")
	@Length(max= 35 , message= "Este campo no puede contener mas de 35 caractéres")
	private String apellidos;
	
	@NotEmpty(message = "Porfavor, introduzca un DNI")
	@Pattern(regexp = "[0-9]{8}[A-Za-z]{1}", message = "Porfavor, introduzca un DNI válido")
	private String dni;
	
	@Past
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate fechaNacimiento;
	
	@NotEmpty(message = "Porfavor, introduzca una dirección")
	private String direccion;
	
	@NotEmpty(message = "Porfavor, introduzca un email")
	@Email(message = "Porfavor, introduzca un email válido")
	private String email;

	@Enumerated(EnumType.STRING)
	private ROL rol;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuario")
    private List<Negocio> negocios;
    
    private String mesa;
    
    private String permiso;
    
    
	
	 public String getMesa() {
		return mesa;
	}

	public void setMesa(String mesa) {
		this.mesa = mesa;
	}

	public String getPermisos() {
		return permiso;
	}

	public void setPermisos(String permiso) {
		this.permiso = permiso;
	}

	@Override
	    public Collection<? extends GrantedAuthority> getAuthorities() {
	        List<GrantedAuthority> roles = new ArrayList<>();
	        roles.add(new SimpleGrantedAuthority(rol.toString()));
	        return roles;
	    }

	public List<Negocio> getNegocios() {
		return negocios;
	}

	public void setNegocios(List<Negocio> negocios) {
		this.negocios = negocios;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

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

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public ROL getRol() {
		return rol;
	}

	public void setRol(ROL rol) {
		this.rol = rol;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	
}
