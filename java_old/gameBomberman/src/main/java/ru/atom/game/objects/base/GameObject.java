package ru.atom.game.objects.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.atom.game.objects.base.interfaces.Destroyable;
import ru.atom.game.objects.base.interfaces.Replicable;
import ru.atom.game.objects.base.util.ColliderFrame;
import ru.atom.game.objects.base.util.Point;
import ru.atom.game.enums.ObjectType;

import javax.swing.text.Position;

public abstract class GameObject extends ColliderFrame implements Destroyable, Replicable {
    protected final static Logger log = LoggerFactory.getLogger(GameObject.class);
    private final Integer id;
    private final ObjectType type;
    private final boolean blocking;
    private boolean destroyed = false;
    private boolean deleted = false;


    public GameObject(Integer id, ObjectType type, Point absolutePosition,
                      Point colliderShift, Double colliderSizeX, Double colliderSizeY, boolean blocking) {
        super(absolutePosition, colliderSizeX, colliderSizeY, colliderShift);
        this.id = id;
        this.type = type;
        this.blocking = blocking;
    }

    private void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public Integer getId() {
        return id;
    }

    public ObjectType getType() {
        return type;
    }

    @JsonIgnore
    protected String getEntrails() {
        return "id:" + id + ",type:" + type + ",frame:" + super.toString();
    }

    @JsonIgnore
    public boolean isBlocking() {
        return blocking;
    }

    // destroyed by bomb
    // deleted by game session - if deleted we remove it from the game (that means, that onDestroy handled)
    @JsonIgnore
    public boolean isDeleted() {
        return deleted;
    }

    @Override
    @JsonIgnore
    public boolean isDestroyable() {
        return false;
    }

    @JsonIgnore
    @Override
    public boolean isDestroyed() {
        return destroyed;
    }

    // todo mb implement it
    @JsonIgnore
    @Override
    public boolean isRestorable() {
        return true;
    }

    public void delete() {
        if(!isDestroyed())
            log.warn("Deleting object what was not destroyed.");
        deleted = true;
    }

    @Override
    public boolean destroy() {
        if (isDestroyable())
            setDestroyed(true);
        return isDestroyed();
    }

    @Override
    public boolean restore() {
        if (isRestorable())
            setDestroyed(false);
        return !isDestroyed();
    }

    @Override
    public String toString() {
        return "{" + getEntrails() + "}";
    }

    @Override
    public boolean equals(Object anObject) {
        if (anObject == null) {
            return false;
        }
        if (this == anObject) {
            return true;
        }
        if (anObject instanceof GameObject) {
            return id.equals(((GameObject) anObject).id);
        }
        return false;
    }
}
