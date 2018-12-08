package com.kotlingirl.matchmaker.model

import javax.persistence.*


@Entity
@Table(name = "sessiondata", schema = "game")
data class sessiondata(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = -1,

        @Column(name = "games_played")
        var game_type: GameType = GameType.CASUAL
){
}