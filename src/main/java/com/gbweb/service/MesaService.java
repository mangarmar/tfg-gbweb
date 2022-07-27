package com.gbweb.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.gbweb.entity.LineaPedido;
import com.gbweb.entity.Mesa;
import com.gbweb.entity.Negocio;
import com.gbweb.entity.Pedido;
import com.gbweb.entity.Usuario;
import com.gbweb.enums.Estado;
import com.gbweb.repository.MesaRepository;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TextAlignment;

@Service
public class MesaService {

	@Autowired
	MesaRepository mesaRepo;
	
	@Autowired
	NegocioService negocioService;
	
	@Autowired
	UserService userService;

	public Boolean añadirMesa(@Valid Mesa mesa, Long idNegocio) {
		
		List<Mesa> mesas = (List<Mesa>) mesaRepo.findAll();
		Boolean existeCodigo = mesas.stream().anyMatch(x->x.getCodigo().equals(mesa.getCodigo()));
		if(!existeCodigo) {
			Negocio negocio = negocioService.findNegocioById(idNegocio);
			negocio.getMesas().add(mesa);
			mesa.setNegocio(negocio);
			mesa.setEstado(Estado.LIBRE);
			mesaRepo.save(mesa);
			return true;
		}else {
			return false;
		}
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
