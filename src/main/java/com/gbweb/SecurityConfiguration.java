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
				.antMatchers("/crearNegocio/**", "/listarNegocios/**", "/crearProducto/**", "/listarProductos/**").hasAuthority("GERENTE")
				.antMatchers("/").permitAll().anyRequest().denyAll()
				
				
				
				.and().formLogin()
				.defaultSuccessUrl("/", true).failureUrl("/login?error=true").usernameParameter("username")
				.passwordParameter("password").and().logout().logoutSuccessUrl("/");

	}

	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers("/**/*.{js,html,css}");
	}

}
