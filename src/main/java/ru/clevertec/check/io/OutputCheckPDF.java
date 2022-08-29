package ru.clevertec.check.io;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import ru.clevertec.check.service.OrderService;

import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalTime;

public class OutputCheckPDF {

    private final OrderService orderService;
    private final OutputStream out;
    private final LocalDate date = LocalDate.now();
    private final LocalTime time = LocalTime.now();

    private static final float TABLE_WIDTH = 300F;

    public OutputCheckPDF(OrderService orderService, OutputStream out) {
        this.orderService = orderService;
        this.out = out;
    }

    public void printCheckPDF(Long card_id) {
        PdfWriter pdfWriter = new PdfWriter(out);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        pdfDocument.setDefaultPageSize(PageSize.A4);
        Document document = new Document(pdfDocument);
        float[] columnWidth = {TABLE_WIDTH};
        Table table = new Table(columnWidth);
        table.addCell("ADSADSASD");
//        table.addCell(setTextCenter("\t\t\t  -=Магазин 777=-"));
//        table.addCell(setTextCenter("\t\t г.Минск, ул. Макаёнка 99"));
//        table.addCell(setTextCenter("\t\t   тел. 8017-123-45-67 \n"));
//        table.addCell(setTextCenter("\t кассир: №1234 \t дата: " + date));
//        table.addCell(setTextCenter("время: " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond()));
//        table.addCell(setTextCenter("-----------------------------------------"));
//        table.addCell(setTextCenter("Кол.\t" + "Наименование\t\t" + "Цена\t" + "Сумма"));


        document.add(table);
//        document.add(table2);
//        document.add(new Paragraph("-".repeat(73)));
//        document.add(table3);
//        document.add(new Paragraph("=".repeat(42)));
//        document.add(table4);
        document.close();

//        pw.println("\t\t\t  -=Магазин 777=-");
//            pw.println("\t\t г.Минск, ул. Макаёнка 99");
//            pw.println("\t\t   тел. 8017-123-45-67 \n");
//            pw.println("\t кассир: №1234 \t дата: " + date);
//            pw.println("\t\t\t\t\t время: " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond());
//            pw.println("-----------------------------------------");
//            pw.println("Кол.\t" + "Наименование\t\t" + "Цена\t" + "Сумма");
//            orderService.printProductFromTheOrder(pw);
//            pw.println("=========================================");
//            orderService.printEndingCheck(pw, card_id);
//            pw.println("*****************************************");
//            pw.println("\t\t\tCпасибо за покупку!");

    }



























//    public void setFormat() {
//        PdfWriter pdfWriter = new PdfWriter(out);
//        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
//        pdfDocument.setDefaultPageSize(PageSize.A4);
//        Document document = new Document(pdfDocument);
//        float[] columnWidth = {TABLE_WIDTH};
//        Table table = new Table(columnWidth);
//        table.addCell(setTextCenter(RECEIPT2));
//        table.addCell(setTextCenter(PropertiesUtil.get("check.supermarket")));
//        table.addCell(setTextCenter(PropertiesUtil.get("check.address")));
//        table.addCell(setTextCenter(PropertiesUtil.get("check.phone")));
//        float[] columnWidth2 = {TABLE_WIDTH / 2, TABLE_WIDTH / 2};
//        Table table2 = new Table(columnWidth2);
//        table2.addCell(setTextLeft(CASHIER2));
//        table2.addCell(setTextLeft(DATE2));
//        table2.addCell(setTextLeft(EMPTY));
//        table2.addCell(setTextLeft(TIME2));
//        float[] columnWidth3 = {TABLE_WIDTH * 0.1F, TABLE_WIDTH * 0.4F, TABLE_WIDTH * 0.18F,
//                TABLE_WIDTH * 0.17F, TABLE_WIDTH * 0.15F};
//        Table table3 = new Table(columnWidth3);
//        table3.addCell(setTextLeft(QTY));
//        table3.addCell(setTextLeft(DESCRIPTION));
//        table3.addCell(setTextLeft(EMPTY));
//        table3.addCell(setTextRight(PRICE));
//        table3.addCell(setTextRight(TOTAL));
//        for (int i = 0; i < list.size(); i++) {
//            if (list.get(i).isPromotion() && list.get(i).getQuantity()
//                    > Integer.parseInt(PropertiesUtil.get("check.quantity.discount"))) {
//                table3.addCell(setTextLeft(String.valueOf(list.get(i).getQuantity())));
//                table3.addCell(setTextLeft(list.get(i).getName()));
//                table3.addCell(setTextLeft(SALE));
//                table3.addCell(setTextRight(String.valueOf(list.get(i).getPrice()
//                        .multiply(DISCOUNT_VALUE)
//                        .setScale(2, RoundingMode.HALF_UP))));
//                table3.addCell(setTextRight(String.valueOf(list.get(i).getPrice()
//                        .multiply(DISCOUNT_VALUE)
//                        .setScale(2, RoundingMode.HALF_UP)
//                        .multiply(BigDecimal.valueOf(list.get(i).getQuantity())))));
//            } else {
//                table3.addCell(setTextLeft(String.valueOf(list.get(i).getQuantity())));
//                table3.addCell(setTextLeft(list.get(i).getName()));
//                table3.addCell(setTextLeft(EMPTY));
//                table3.addCell(setTextRight(String.valueOf(list.get(i).getPrice()
//                        .multiply(new BigDecimal(ONE_HUNDRED)
//                                .subtract(new BigDecimal(discount))
//                                .divide(new BigDecimal(ONE_HUNDRED)))
//                        .setScale(2, RoundingMode.HALF_UP))));
//                table3.addCell(setTextRight(String.valueOf(list.get(i).getPrice()
//                        .multiply(new BigDecimal(ONE_HUNDRED)
//                                .subtract(new BigDecimal(discount))
//                                .divide(new BigDecimal(ONE_HUNDRED)))
//                        .setScale(2, RoundingMode.HALF_UP)
//                        .multiply(BigDecimal.valueOf(list.get(i).getQuantity())))));
//            }
//        }
//        float[] columnWidth4 = {TABLE_WIDTH * 0.67F, TABLE_WIDTH * 0.33F};
//        Table table4 = new Table(columnWidth4);
//        table4.addCell(setTextLeft(TAXABLE_TOT));
//        table4.addCell(setTextRight(String.valueOf(value)));
//        document.add(table);
//        document.add(table2);
//        document.add(new Paragraph("-".repeat(73)));
//        document.add(table3);
//        document.add(new Paragraph("=".repeat(42)));
//        document.add(table4);
//        document.close();
//    }

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
