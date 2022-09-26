package ru.clevertec.check.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.check.dto.CardDto;
import ru.clevertec.check.entity.Card;
import ru.clevertec.check.service.ICardService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cards")
@RequiredArgsConstructor
public class CardController {

    private final ICardService cardService;

    @GetMapping
    public ResponseEntity<List<CardDto>> findAll(Pageable pageable) {
        return ResponseEntity.ok(cardService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardDto> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(cardService.findById(id));
    }

    @GetMapping("/number/{number}")
    public ResponseEntity<CardDto> findByNumber(@PathVariable("number") Integer number) {
        return ResponseEntity.ok(cardService.findByNumber(number));
    }

    @PostMapping
    public ResponseEntity<CardDto> save(@RequestBody Card card) {
        return ResponseEntity.ok(cardService.save(card));
    }

    @PutMapping
    public ResponseEntity<CardDto> update(@RequestBody Card card) {
        return ResponseEntity.ok(cardService.update(card, card.getId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cardService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> findById() {
        return ResponseEntity.ok(cardService.countAllEntities());
    }
}
