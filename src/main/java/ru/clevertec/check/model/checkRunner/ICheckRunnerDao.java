package ru.clevertec.check.model.checkRunner;

import java.util.List;

public interface ICheckRunnerDao {

    void buy(Integer id, Integer quantity);

    List<CheckRunner> getAll();
}
