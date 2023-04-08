package ru.clevertec.servlets.util;

import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import lombok.SneakyThrows;
import ru.clevertec.servlets.model.Cheque;
import ru.clevertec.servlets.model.ChequeItem;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class PdfConverter {

    @SneakyThrows
    public void chequeToPdf(Cheque cheque) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream input = classLoader.getResourceAsStream("pdf\\ChequeTemplate.pdf");
        PdfReader reader = new PdfReader(input);
        PdfStamper pdfStamper = new PdfStamper(reader, new FileOutputStream("Cheque.pdf"));
        BaseFont baseFont = BaseFont.createFont(
                BaseFont.TIMES_ROMAN,
                BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
        PdfContentByte content = pdfStamper.getOverContent(1);
        content.setFontAndSize(baseFont, 14);
        content.beginText();
        content.showTextAligned(PdfContentByte.ALIGN_LEFT, "Cash receipt", 235, 650, 0);
        content.showTextAligned(PdfContentByte.ALIGN_LEFT, "SUPERMARKET 123", 100, 620, 0);
        content.showTextAligned(PdfContentByte.ALIGN_LEFT, "i2, MILKYWAY Galaxy/Earth", 100, 605, 0);
        content.showTextAligned(PdfContentByte.ALIGN_LEFT, "TEL: +375-25-999-99-99", 100, 590, 0);
        content.showTextAligned(PdfContentByte.ALIGN_LEFT, "CASHIER: Andrei Yurueu", 100, 575, 0);
        content.showTextAligned(PdfContentByte.ALIGN_LEFT, "DATE: " + LocalDate.now(), 320, 575, 0);
        content.showTextAligned(PdfContentByte.ALIGN_LEFT, "TIME: " + LocalTime.now().truncatedTo(ChronoUnit.SECONDS), 320, 560, 0);

        content.showTextAligned(PdfContentByte.ALIGN_LEFT, "QTY", 100, 530, 0);
        content.showTextAligned(PdfContentByte.ALIGN_LEFT, "DESCRIPTION", 150, 530, 0);
        content.showTextAligned(PdfContentByte.ALIGN_LEFT, "PRICE", 300, 530, 0);
        content.showTextAligned(PdfContentByte.ALIGN_LEFT, "TOTAL", 400, 530, 0);
        int y = 500;
        for (ChequeItem chequeItem : cheque.getProducts()) {
            content.showTextAligned(PdfContentByte.ALIGN_LEFT, String.valueOf(chequeItem.getQuantity()), 100, y, 0);
            content.showTextAligned(PdfContentByte.ALIGN_LEFT, String.valueOf(chequeItem.getItemName()), 150, y, 0);
            content.showTextAligned(PdfContentByte.ALIGN_LEFT, chequeItem.getPrice() + "$", 300, y, 0);
            content.showTextAligned(PdfContentByte.ALIGN_LEFT, chequeItem.getTotal() + "$", 400, y, 0);
            y -= 20;
        }
        content.showTextAligned(PdfContentByte.ALIGN_LEFT, "TOTAL", 100, y - 30, 0);
        content.showTextAligned(PdfContentByte.ALIGN_LEFT, "DISCOUNT", 100, y - 50, 0);
        content.showTextAligned(PdfContentByte.ALIGN_LEFT, "TOTAL WITH DISCOUNT", 100, y - 70, 0);

        content.showTextAligned(PdfContentByte.ALIGN_LEFT, cheque.getTotal() + "$", 400, y - 30, 0);
        content.showTextAligned(PdfContentByte.ALIGN_LEFT, cheque.getDiscount() + "$", 400, y - 50, 0);
        content.showTextAligned(PdfContentByte.ALIGN_LEFT, cheque.getTotalWithDiscount() + "$", 400, y - 70, 0);
        content.endText();
        pdfStamper.close();
    }
}
