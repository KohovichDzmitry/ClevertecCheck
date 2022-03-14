package ru.clevertec.check.model.checkRunner;

import java.util.List;

public interface ICheckRunnerDao {

    void buy(int id, int quantity);

    List<CheckRunner> getAll();
}
