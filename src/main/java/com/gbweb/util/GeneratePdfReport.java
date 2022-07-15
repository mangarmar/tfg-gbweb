package com.gbweb.util;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

import com.gbweb.entity.LineaPedido;

public class GeneratePdfReport {

    private static final Logger logger = LoggerFactory.getLogger(GeneratePdfReport.class);

    public static ByteArrayInputStream cuentaPDF(List<LineaPedido> productos) {

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

                cell = new PdfPCell(new Phrase(String.valueOf(producto.getPrecio())));
                table.addCell(cell);
                
                cell = new PdfPCell(new Phrase(String.valueOf((producto.getPrecio()*producto.getCantidad()))));
                table.addCell(cell);
            }

            PdfWriter.getInstance(document, out);
            document.open();
            document.add(table);

            document.close();

        } catch (DocumentException ex) {

            logger.error("Error occurred: {0}", ex);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
