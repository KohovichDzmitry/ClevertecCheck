package ru.clevertec.check.servlet.product;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import ru.clevertec.check.api.exceptions.ServiceException;
import ru.clevertec.check.custom.CustomList;
import ru.clevertec.check.model.Product;
import ru.clevertec.check.service.ProductService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/products")
public class ProductServlet extends HttpServlet {

    private final ProductService productService = ProductService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pageSize = req.getParameter("page_size");
        String page = req.getParameter("page");
        try {
            CustomList<Product> products = productService.findAll(pageSize, page);
            String json = new Gson().toJson(products);
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
        String name = data.get("product_name").toString().replaceAll("\"", "");
        String price = data.get("price").toString().replaceAll("\"", "");
        String stock = data.get("stock").toString().replaceAll("\"", "");
        Map<String, String> productParameters = new HashMap<>();
        productParameters.put("product_name", name);
        productParameters.put("price", price);
        productParameters.put("stock", stock);
        try {
            Product product = productService.save(productParameters);
            try (PrintWriter out = resp.getWriter()) {
                out.write(String.valueOf(product));
                resp.setStatus(201);
            }
        } catch (ServiceException e) {
            resp.sendError(400, String.valueOf(e));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonObject data = new Gson().fromJson(req.getReader(), JsonObject.class);
        Long id = Long.valueOf(data.get("product_id").toString().replaceAll("\"", ""));
        String name = data.get("product_name").toString().replaceAll("\"", "");
        String price = data.get("price").toString().replaceAll("\"", "");
        String stock = data.get("stock").toString().replaceAll("\"", "");
        Map<String, String> productParameters = new HashMap<>();
        productParameters.put("product_name", name);
        productParameters.put("price", price);
        productParameters.put("stock", stock);
        try {
            Product product = productService.update(productParameters, id);
            try (PrintWriter out = resp.getWriter()) {
                out.write(String.valueOf(product));
                resp.setStatus(200);
            }
        } catch (ServiceException e) {
            resp.sendError(400, String.valueOf(e));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Long.valueOf(req.getParameter("product_id"));
        try {
            productService.delete(id);
            String json = new Gson().toJson(String.format("Продукт с id %d был удален", id));
            try (PrintWriter out = resp.getWriter()) {
                out.write(json);
                resp.setStatus(200);
            }
        } catch (ServiceException e) {
            resp.sendError(400, String.valueOf(e));
        }
    }
}
