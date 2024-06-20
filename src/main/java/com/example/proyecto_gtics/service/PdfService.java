package com.example.proyecto_gtics.service;

import com.example.proyecto_gtics.entity.Usuarios;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@Service
public class PdfService {
    public ByteArrayInputStream generarPdfUsuarios(List<Usuarios> listaUsuarios, String titulo) {
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Añadir título
            Font font = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
            Paragraph para = new Paragraph(titulo, font);
            para.setAlignment(Element.ALIGN_CENTER);
            document.add(para);
            document.add(new Paragraph(" ")); // Espacio

            // Crear tabla
            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{ 2.3f, 1.5f, 1.5f, 1.5f, 1.7f, 3.5f});

            // Añadir cabecera
            Font headFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            PdfPCell hcell;
            BaseColor bgColor = new BaseColor(221, 234, 255); // Color de fondo

            hcell = new PdfPCell(new Phrase("Nombre", headFont));
            hcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            hcell.setBackgroundColor(bgColor);
            hcell.setFixedHeight(25f);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Estado", headFont));
            hcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            hcell.setBackgroundColor(bgColor);
            hcell.setFixedHeight(25f);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Sede", headFont));
            hcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            hcell.setBackgroundColor(bgColor);
            hcell.setFixedHeight(25f);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("DNI", headFont));
            hcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            hcell.setBackgroundColor(bgColor);
            hcell.setFixedHeight(25f);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Distrito", headFont));
            hcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            hcell.setBackgroundColor(bgColor);
            hcell.setFixedHeight(25f);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Correo", headFont));
            hcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            hcell.setBackgroundColor(bgColor);
            hcell.setFixedHeight(25f);
            table.addCell(hcell);

            // Añadir datos de la tabla

            Font dataFont = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL);

            for (Usuarios adminSede : listaUsuarios) {
                PdfPCell cell;

                cell = new PdfPCell(new Phrase(adminSede.getNombre()));
                cell.setPaddingLeft(5);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setFixedHeight(20f);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(adminSede.getEstadoUsuario().getIdEstadoUsuario()));
                cell.setPaddingLeft(5);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setFixedHeight(20f);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(adminSede.getSedes().getNombre()));
                cell.setPaddingLeft(5);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell.setFixedHeight(20f);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(String.valueOf(adminSede.getDni())));
                cell.setPaddingLeft(5);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setFixedHeight(20f);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(adminSede.getDistritoResidencia()));
                cell.setPaddingLeft(5);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setFixedHeight(20f);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(adminSede.getCorreo()));
                cell.setPaddingLeft(5);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                cell.setFixedHeight(20f);
                table.addCell(cell);
            }

            document.add(table);
            document.close();
        } catch (DocumentException ex) {
            ex.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
