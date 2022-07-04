package com.gbweb;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.gbweb.enums.ROL;
import com.gbweb.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	DataSource dataSource;

	@Bean
	public UserDetailsService userDetailsService() {
		return new UserService();
	}

	@Bean 
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
		auth.userDetailsService(userDetailsService());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
				.antMatchers("/resources/**", "/webjars/**", "/h2-console/**").permitAll()
				.antMatchers("/crearCliente", "/crearGerente/**").permitAll()
				.antMatchers("/crearNegocio/**","/editarNegocio/**", "/crearProducto/**",
						"/añadirProducto/**", "/eliminarProducto/**", "/editarProducto/**", "/cambiarVisibilidad/**", "/añadirMesa/**", "/mesas/**", "/mesas/mesaOcupada/**").hasAuthority("GERENTE")
				.antMatchers("/confirmarMesa/**", "/mesas/**").hasAnyAuthority("CLIENTE", "GERENTE", "CLIENTE_CONFIRMADO")
				.antMatchers("/salir/**").hasAuthority("CLIENTE_CONFIRMADO")
				.antMatchers("/pedir/**", "/añadirAlPedido/**").hasAuthority("CLIENTE")
				.antMatchers("/images/**").anonymous()
				.antMatchers("/listarNegocios/**", "/listarProductos/**").permitAll()
				.antMatchers("/").permitAll().anyRequest().permitAll()
		        .and()
	              .formLogin()
	              .loginPage("/login")
	              .defaultSuccessUrl("/listarNegocios").permitAll()
					.failureUrl("/login").and().logout().logoutSuccessUrl("/login");

			http.csrf().disable();
			

	}

	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers("/**/*.{js,html,css}");
	}

}
