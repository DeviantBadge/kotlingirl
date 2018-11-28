package ru.atom.game.objects.base.interfaces;

public interface Destroyable {
    boolean isDestroyable();

    boolean isRestorable();

    boolean destroy();

    boolean restore();

    boolean isDestroyed();
}
