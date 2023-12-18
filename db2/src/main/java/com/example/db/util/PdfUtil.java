package com.example.db.util;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Component
public class PdfUtil {

    public byte[] createBalanceOperationsTablePdf(List<BalanceOperations> data){
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);
            document.open();
            document.add(new Paragraph("Operations on balances:\n "));
            PdfPTable table = new PdfPTable(2);
            table.addCell("Balance id");
            table.addCell("Amount of operations");
            for (BalanceOperations balanceOperations : data) {
                table.addCell(String.valueOf(balanceOperations.getB_id()));
                table.addCell(String.valueOf(balanceOperations.getOperations_count()));
            }
            document.add(table);
            document.close();
            return outputStream.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] createTablePdf(List<UncalculatedOperations> data){
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);
            document.open();
            document.add(new Paragraph("Uncalculated operations:\n "));
            PdfPTable table = new PdfPTable(2);
            table.addCell("Article id");
            table.addCell("Total");
            for (UncalculatedOperations uncalculatedOperations : data) {
                table.addCell(String.valueOf(uncalculatedOperations.getArt_name()));
                table.addCell(String.valueOf(uncalculatedOperations.getTotal()));
            }
            document.add(table);
            document.close();
            return outputStream.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
