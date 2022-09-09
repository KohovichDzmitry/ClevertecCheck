package ru.clevertec.check.servlet.card;

import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.clevertec.check.api.exceptions.ServiceException;
import ru.clevertec.check.model.Card;
import ru.clevertec.check.service.CardService;
import ru.clevertec.check.spring.Configuration;

import javax.annotation.PostConstruct;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@WebServlet("/api/cards/id")
public class GetByIdCardServlet extends HttpServlet {

    private CardService cardService;

    @PostConstruct
    public void init() {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(Configuration.class);
        cardService = context.getBean("cardService", CardService.class);
        context.close();
    }

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        Long id = Long.valueOf(req.getParameter("card_id"));
        try {
            Card card = cardService.getById(id);
            String json = new Gson().toJson(card);
            try (PrintWriter out = resp.getWriter()) {
                out.write(json);
                resp.setStatus(200);
            }
        } catch (ServiceException e) {
            resp.sendError(400, String.valueOf(e));
        }
    }
}
