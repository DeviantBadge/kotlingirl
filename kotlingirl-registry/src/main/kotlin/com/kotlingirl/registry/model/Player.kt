package com.kotlingirl.registry.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "player", schema = "game")
@JsonIgnoreProperties("password")
data class Player(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long,

        @Column(name = "games_played")
        var gamesPlayed: Int = 0,

        @Column(name = "games_won")
        var gamesWon: Int = 0,

        @Column(name = "login")
        var login: String,

        @Column(name = "password")
        var password: String,

        @Column(name = "rating")
        var rating: Int = 1000,

        @Column(name = "modified_at")
        var modifiedAt: Date = Date(),

        @Column(name = "registration_date")
        var registrationDate: Date? = Date(),

        @Column(name = "logged")
        var logged: Boolean = false,

        //@OneToMany()
        @Column(name = "current_game")
        var currentGame: Long? = null
)
{
    constructor(login: String, password: String) : this(0,0,0,login,password,1000, Date(), Date(), false, null)
}