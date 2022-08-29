package com.gbweb.util;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.gbweb.entity.LineaPedido;
import com.gbweb.entity.Mesa;
import com.gbweb.entity.Negocio;
import com.gbweb.entity.Pedido;

public class GeneratePdfReport {

    private static final Logger logger = LoggerFactory.getLogger(GeneratePdfReport.class);

	private static final DecimalFormat df = new DecimalFormat("0.00");
    
    public static ByteArrayInputStream cuentaPDF(List<LineaPedido> productos, Negocio negocio, Mesa mesa, Double precioTotalServido) {

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {

        	
        	
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(90);
            table.setWidths(new int[]{3, 3, 3, 3});

            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

            PdfPCell hcell;
            hcell = new PdfPCell(new Phrase("Producto", headFont));
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Cantidad", headFont));
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Precio Ud", headFont));
            table.addCell(hcell);
            
            hcell = new PdfPCell(new Phrase("Total", headFont));
            table.addCell(hcell);

            for (LineaPedido producto : productos) {

                PdfPCell cell;

                cell = new PdfPCell(new Phrase(producto.getNombre()));
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(String.valueOf(producto.getCantidad())));
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(String.valueOf(producto.getPrecio()+" €")));
                table.addCell(cell);
                
                Double precioTotal = producto.getPrecio()*producto.getCantidad();
               
                
                cell = new PdfPCell(new Phrase(df.format(precioTotal)));
                table.addCell(cell);
            }

            DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm:ss");
            
            PdfWriter.getInstance(document, out);
            document.open();
            
            Paragraph paragraph = new Paragraph(negocio.getNombre().toUpperCase());
            paragraph.setAlignment(1);
            document.add(paragraph);
            
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Mesa: "+mesa.getCodigo()));
            document.add(new Paragraph("Fecha: " +String.valueOf(LocalDate.now())+
            		"                                                                                                    "
            		+ "Hora: "+String.valueOf(formatoHora.format(LocalTime.now(Clock.systemDefaultZone())))));
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));

            Phrase phrase = new Phrase();
            document.add(phrase);
            
            
            document.add(table);
            document.add(new Paragraph("---------------------------------------------------------------------------------------------------------------------------------- "));
            document.add(new Paragraph("                                                                                 Precio total:                      "+df.format(precioTotalServido)+" €"));

            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            
            Paragraph gracias = new Paragraph("Gracias por su visita, vuelva pronto");
            gracias.setAlignment(1);
            document.add(gracias);
            
            document.close();

        } catch (DocumentException ex) {

            logger.error("Error occurred: {0}", ex);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
    
    
    public static ByteArrayInputStream facturaPDF(Pedido pedidoActivo, List<LineaPedido> productos, Negocio negocio, Mesa mesa, Double precioTotalServido) {

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {

        	
        	
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(90);
            table.setWidths(new int[]{3, 3, 3, 3});

            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

            PdfPCell hcell;
            hcell = new PdfPCell(new Phrase("Producto", headFont));
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Cantidad", headFont));
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Precio Ud", headFont));
            table.addCell(hcell);
            
            hcell = new PdfPCell(new Phrase("Total", headFont));
            table.addCell(hcell);

            for (LineaPedido producto : productos) {

                PdfPCell cell;

                cell = new PdfPCell(new Phrase(producto.getNombre()));
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(String.valueOf(producto.getCantidad())));
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(String.valueOf(producto.getPrecio()+" €")));
                table.addCell(cell);
                
                Double precioTotal = producto.getPrecio()*producto.getCantidad();
               
                
                cell = new PdfPCell(new Phrase(df.format(precioTotal)));table.addCell(cell);
            }

            DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm:ss");
            
            PdfWriter.getInstance(document, out);
            document.open();
            
            Paragraph paragraph = new Paragraph(negocio.getNombre().toUpperCase());
            paragraph.setAlignment(1);
            document.add(paragraph);
            
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Mesa: "+mesa.getCodigo()));
            document.add(new Paragraph("Fecha: " +String.valueOf(LocalDate.now())+
            		"                                                                                                    "
            		+ "Hora: "+String.valueOf(formatoHora.format(LocalTime.now(Clock.systemDefaultZone())))));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Datos del cliente:"));
            document.add(new Paragraph("-----------------------------------"));
            document.add(new Paragraph("	Nombre: "+pedidoActivo.getUsuario().getNombre()));
            document.add(new Paragraph("	Apellidos: "+pedidoActivo.getUsuario().getApellidos()));
            document.add(new Paragraph("	DNI: "+pedidoActivo.getUsuario().getDni()));
            document.add(new Paragraph("	Email: "+pedidoActivo.getUsuario().getEmail()));
            document.add(new Paragraph("-----------------------------------"));

            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));

            Phrase phrase = new Phrase();
            document.add(phrase);
            
            
            document.add(table);
            document.add(new Paragraph("---------------------------------------------------------------------------------------------------------------------------------- "));
            document.add(new Paragraph("                                                                                 Precio total:                      "+df.format(precioTotalServido)+" €"));

            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            
            Paragraph gracias = new Paragraph("Gracias por su visita, vuelva pronto");
            gracias.setAlignment(1);
            document.add(gracias);
            
            document.close();

        } catch (DocumentException ex) {

            logger.error("Error occurred: {0}", ex);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
