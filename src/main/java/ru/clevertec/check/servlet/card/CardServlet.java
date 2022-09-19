package ru.clevertec.check.servlet.card;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.clevertec.check.exceptions.ServiceException;
import ru.clevertec.check.service.card.ICardService;
import ru.clevertec.check.custom.CustomList;
import ru.clevertec.check.dto.CardDto;
import ru.clevertec.check.model.Card;

import javax.annotation.PostConstruct;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@WebServlet("/api/cards")
public class CardServlet extends HttpServlet {

    @Autowired
    private ICardService cardService;

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
            CustomList<CardDto> cards = cardService.findAll(pageSize, page);
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
        Integer number = Integer.valueOf(data.get("card_number").toString().replaceAll("\"", ""));
        Integer discount = Integer.parseInt(data.get("discount").toString().replaceAll("\"", ""));
        Card card = Card.builder().number(number).discount(discount).build();
        try {
            CardDto cardDto = cardService.save(card);
            String json = new Gson().toJson(cardDto);
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
        Integer number = Integer.valueOf(req.getParameter("card_number"));
        Integer discount = Integer.valueOf(req.getParameter("discount"));
        Long id = Long.valueOf(req.getParameter("card_id"));
        Card card = Card.builder().number(number).discount(discount).build();
        try {
            CardDto cardDto = cardService.update(card, id);
            String json = new Gson().toJson(cardDto);
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
