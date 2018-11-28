package ru.atom.game.objects.base.interfaces;

public interface Ticking {
    void start();

    void stop(); // equals to finish - we end his ticking life --hard

    void tick(long ms);

    boolean isReady();
}
