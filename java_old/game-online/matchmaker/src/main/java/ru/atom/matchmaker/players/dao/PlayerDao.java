package ru.atom.matchmaker.players.dao;

import ru.atom.matchmaker.players.model.Player;
import java.util.List;

public interface PlayerDao {

    void save (Player player);

    void update(Player player);

    void delete(Player player);

    Player getById(long id);

    Player getByLogin(String login);

    List<Player> getPlayerList();
}
