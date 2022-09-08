package ru.clevertec.check.service.handler;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.clevertec.check.annotation.Log;
import ru.clevertec.check.api.service.IOrderService;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@Value
@RequiredArgsConstructor
public class OrderServiceHandler implements InvocationHandler {

    IOrderService orderService;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Logger logger = LogManager.getLogger(method.getDeclaringClass().getName());
        Gson gson = new Gson();
        Object invoke = method.invoke(orderService, args);
        if (method.getAnnotation(Log.class) != null ||
                orderService.getClass().getMethod(method.getName(), method.getParameterTypes())
                        .getAnnotation(Log.class) != null) {
            logger.debug("Название метода: {}, аргументы метода: {}", method.getName(), gson.toJson(args));
            logger.debug("Результат выполнения метода {} - {}", method.getName(), gson.toJson(invoke));
        }
        return invoke;
    }
}
