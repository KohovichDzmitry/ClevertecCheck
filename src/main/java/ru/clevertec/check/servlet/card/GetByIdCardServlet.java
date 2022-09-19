package ru.clevertec.check.servlet.card;

import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.clevertec.check.exceptions.ServiceException;
import ru.clevertec.check.service.card.ICardService;
import ru.clevertec.check.dto.CardDto;

import javax.annotation.PostConstruct;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@WebServlet("/api/cards/id")
public class GetByIdCardServlet extends HttpServlet {

    @Autowired
    private ICardService cardService;

    @PostConstruct
    public void init() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        Long id = Long.valueOf(req.getParameter("card_id"));
        try {
            CardDto cardDto = cardService.findById(id);
            String json = new Gson().toJson(cardDto);
            try (PrintWriter out = resp.getWriter()) {
                out.write(json);
                resp.setStatus(200);
            }
        } catch (ServiceException e) {
            resp.sendError(400, String.valueOf(e));
        }
    }
}
