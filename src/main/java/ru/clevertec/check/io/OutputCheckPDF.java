package ru.clevertec.check.io;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.text.DocumentException;
import ru.clevertec.check.service.OrderService;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalTime;

public class OutputCheckPDF {

    private final OrderService orderService;
    private final OutputStream out;
    private final LocalDate date = LocalDate.now();
    private final LocalTime time = LocalTime.now();

    private static final float TABLE_WIDTH = 300F;
    public static final String FONT = "C:\\Users\\Dmitry\\IdeaProjects\\Clevertec\\src\\main\\resources\\Courier10 Cyr BT.ttf";

    public OutputCheckPDF(OrderService orderService, OutputStream out) {
        this.orderService = orderService;
        this.out = out;
    }

    public void printCheckPDF(Long card_id) throws DocumentException, IOException {
        try (PdfWriter pdfWriter = new PdfWriter(out)) {
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            pdfDocument.setDefaultPageSize(PageSize.A4);
            Document document = new Document(pdfDocument);
            PdfFont pdfFont = PdfFontFactory.createFont(FONT, PdfEncodings.IDENTITY_H, true);
            document.setFont(pdfFont);
            float[] columnWidth = {TABLE_WIDTH};
            Table table = new Table(columnWidth);
            table.addCell(setTextCenter("г.Минск, ул. Макаёнка 99"));
            table.addCell(setTextCenter("тел. 8017-123-45-67"));
            float[] columnWidth2 = {TABLE_WIDTH / 3, TABLE_WIDTH / 4.5F, TABLE_WIDTH / 1.5F};
            Table table2 = new Table(columnWidth2);
            table2.addCell(setTextLeft("кассир: №1234"));
            table2.addCell(setTextRight(""));
            table2.addCell(setTextLeft("дата: " + date));
            table2.addCell(setTextRight(""));
            table2.addCell(setTextRight(""));
            table2.addCell(setTextLeft("время: " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond()));
            float[] columnWidth3 = {TABLE_WIDTH * 0.15F, TABLE_WIDTH * 0.53F, TABLE_WIDTH * 0.16F, TABLE_WIDTH * 0.16F};
            Table table3 = new Table(columnWidth3);
            table3.addCell(setTextLeft("Кол."));
            table3.addCell(setTextLeft("Наименование"));
            table3.addCell(setTextLeft("Цена"));
            table3.addCell(setTextLeft("Сумма"));
            orderService.printProductFromTheOrderPDF(table3);
            float[] columnWidth4 = {TABLE_WIDTH * 0.84F, TABLE_WIDTH * 0.16F};
            Table table4 = new Table(columnWidth4);
            orderService.printEndingCheckPDF(table4, card_id);
            document.add(table);
            document.add(table2);
            document.add(new Paragraph("-".repeat(41)));
            document.add(table3);
            document.add(new Paragraph("=".repeat(41)));
            document.add(table4);
            document.add(new Paragraph("*".repeat(41)));
            document.add(new Table(columnWidth).addCell(setTextCenter("Cпасибо за покупку!")));
            document.close();
        }
    }

    public static Cell setTextCenter(String text) {
        return new Cell().add(text).setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.CENTER);
    }

    public static Cell setTextRight(String text) {
        return new Cell().add(text).setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.RIGHT);
    }

    public static Cell setTextLeft(String text) {
        return new Cell().add(text).setBorder(Border.NO_BORDER);
    }
}
