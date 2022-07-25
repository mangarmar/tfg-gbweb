package com.gbweb.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.gbweb.entity.LineaPedido;
import com.gbweb.entity.Mesa;
import com.gbweb.entity.Pedido;
import com.gbweb.repository.LineaPedidoRepository;
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
public class LineaPedidoService {
	
	@Autowired
	LineaPedidoRepository lineaPedidoRepo;
	
	@Autowired
	MesaService mesaService;

	public LineaPedido save(LineaPedido lp) {
		
		 return lineaPedidoRepo.save(lp);	
	}
	
//	public List<LineaPedido> lineaPedidosPorPedido(Long idMesa){
//		List<LineaPedido> lps = lineaPedidoRepo.findAll();
//		Pedido pedido = mesaService.findById(idMesa).getPedidos().stream().filter(x->x.getEstadoPedido().toString().equals("ACTIVO")).findFirst().orElse(null);
//		
//		return lps.stream().filter(x->x.getPedido().equals(pedido)).collect(Collectors.toList());
//	}
	
	public void borraTodosNoConfirmadosPorPedido(Pedido pedidoActivo) {
		List<LineaPedido> lps = pedidoActivo.getLineaPedidos();
		List<LineaPedido> borrar = new ArrayList<LineaPedido>();
		for(int i = 0;i<lps.size();i++) {
			Boolean servido = lps.get(i).getServido();
			if(servido == null) {
				borrar.add(lps.get(i));
			}
		}
		
		 lineaPedidoRepo.deleteAll(borrar);
	}
	
	public void borraLineaPedido(LineaPedido lp) {
		lineaPedidoRepo.delete(lp);
	}
	
	public List<LineaPedido> findTodosPorPedido(Pedido pedidoActivo) {
		List<LineaPedido> lps = lineaPedidoRepo.findAll();
		
		 return lps.stream().filter(x->x.getPedido().equals(pedidoActivo)).collect(Collectors.toList());
	}
	
	public LineaPedido findById(Long idProducto) {
		return lineaPedidoRepo.findById(idProducto).orElse(null);
	}
	
	
	
	

}
