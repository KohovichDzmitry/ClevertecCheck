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
import lombok.Value;
import ru.clevertec.check.api.exceptions.DaoException;
import ru.clevertec.check.api.exceptions.ServiceException;
import ru.clevertec.check.api.service.IOrderService;
import ru.clevertec.check.custom.CustomList;
import ru.clevertec.check.model.Product;

import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Value
@RequiredArgsConstructor
public class FormatPDF implements Format {

    private static final float TABLE_WIDTH = 300F;
    CustomList<Product> products;
    Integer discount;
    IOrderService orderService;
    OutputStream out;

    @SneakyThrows
    public void setFormat() throws ServiceException {
        try (PdfWriter pdfWriter = new PdfWriter(out); InputStream inputStream = this.getClass().getClassLoader()
                .getResourceAsStream("font/Courier10 Cyr BT.ttf")) {
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            pdfDocument.setDefaultPageSize(PageSize.A4);
            Document document = new Document(pdfDocument);
            PdfFont pdfFont = PdfFontFactory.createFont(Objects.
                    requireNonNull(inputStream).readAllBytes(), PdfEncodings.IDENTITY_H, true);
            document.setFont(pdfFont);
            float[] columnWidth = {TABLE_WIDTH};
            Table table = new Table(columnWidth);
            table.addCell(setTextCenter(NAME_OF_SHOP));
            table.addCell(setTextCenter(ADDRESS));
            table.addCell(setTextCenter(PHONE_NUMBER));
            float[] columnWidth2 = {TABLE_WIDTH / 3, TABLE_WIDTH / 4.5F, TABLE_WIDTH / 1.5F};
            Table table2 = new Table(columnWidth2);
            table2.addCell(setTextLeft(CASHIER));
            table2.addCell(setTextRight());
            table2.addCell(setTextLeft(DATE + DATE_TIME.format(FORMATTER_DATE)));
            table2.addCell(setTextRight());
            table2.addCell(setTextRight());
            table2.addCell(setTextLeft(TIME + DATE_TIME.format(FORMATTER_TIME)));
            float[] columnWidth3 = {TABLE_WIDTH * 0.15F, TABLE_WIDTH * 0.53F, TABLE_WIDTH * 0.16F, TABLE_WIDTH * 0.16F};
            Table table3 = new Table(columnWidth3);
            table3.addCell(setTextLeft(QUANTITY));
            table3.addCell(setTextLeft(NAME));
            table3.addCell(setTextLeft(PRICE));
            table3.addCell(setTextLeft(SUM));
            products.stream()
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
            float[] columnWidth4 = {TABLE_WIDTH * 0.84F, TABLE_WIDTH * 0.16F};
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
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    private Cell setTextCenter(String text) {
        return new Cell().add(text).setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.CENTER);
    }

    private Cell setTextRight() {
        return new Cell().add(Format.EMPTY).setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.RIGHT);
    }

    private Cell setTextLeft(String text) {
        return new Cell().add(text).setBorder(Border.NO_BORDER);
    }
}
