package ru.atom.matchmaker.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.atom.matchmaker.Connection;
import ru.atom.matchmaker.Matchmaker;

@Controller
@RequestMapping("matchmaker")
public class MatchmakerController {

    private static final Logger logger = LoggerFactory.getLogger(MatchmakerController.class);

    private Matchmaker matchmaker;

    @Autowired
    public MatchmakerController(Matchmaker matchmaker) {
        this.matchmaker = matchmaker;
    }

    @CrossOrigin("*")
    @RequestMapping(path = "join",
            method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Long> join(@RequestParam("name") String name) {
        logger.info("New connection: name = {}", name);
        Connection playerConnection = new Connection(name);
        matchmaker.getQueue().offer(playerConnection);
        synchronized (playerConnection) {
            try {
                playerConnection.wait(10_000);
            } catch (InterruptedException e) {
                logger.error(e.getLocalizedMessage());
                return ResponseEntity.badRequest().body((long) 0);
            }
        }
        if ( playerConnection.isAvailable()){
            return ResponseEntity.ok(playerConnection.getGameId());
        }

        return ResponseEntity.badRequest().body((long) 0);
    }

/*
    @CrossOrigin(origins = "*")
    @RequestMapping(path = "login",
            method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> login(@RequestParam("Login") String login, @RequestParam("password") String password) {
        logger.info("New connection: Login = {}", login);

        Player player = playerDao.getByLogin(login);
        if (player == null){
            return ResponseEntity.badRequest().body("Cant find this name");
        }

        if (!player.getPassword().equals(password)) {
            return ResponseEntity.badRequest().body("Wrong password");
        }

        Connection playerConnection = new Connection(login);
        matchmaker.getQueue().offer(playerConnection);
        synchronized (playerConnection) {
            try {
                playerConnection.wait(10_000);
            } catch (InterruptedException e) {
                logger.error(e.getLocalizedMessage());
                return ResponseEntity.badRequest().body("0");
            }
        }
        if ( playerConnection.isAvailable()){
            return ResponseEntity.ok(String.valueOf(playerConnection.getGameId()));
        }

        return ResponseEntity.badRequest().body("0");
    }

    @RequestMapping(path = "registration",
            method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> registration(@RequestParam("Login") String login, @RequestParam("password") String password) {
        logger.info("New player: Login = {}", login);

        if (login.length() < 4) {
            return ResponseEntity.badRequest().body("Too short name, sorry :(");
        }

        if (login.length() > 20) {
            return ResponseEntity.badRequest().body("Too long name, sorry :(");
        }

        Player player = playerDao.getByLogin(login);
        if (player != null){
            return ResponseEntity.badRequest().body("Login is created before");
        }

        try {
            logger.info("create player = {}", login);
            Player newPlayer = new Player(login, password);
            playerDao.save(newPlayer);
        } catch (Exception ex) {
            logger.error(ex.getLocalizedMessage());
            return ResponseEntity.badRequest().body("smt is wrong");
        }
        return ResponseEntity.ok("Create new player");
    }
*/
}