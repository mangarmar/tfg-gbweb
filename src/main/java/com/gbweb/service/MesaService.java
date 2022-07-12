package com.gbweb.service;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gbweb.entity.Mesa;
import com.gbweb.entity.Negocio;
import com.gbweb.entity.Producto;
import com.gbweb.enums.Estado;
import com.gbweb.repository.MesaRepository;
import com.gbweb.repository.ProductoRepository;

@Service
public class MesaService {

	@Autowired
	MesaRepository mesaRepo;
	
	@Autowired
	NegocioService negocioService;

	public void añadirMesa(@Valid Mesa mesa, Long idNegocio) {
		
		Negocio negocio = negocioService.findNegocioById(idNegocio);
		negocio.getMesas().add(mesa);
		mesa.setNegocio(negocio);
		mesa.setEstado(Estado.LIBRE);
		mesaRepo.save(mesa);	
	}
	
	public void actualizarEstado(@Valid Mesa mesa, Long idNegocio, Estado estado) {
		
		mesa.setEstado(estado);
		mesaRepo.save(mesa);
	}
	
	
	public Mesa findById(Long id) {
		return mesaRepo.findById(id).get();
	}
	
	public void eliminarMesa(Mesa mesa) {
		
		mesaRepo.delete(mesa);
	}

	public Mesa save(Mesa mesa) {
		return mesaRepo.save(mesa);
		
	}

	
}
