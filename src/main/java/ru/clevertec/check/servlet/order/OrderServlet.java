package ru.clevertec.check.servlet.order;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import ru.clevertec.check.api.exceptions.ServiceException;
import ru.clevertec.check.custom.CustomList;
import ru.clevertec.check.model.Order;
import ru.clevertec.check.service.OrderService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/order")
public class OrderServlet extends HttpServlet {

    private final OrderService orderService = OrderService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pageSize = req.getParameter("page_size");
        String page = req.getParameter("page");
        try {
            CustomList<Order> order = orderService.findAll(pageSize, page);
            String json = new Gson().toJson(order);
            try (PrintWriter out = resp.getWriter()) {
                out.write(json);
                resp.setStatus(200);
            }
        } catch (ServiceException e) {
            resp.sendError(400, String.valueOf(e));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonObject data = new Gson().fromJson(req.getReader(), JsonObject.class);
        String id_product = data.get("id_product").toString().replaceAll("\"", "");
        String quantity = data.get("quantity").toString().replaceAll("\"", "");
        Map<String, String> orderParameters = new HashMap<>();
        orderParameters.put("id_product", id_product);
        orderParameters.put("quantity", quantity);
        try {
            Order order = orderService.save(orderParameters);
            try (PrintWriter out = resp.getWriter()) {
                out.write(String.valueOf(order));
                resp.setStatus(201);
            }
        } catch (ServiceException e) {
            resp.sendError(400, String.valueOf(e));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonObject data = new Gson().fromJson(req.getReader(), JsonObject.class);
        Long id = Long.valueOf(data.get("order_id").toString().replaceAll("\"", ""));
        String id_product = data.get("id_product").toString().replaceAll("\"", "");
        String quantity = data.get("quantity").toString().replaceAll("\"", "");
        Map<String, String> orderParameters = new HashMap<>();
        orderParameters.put("id_product", id_product);
        orderParameters.put("quantity", quantity);
        try {
            Order order = orderService.update(orderParameters, id);
            try (PrintWriter out = resp.getWriter()) {
                out.write(String.valueOf(order));
                resp.setStatus(200);
            }
        } catch (ServiceException e) {
            resp.sendError(400, String.valueOf(e));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Long.valueOf(req.getParameter("order_id"));
        try {
            orderService.delete(id);
            String json = new Gson().toJson(String.format("Продукт в заказе с id %d был удален", id));
            try (PrintWriter out = resp.getWriter()) {
                out.write(json);
                resp.setStatus(200);
            }
        } catch (ServiceException e) {
            resp.sendError(400, String.valueOf(e));
        }
    }
}
