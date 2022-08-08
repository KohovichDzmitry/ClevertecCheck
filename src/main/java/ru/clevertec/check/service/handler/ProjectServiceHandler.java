package ru.clevertec.check.service.handler;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.clevertec.check.annotation.Log;
import ru.clevertec.check.api.service.IProjectService;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ProjectServiceHandler implements InvocationHandler {

    private final IProjectService iProjectService;

    public ProjectServiceHandler(IProjectService iProjectService) {
        this.iProjectService = iProjectService;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Logger logger = LogManager.getLogger(method.getDeclaringClass().getName());
        Gson gson = new Gson();
        Object invoke = method.invoke(iProjectService, args);
        if (method.getAnnotation(Log.class) != null ||
                iProjectService.getClass().getMethod(method.getName(), method.getParameterTypes())
                        .getAnnotation(Log.class) != null) {
            logger.debug("Название метода: {}, аргументы метода: {}", method.getName(), gson.toJson(args));
            logger.debug("Результат выполнения метода {} - {}", method.getName(), gson.toJson(invoke));
        }
        return invoke;
    }
}
