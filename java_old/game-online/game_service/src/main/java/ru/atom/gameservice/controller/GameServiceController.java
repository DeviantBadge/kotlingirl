package ru.atom.gameservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.atom.gameservice.GameServer;
import ru.atom.gameservice.GameSession;

@Controller
@RequestMapping("game")
public class GameServiceController {

    private static final Logger logger = LoggerFactory.getLogger(GameServiceController.class);
    private GameServer gameServer;

    @Autowired
    public GameServiceController(GameServer gameServer) {
        this.gameServer = gameServer;
    }

    @RequestMapping(path = "create",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Long> create(@RequestParam("count") int count) {
        long id = gameServer.getNextId();
        gameServer.addGameSession(String.valueOf(id), new GameSession(count));
        logger.info("Game {} created", id);
        return ResponseEntity.ok(id);
    }

    @RequestMapping(path = "start",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity start(@RequestParam("gameId") long gameId) {
        logger.info("Game {} started", gameId);
        new Thread(gameServer.getGameSessionByName(String.valueOf(gameId))).run();
        return ResponseEntity.ok().build();
    }
}
