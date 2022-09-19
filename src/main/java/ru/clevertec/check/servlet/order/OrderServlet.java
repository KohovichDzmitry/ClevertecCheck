package ru.clevertec.check.servlet.order;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.clevertec.check.exceptions.ServiceException;
import ru.clevertec.check.service.order.IOrderService;

import javax.annotation.PostConstruct;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.Map;

@WebServlet("/api/order")
public class OrderServlet extends HttpServlet {

    @Autowired
    private IOrderService orderService;

    @PostConstruct
    public void init() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

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
