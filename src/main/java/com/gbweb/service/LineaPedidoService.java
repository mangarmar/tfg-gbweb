package com.gbweb.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
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

	public void save(LineaPedido lp) {
		
		 lineaPedidoRepo.save(lp);	
	}
	
//	public List<LineaPedido> lineaPedidosPorPedido(Long idMesa){
//		List<LineaPedido> lps = lineaPedidoRepo.findAll();
//		Pedido pedido = mesaService.findById(idMesa).getPedidos().stream().filter(x->x.getEstadoPedido().toString().equals("ACTIVO")).findFirst().orElse(null);
//		
//		return lps.stream().filter(x->x.getPedido().equals(pedido)).collect(Collectors.toList());
//	}
	
	public void borraTodosNoConfirmadosPorPedido(Pedido pedidoActivo) {
		List<LineaPedido> lps = lineaPedidoRepo.findAll();
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
		List<LineaPedido> lps = lineaPedidoRepo.findAll();
		List<LineaPedido> lpBorrar = lps.stream().filter(x->x.getPedido().equals(lp)).collect(Collectors.toList());
		
		lineaPedidoRepo.deleteAll(lpBorrar);
	}
	
	public List<LineaPedido> findTodosPorPedido(Pedido pedidoActivo) {
		List<LineaPedido> lps = lineaPedidoRepo.findAll();
		
		 return lps.stream().filter(x->x.getPedido().equals(pedidoActivo)).collect(Collectors.toList());
	}
	
	public LineaPedido findById(Long idProducto) {
		return lineaPedidoRepo.findById(idProducto).orElse(null);
	}
	
	public String generarCuenta(Long idMesa) {
		String fileName = "cuenta.pdf";
		try {
			File file = new File(fileName);
			PdfWriter pdfWriter = new PdfWriter(file);
			PdfDocument pdfDocument = new PdfDocument(pdfWriter);
			Document document = new Document(pdfDocument);
			
//			 Add logo banner to recipt
			String imageFile = "src/main/resources/static/images/LOGO.png";
			ImageData data = ImageDataFactory.create(imageFile);
			Image img = new Image(data);
			document.add(img);

			Paragraph paragraph = new Paragraph();
			paragraph.setTextAlignment(TextAlignment.CENTER);
			Text blankLine = new Text("\n");
			paragraph.add(blankLine);
			
			// add font for pretext
			Style italic = new Style();
			PdfFont font = PdfFontFactory.createFont(StandardFonts.TIMES_ITALIC);
			italic.setFont(font).setFontSize(12);
			// add font for body
			Style body = new Style();
			PdfFont fontRoman = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
			body.setFont(fontRoman).setFontSize(14);
			
			String bodyMessage = "";
				Mesa mesa = mesaService.findById(idMesa);
				Pedido pedidoActivo = mesa.getPedidos().stream().filter(x->x.getEstadoPedido().toString().equals("ACTIVO")).findFirst().orElse(null);
				List<LineaPedido> lineaPedidos = pedidoActivo.getLineaPedidos();
				List<LineaPedido> productosServidos = new ArrayList<LineaPedido>();
				Double precioTotalServido = 0.0;
				for(int i = 0; i<lineaPedidos.size();i++){
					LineaPedido lp = lineaPedidos.get(i);
					if(lp.getServido()!=null) {
						if(lp.getServido()==true) {
							precioTotalServido+=lineaPedidos.get(i).getPrecio()*lineaPedidos.get(i).getCantidad();
							productosServidos.add(lp);
						}
					}
				}
				
				// pretext
				String pretext = "Gracias por visitar " + mesa.getNegocio().getNombre() + ". Esperamos que vuelva pronto.";
				paragraph.add(new Text(pretext).addStyle(italic));
				paragraph.add(blankLine);
				pretext = "Número de mesa: " + mesa.getCodigo();
				paragraph.add(new Text(pretext).addStyle(italic));
				paragraph.add(blankLine);
				paragraph.add(blankLine);
				paragraph.add(blankLine);
				
				// info plate by plate
				for (LineaPedido linea: productosServidos) {
					Double total = linea.getPrecio()*linea.getCantidad();
					bodyMessage = linea.getNombre() + " = " + linea.getPrecio() + "€ x" + linea.getCantidad() + " = " + String.format("%.2f", total) + "€"; 
					paragraph.add(new Text(bodyMessage).addStyle(body));
					paragraph.add(blankLine);	
				}
				bodyMessage = "=========================================";
				paragraph.add(new Text(bodyMessage).addStyle(body));
				paragraph.add(blankLine);
				bodyMessage = "Coste total del pedido: " + /*price +*/  " €";
				paragraph.add(new Text(bodyMessage).addStyle(body));
				paragraph.add(blankLine);
				bodyMessage = "Id de compra: "+pedidoActivo.getId();
				paragraph.add(new Text(bodyMessage).addStyle(italic));
			
			
			document.add(paragraph);
			document.close();
			pdfWriter.close();
			System.out.println("PDF creado");
		} catch (FileNotFoundException ex) {
			System.err.println(ex.getMessage());
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
			
		}
		return fileName;
	}
	
	

}
