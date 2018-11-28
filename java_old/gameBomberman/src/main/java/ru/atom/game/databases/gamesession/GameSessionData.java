package ru.atom.game.databases.gamesession;

import ru.atom.game.databases.player.PlayerData;
import ru.atom.game.gamesession.properties.GameSessionProperties;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "sessionData", schema = "game")
public class GameSessionData {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    /*
    @Column(name = "players")
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<PlayerData> players = new HashSet<>();
    */

    GameSessionData() {}

    GameSessionData(GameSessionProperties properties) {

    }
}
