package ru.atom.game.http.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.atom.game.databases.player.PlayerData;
import ru.atom.game.databases.player.PlayerDataRepository;
import ru.atom.game.http.matchmaker.MatchMaker;
import ru.atom.game.http.message.Credentials;
import ru.atom.game.http.message.ResponseError;
import ru.atom.game.http.util.ResponseFactory;
import ru.atom.game.socket.util.JsonHelper;
import sun.misc.Regexp;

import java.util.Map;
import java.util.regex.Pattern;

@Controller
@RequestMapping("registry")
public class Register {
    private static final Logger log = LoggerFactory.getLogger(Register.class);

    @Autowired
    private MatchMaker matchMaker;

    @Autowired
    private PlayerDataRepository playerRepo;
    private CredentialsChecker checker = new CredentialsChecker();

    @RequestMapping(
            path = "signIn",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> signIn(@RequestBody String userData) {
        Credentials credentials = JsonHelper.fromJson(userData, Credentials.class);
        ResponseEntity<String> response;
        PlayerData playerData;

        response = checker.checkCredentialsSignIn(credentials);
        if (response != null)
            return response;

        playerData = playerRepo.findByName(credentials.getName());
        response = checker.checkSignInPayerData(credentials, playerData);
        if (response != null)
            return response;

        return new ResponseEntity<>("0", HttpStatus.OK);
    }

    @RequestMapping(
            path = "register",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> register(@RequestBody String userData) {
        Credentials credentials = JsonHelper.fromJson(userData, Credentials.class);
        ResponseEntity<String> response;
        PlayerData playerData;

        response = checker.checkCredentialsRegisterIn(credentials);
        if (response != null)
            return response;

        playerData = playerRepo.findByName(credentials.getName());
        response = checker.checkRegisterPayerData(credentials, playerData);
        if (response != null)
            return response;

        playerData = new PlayerData(credentials.getName(), credentials.getPassword());
        response = addNewPlayerToDB(playerData);
        if (response != null)
            return response;
        return ResponseFactory.generateOkResponse("You are registred", HttpStatus.OK);
        //return new ResponseEntity<>(playerData.toString(), HttpStatus.OK);
    }

    private ResponseEntity<String> addNewPlayerToDB(PlayerData playerData) {
        try {
            playerRepo.save(playerData);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFactory.generateErrorResponse(
                    "Failed to create new player",
                    "Try again later.",
                    HttpStatus.CONFLICT
            );
        }
        return null;
    }
}
