package ru.atom.game.databases.player;

import javax.persistence.*;

@Entity
@Table(name = "player", schema = "game")
public class PlayerData {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "logged")
    private Boolean logged;

    protected PlayerData() {}

    public PlayerData(String name, String password) {
        this.name = name;
        this.password = password;
        this.rating = 1000;
        this.logged = true;
    }

    @Override
    public String toString() {
        return String.format(
                "Player[id=%d, name='%s', rating='%s', logged='%s']",
                id, name, rating.toString(), logged.toString());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public Integer getRating() {
        return rating;
    }

    public Integer addToRating(int ratingPoints) {
        return rating += ratingPoints;
    }

    public boolean isLogged() {
        return logged;
    }

    public void setLogged(Boolean logged) {
        this.logged = logged;
    }
}
