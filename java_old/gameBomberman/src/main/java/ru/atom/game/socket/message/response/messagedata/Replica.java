package ru.atom.game.socket.message.response.messagedata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.atom.game.objects.base.interfaces.Replicable;
import ru.atom.game.socket.util.JsonHelper;

import java.util.ArrayList;
import java.util.List;

public class Replica {
    private static Logger log = LoggerFactory.getLogger(Replica.class);
    private ArrayList<Replicable> data;
    public Replica() {
        data = new ArrayList<>();
    }

    public void addToReplica(Replicable replicable) {
        data.add(replicable);
    }

    public void addAllToReplica(List<? extends Replicable> replicable) {
        data.addAll(replicable);
    }

    public ArrayList<Replicable> getDataAndClear() {
        ArrayList<Replicable> oldReplica = data;
        data = new ArrayList<>();
        return oldReplica;
    }

    public ArrayList<Replicable> getData() {
        return data;
    }

    public String toStringAndClear() {
        return JsonHelper.toJson(getDataAndClear());
    }

    @Override
    public String toString() {
        return JsonHelper.toJson(getData());
    }

    public void clear() {
        data.clear();
    }
}
