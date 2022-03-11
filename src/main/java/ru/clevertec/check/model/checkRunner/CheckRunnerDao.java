package ru.clevertec.check.model.checkRunner;

import java.util.ArrayList;
import java.util.List;

public class CheckRunnerDao implements ICheckRunnerDao{

    private final List<CheckRunner> listOfCheckRunner = new ArrayList<>();

    @Override
    public void buy(Integer id, Integer quantity) {
        CheckRunner checkRunner = new CheckRunner(id, quantity);
        listOfCheckRunner.add(checkRunner);
    }

    @Override
    public List<CheckRunner> getAll() {
        return new ArrayList<>(listOfCheckRunner);
    }
}
