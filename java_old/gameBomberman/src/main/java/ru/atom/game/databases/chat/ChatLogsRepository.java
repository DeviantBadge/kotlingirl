package ru.atom.game.databases.chat;

import org.springframework.data.repository.CrudRepository;
import ru.atom.game.databases.player.PlayerData;

public interface ChatLogsRepository extends CrudRepository<ChatLogs, Long> {
    
}
