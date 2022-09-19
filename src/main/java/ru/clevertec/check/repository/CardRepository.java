package ru.clevertec.check.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.check.model.Card;

import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {

    Optional<Card> findCardByNumber(Integer number);

    Integer countAllBy();
}
