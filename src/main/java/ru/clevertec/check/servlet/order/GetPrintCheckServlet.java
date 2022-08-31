package ru.clevertec.check.servlet.order;

import com.itextpdf.text.DocumentException;
import ru.clevertec.check.api.exceptions.ServiceException;
import ru.clevertec.check.io.OutputCheckPDF;
import ru.clevertec.check.service.OrderService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@WebServlet("/api/order/print")
public class GetPrintCheckServlet extends HttpServlet {

    private final OrderService orderService = OrderService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Long card_id = Long.valueOf(req.getParameter("card_id"));
            resp.setContentType("application/pdf");
            resp.setHeader("Content-Disposition", "attachment; filename=\"check.pdf\"");
            try (OutputStream out = resp.getOutputStream()) {
                OutputCheckPDF outputCheckPDF = new OutputCheckPDF(orderService, out);
                outputCheckPDF.printCheckPDF(card_id);
                resp.setStatus(200);
            } catch (DocumentException e) {
                throw new RuntimeException(e);
            }
        } catch (ServiceException e) {
            resp.sendError(400, String.valueOf(e));
        }
    }
}
