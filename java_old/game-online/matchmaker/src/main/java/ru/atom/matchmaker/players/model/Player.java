package ru.atom.matchmaker.players.model;

import javax.persistence.*;
import java.io.Serializable;
//import java.util.List;

@Entity
@Table(name = "players", schema = "public")
public class Player implements Serializable {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column(name = "login", unique = true, nullable = false, length = 40)
    private String login;

    @Column(name = "password", nullable = false, length = 40)
    private String password;

    @Column(name = "rating", nullable = false)
    private int rating;

    @Column(name = "isonline")
    private boolean isOnline;

    @Column(name = "insearch")
    private boolean inSearch;

    @Column(name = "idsession")
    private long idSession;

    public long getId() {

        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public boolean isInSearch() {
        return inSearch;
    }

    public void setInSearch(boolean inSearch) {
        this.inSearch = inSearch;
    }

    public long getIdSession() {
        return idSession;
    }

    public void setIdSession(long idSession) {
        this.idSession = idSession;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Player() {
        rating = 1500;
    }

    public Player(String login, String password) {
        this.login = login;
        this.password = password;
        this.rating = 1500;
    }

    @Override
    public String toString() {
        return "Player(" + "id =" + id + ", name ='" + login
                + '\'' + ", rating = "
                + rating + ')';
    }
}