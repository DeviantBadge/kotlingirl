package ru.atom.game.databases.player;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PlayerDataRepository extends CrudRepository<PlayerData, Long> {
    PlayerData findByName(String name);

    default boolean saveNewPlayer(PlayerData player) {
        try {
            save(player);
        } catch (Exception e){
            return false;
        }
        return true;
    }
}
