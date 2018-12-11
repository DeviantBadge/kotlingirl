package com.kotlingirl.registry.model

import javax.persistence.*


@Entity
@Table(name = "sessiondata", schema = "game")
data class SessionData(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = -1,

        @Column(name = "games_played")
        var game_type: GameType = GameType.CASUAL
){
}