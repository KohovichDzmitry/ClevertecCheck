package ru.clevertec.check.servlet.order;

import lombok.SneakyThrows;
import ru.clevertec.check.api.exceptions.ServiceException;
import ru.clevertec.check.service.OrderService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.Map;

@WebServlet("/api/order")
public class OrderServlet extends HttpServlet {

    private final OrderService orderService = OrderService.getInstance();

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType("application/pdf");
        resp.setHeader("Content-Disposition", "attachment; filename=\"check.pdf\"");
        try (OutputStream out = resp.getOutputStream()) {
            Map<String, String[]> map = req.getParameterMap();
            orderService.printCheck(map, out);
            resp.setStatus(200);
        } catch (ServiceException e) {
            resp.sendError(400, String.valueOf(e));
        }
    }
}
