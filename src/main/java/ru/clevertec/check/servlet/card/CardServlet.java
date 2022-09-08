package ru.clevertec.check.servlet.card;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.SneakyThrows;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.clevertec.check.api.exceptions.ServiceException;
import ru.clevertec.check.custom.CustomList;
import ru.clevertec.check.model.Card;
import ru.clevertec.check.service.CardService;
import ru.clevertec.check.spring.Configuration;

import javax.annotation.PostConstruct;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/cards")
public class CardServlet extends HttpServlet {

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
        String pageSize = req.getParameter("pageSize");
        String page = req.getParameter("page");
        try {
            CustomList<Card> cards = cardService.findAll(pageSize, page);
            String json = new Gson().toJson(cards);
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
        String number = data.get("card_number").toString().replaceAll("\"", "");
        String discount = data.get("discount").toString().replaceAll("\"", "");
        Map<String, String> cardParameters = new HashMap<>();
        cardParameters.put("card_number", number);
        cardParameters.put("discount", discount);
        try {
            Card card = cardService.save(cardParameters);
            try (PrintWriter out = resp.getWriter()) {
                out.write(String.valueOf(card));
                resp.setStatus(201);
            }
        } catch (ServiceException e) {
            resp.sendError(400, String.valueOf(e));
        }
    }

    @SneakyThrows
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        JsonObject data = new Gson().fromJson(req.getReader(), JsonObject.class);
        Long id = Long.valueOf(data.get("card_id").toString().replaceAll("\"", ""));
        String number = data.get("card_number").toString().replaceAll("\"", "");
        String discount = data.get("discount").toString().replaceAll("\"", "");
        Map<String, String> cardParameters = new HashMap<>();
        cardParameters.put("card_number", number);
        cardParameters.put("discount", discount);
        try {
            Card card = cardService.update(cardParameters, id);
            try (PrintWriter out = resp.getWriter()) {
                out.write(String.valueOf(card));
                resp.setStatus(200);
            }
        } catch (ServiceException e) {
            resp.sendError(400, String.valueOf(e));
        }
    }

    @SneakyThrows
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        Long id = Long.valueOf(req.getParameter("card_id"));
        try {
            cardService.delete(id);
            String json = new Gson().toJson(String.format("Скидочная карта с id %d была удалена", id));
            try (PrintWriter out = resp.getWriter()) {
                out.write(json);
                resp.setStatus(200);
            }
        } catch (ServiceException e) {
            resp.sendError(400, String.valueOf(e));
        }
    }
}
