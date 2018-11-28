package ru.atom.game.databases.gamesession;

import org.springframework.data.repository.CrudRepository;
import ru.atom.game.databases.player.PlayerData;

import java.util.List;

public interface GameSessionDataRepository extends CrudRepository<GameSessionData, Long> {
}
