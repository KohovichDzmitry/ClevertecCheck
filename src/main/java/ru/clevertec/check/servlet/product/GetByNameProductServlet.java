package ru.clevertec.check.servlet.product;

import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.clevertec.check.api.exceptions.ServiceException;
import ru.clevertec.check.model.Product;
import ru.clevertec.check.service.ProductService;
import ru.clevertec.check.spring.Configuration;

import javax.annotation.PostConstruct;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@WebServlet("/api/products/name")
public class GetByNameProductServlet extends HttpServlet {

    private ProductService productService;

    @PostConstruct
    public void init() {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(Configuration.class);
        productService = context.getBean("productService", ProductService.class);
        context.close();
    }

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String name = req.getParameter("product_name");
        try {
            Product product = productService.getProductByName(name);
            String json = new Gson().toJson(product);
            try (PrintWriter out = resp.getWriter()) {
                out.write(json);
                resp.setStatus(200);
            }
        } catch (ServiceException e) {
            resp.sendError(400, String.valueOf(e));
        }
    }
}
