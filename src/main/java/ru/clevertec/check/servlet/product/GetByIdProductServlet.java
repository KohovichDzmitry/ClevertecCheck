package ru.clevertec.check.servlet.product;

import com.google.gson.Gson;
import ru.clevertec.check.api.exceptions.ServiceException;
import ru.clevertec.check.model.Product;
import ru.clevertec.check.service.ProductService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/api/products/id")
public class GetByIdProductServlet extends HttpServlet {

    private final ProductService productService = ProductService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Long.valueOf(req.getParameter("product_id"));
        try {
            Product product = productService.getById(id);
            String json = new Gson().toJson(product);
            try (PrintWriter out = resp.getWriter()) {
                out.write(json);
                resp.setStatus(200);
            }
        } catch (ServiceException e) {
            resp.sendError(400, String.format("Не удалось найти продукт по введённому id: %d", id));
        }
    }
}
