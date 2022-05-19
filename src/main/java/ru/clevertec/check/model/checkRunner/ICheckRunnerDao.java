package ru.clevertec.check.model.checkRunner;

import ru.clevertec.check.util.CustomList;

public interface ICheckRunnerDao {

    void buy(int id, int quantity);

    CustomList<CheckRunner> getAll();
}
