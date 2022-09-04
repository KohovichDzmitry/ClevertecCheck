package ru.clevertec.check.servlet.card;

import com.google.gson.Gson;
import lombok.SneakyThrows;
import ru.clevertec.check.api.exceptions.ServiceException;
import ru.clevertec.check.model.Card;
import ru.clevertec.check.service.CardService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@WebServlet("/api/cards/number")
public class GetByNumberCardServlet extends HttpServlet {

    private final CardService cardService = CardService.getInstance();

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        Integer number = Integer.valueOf(req.getParameter("card_number"));
        try {
            Card card = cardService.getCardByNumber(number);
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
