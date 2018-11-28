package ru.atom.game.databases.chat;

import javax.persistence.*;

@Entity
@Table(name = "chatLogs", schema = "game")
public class ChatLogs {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
}
