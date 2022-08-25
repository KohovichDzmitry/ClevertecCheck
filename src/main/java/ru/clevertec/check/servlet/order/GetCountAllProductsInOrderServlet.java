package ru.clevertec.check.servlet.order;

import com.google.gson.Gson;
import ru.clevertec.check.api.exceptions.ServiceException;
import ru.clevertec.check.service.OrderService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/api/order/count")
public class GetCountAllProductsInOrderServlet extends HttpServlet {

    private final OrderService orderService = OrderService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Integer countAllProductsInOrder = orderService.countAllEntities();
            String json = new Gson().toJson(countAllProductsInOrder);
            try (PrintWriter out = resp.getWriter()) {
                out.write(json);
                resp.setStatus(200);
            }
        } catch (ServiceException e) {
            resp.sendError(400, String.valueOf(e));
        }
    }
}
