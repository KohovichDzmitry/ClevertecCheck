package ru.clevertec.check.servlet.product;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.clevertec.check.exceptions.ServiceException;
import ru.clevertec.check.service.product.IProductService;
import ru.clevertec.check.custom.CustomList;
import ru.clevertec.check.dto.ProductDto;
import ru.clevertec.check.model.Product;

import javax.annotation.PostConstruct;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@WebServlet("/api/products")
public class ProductServlet extends HttpServlet {

    @Autowired
    private IProductService productService;

    @PostConstruct
    public void init() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String pageSize = req.getParameter("pageSize");
        String page = req.getParameter("page");
        try {
            CustomList<ProductDto> products = productService.findAll(pageSize, page);
            String json = new Gson().toJson(products);
            try (PrintWriter out = resp.getWriter()) {
                out.write(json);
                resp.setStatus(200);
            }
        } catch (ServiceException e) {
            resp.sendError(400, String.valueOf(e));
        }
    }

    @SneakyThrows
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        JsonObject data = new Gson().fromJson(req.getReader(), JsonObject.class);
        String name = data.get("product_name").toString().replaceAll("\"", "");
        Double price = Double.parseDouble(data.get("price").toString().replaceAll("\"", ""));
        Integer stock = Integer.parseInt(data.get("stock").toString().replaceAll("\"", ""));
        Product product = Product.builder().name(name).price(price).stock(stock).build();
        try {
            ProductDto productDto = productService.save(product);
            String json = new Gson().toJson(productDto);
            try (PrintWriter out = resp.getWriter()) {
                out.write(String.valueOf(json));
                resp.setStatus(201);
            }
        } catch (ServiceException e) {
            resp.sendError(400, String.valueOf(e));
        }
    }

    @SneakyThrows
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        String name = req.getParameter("product_name");
        Double price = Double.valueOf(req.getParameter("price"));
        Integer stock = Integer.valueOf(req.getParameter("stock"));
        Long id = Long.valueOf(req.getParameter("product_id"));
        Product product = Product.builder().name(name).price(price).stock(stock).build();
        try {
            ProductDto productDto = productService.update(product, id);
            String json = new Gson().toJson(productDto);
            try (PrintWriter out = resp.getWriter()) {
                out.write(String.valueOf(json));
                resp.setStatus(200);
            }
        } catch (ServiceException e) {
            resp.sendError(400, String.valueOf(e));
        }
    }

    @SneakyThrows
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
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
