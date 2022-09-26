package ru.clevertec.check.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.check.format.Format;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/check")
@RequiredArgsConstructor
public class OrderController {

    @Qualifier("formatPDF")
    private final Format format;

    @GetMapping
    public ResponseEntity<byte[]> printCheck(HttpServletRequest req) {
        return format.setFormat(req.getParameterMap());
    }
}
