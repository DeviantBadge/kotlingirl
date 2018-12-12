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
        var password: CharArray,

        @Column(name = "rating")
        var rating: Int = 1000,

        @Column(name = "modified_at")
        var modifiedAt: Date = Date(),

        @Column(name = "registration_date")
        var registrationDate: Date? = Date(),

        @Column(name = "logged")
        var logged: Boolean = false,

/*        @ManyToOne
        @JoinColumn(name = "sessiondata_id", insertable = false, updatable = false)*/
        @Column(name = "current_game")
        var currentGame: Long? = null
)
{
    constructor(login: String, password: CharArray) : this(0,0,0,login,password,1000, Date(), Date(), false, null)

        override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as Player

                if (id != other.id) return false
                if (gamesPlayed != other.gamesPlayed) return false
                if (gamesWon != other.gamesWon) return false
                if (login != other.login) return false
                if (!password.contentEquals(other.password)) return false
                if (rating != other.rating) return false
                if (modifiedAt != other.modifiedAt) return false
                if (registrationDate != other.registrationDate) return false
                if (logged != other.logged) return false
                if (currentGame != other.currentGame) return false

                return true
        }

        override fun hashCode(): Int {
                var result = id.hashCode()
                result = 31 * result + gamesPlayed
                result = 31 * result + gamesWon
                result = 31 * result + login.hashCode()
                result = 31 * result + password.contentHashCode()
                result = 31 * result + rating
                result = 31 * result + modifiedAt.hashCode()
                result = 31 * result + (registrationDate?.hashCode() ?: 0)
                result = 31 * result + logged.hashCode()
                result = 31 * result + (currentGame?.hashCode() ?: 0)
                return result
        }
}