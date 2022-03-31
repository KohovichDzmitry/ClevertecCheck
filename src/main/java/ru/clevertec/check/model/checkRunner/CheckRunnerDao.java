package ru.clevertec.check.model.checkRunner;

import ru.clevertec.check.util.CustomArrayList;
import ru.clevertec.check.util.CustomList;

public class CheckRunnerDao implements ICheckRunnerDao {

    private final CustomList<CheckRunner> listOfCheckRunner = new CustomArrayList<>();

    @Override
    public void buy(int id, int quantity) {
        CheckRunner checkRunner = new CheckRunner(id, quantity);
        listOfCheckRunner.add(checkRunner);
    }

    @Override
    public CustomList<CheckRunner> getAll() {
        return new CustomArrayList<>(listOfCheckRunner);
    }
}
