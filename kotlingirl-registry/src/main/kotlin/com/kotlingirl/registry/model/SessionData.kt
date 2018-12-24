package com.kotlingirl.registry.model

import java.util.ArrayList
import javax.persistence.*


@Entity
@Table(name = "sessiondata", schema = "game")
data class SessionData(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = -1,

        @Column(name = "games_played")
        var game_type: GameType = GameType.CASUAL

/*        @OneToMany(mappedBy = "player")
        var players : List<Player>*/
){
}