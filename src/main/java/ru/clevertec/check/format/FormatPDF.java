package ru.clevertec.check.format;

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
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.clevertec.check.entity.Product;
import ru.clevertec.check.exceptions.ServiceException;
import ru.clevertec.check.service.IOrderService;
import ru.clevertec.check.service.impl.CheckInfo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static ru.clevertec.check.util.Constant.*;

@Component
@RequiredArgsConstructor
public class FormatPDF implements Format {

    @Value("${check.tableWidth}")
    private final float tableWidth;
    @Value("${check.pathPdf}")
    private final String pathPdf;
    @Value("${check.font}")
    private final String font;
    private final IOrderService orderService;

    @SneakyThrows
    @Override
    public ResponseEntity<byte[]> setFormat(Map<String, String[]> map) throws ServiceException {
        CheckInfo checkInfo = orderService.readCheck(map);
        List<Product> products = checkInfo.getItemList();
        int discount = checkInfo.getDiscount();
        PdfWriter pdfWriter = new PdfWriter(pathPdf);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        pdfDocument.setDefaultPageSize(PageSize.A4);
        Document document = new Document(pdfDocument);
        PdfFont pdfFont = PdfFontFactory.createFont(font, PdfEncodings.IDENTITY_H, true);
        document.setFont(pdfFont);
        float[] columnWidth = {tableWidth};
        Table table = new Table(columnWidth);
        table.addCell(setTextCenter(NAME_OF_SHOP));
        table.addCell(setTextCenter(ADDRESS));
        table.addCell(setTextCenter(PHONE_NUMBER));
        float[] columnWidth2 = {tableWidth / 3, tableWidth / 4.5F, tableWidth / 1.5F};
        Table table2 = new Table(columnWidth2);
        table2.addCell(setTextLeft(CASHIER));
        table2.addCell(setTextRight());
        table2.addCell(setTextLeft(DATE + DATE_TIME.format(FORMATTER_DATE)));
        table2.addCell(setTextRight());
        table2.addCell(setTextRight());
        table2.addCell(setTextLeft(TIME + DATE_TIME.format(FORMATTER_TIME)));
        float[] columnWidth3 = {tableWidth * 0.15F, tableWidth * 0.53F, tableWidth * 0.16F, tableWidth * 0.16F};
        Table table3 = new Table(columnWidth3);
        table3.addCell(setTextLeft(QUANTITY));
        table3.addCell(setTextLeft(NAME));
        table3.addCell(setTextLeft(PRICE));
        table3.addCell(setTextLeft(SUM));
        products
                .forEach(product -> {
                    table3.addCell(new Cell().add(String.valueOf(product.getQuantity())).setBorder(Border.NO_BORDER));
                    table3.addCell(new Cell().add(product.getName()).setBorder(Border.NO_BORDER));
                    table3.addCell(new Cell().add(String.valueOf(product.getPrice())).setBorder(Border.NO_BORDER));
                    table3.addCell(new Cell().add(String.valueOf(BigDecimal.valueOf(orderService
                                    .getProductStockCost(products, product.getId(), false) * product.getQuantity())
                            .setScale(2, RoundingMode.HALF_UP).doubleValue())).setBorder(Border.NO_BORDER));
                    if (product.getStock() == 1 && orderService.numberOfProductsFromOrderWithStock(products) > 4) {
                        table3.addCell(new Cell().add("(на ").setBorder(Border.NO_BORDER)
                                .setTextAlignment(TextAlignment.RIGHT));
                        table3.addCell(new Cell().add("товар \"" + product.getName() + "\" акция ")
                                .setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.JUSTIFIED_ALL));
                        table3.addCell(new Cell().add("-10%)").setBorder(Border.NO_BORDER));
                        table3.addCell(new Cell().add(String.valueOf(BigDecimal.valueOf(orderService
                                        .getProductStockCost(products, product.getId(), true) * product.getQuantity())
                                .setScale(2, RoundingMode.HALF_UP).doubleValue())).setBorder(Border.NO_BORDER));
                    }
                });
        float[] columnWidth4 = {tableWidth * 0.84F, tableWidth * 0.16F};
        Table table4 = new Table(columnWidth4);
        table4.addCell(new Cell().add(SUM).setBorder(Border.NO_BORDER));
        table4.addCell(new Cell().add(String.valueOf(BigDecimal.valueOf(orderService.getTotalSum(products))
                .setScale(2, RoundingMode.HALF_UP).doubleValue())).setBorder(Border.NO_BORDER));
        table4.addCell(new Cell().add(STOCK).setBorder(Border.NO_BORDER));
        table4.addCell(new Cell().add(discount + "%").setBorder(Border.NO_BORDER));
        table4.addCell(new Cell().add(SUM_WITH_STOCK).setBorder(Border.NO_BORDER));
        table4.addCell(new Cell().add(String.valueOf(BigDecimal.valueOf(orderService.getTotalSum(products) *
                        ((100 - discount)) / 100).setScale(2, RoundingMode.HALF_UP)
                .doubleValue())).setBorder(Border.NO_BORDER));
        document.add(table);
        document.add(table2);
        document.add(new Paragraph("-".repeat(41)));
        document.add(table3);
        document.add(new Paragraph("=".repeat(41)));
        document.add(table4);
        document.add(new Paragraph("*".repeat(41)));
        document.add(new Table(columnWidth).addCell(setTextCenter(THANKS)));
        document.close();
        byte[] content = Files.readAllBytes(Path.of(pathPdf));
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(content.length)
                .body(content);
    }

    private Cell setTextCenter(String text) {
        return new Cell().add(text).setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.CENTER);
    }

    private Cell setTextRight() {
        return new Cell().add(EMPTY).setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.RIGHT);
    }

    private Cell setTextLeft(String text) {
        return new Cell().add(text).setBorder(Border.NO_BORDER);
    }
}
