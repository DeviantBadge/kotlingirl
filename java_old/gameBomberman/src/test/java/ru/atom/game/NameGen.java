package ru.atom.game;

public class NameGen {
    private static int testNumber = 0;

    public int getGenId() {
        return testNumber++;
    }

    public String generateName() {
        return "test" + (testNumber++);
    }
}
