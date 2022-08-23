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

@WebServlet("/api/products/name")
public class GetByNameProductServlet extends HttpServlet {

    private final ProductService productService = ProductService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("product_name");
        try {
            Product product = productService.getProductByName(name);
            String json = new Gson().toJson(product);
            try (PrintWriter out = resp.getWriter()) {
                out.write(json);
                resp.setStatus(200);
            }
        } catch (ServiceException e) {
            resp.sendError(400, String.format("Не удалось найти продукт по введённому названию: %s", name));
        }
    }
}
