package ru.atom.game.http.matchmaker;

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
import ru.atom.game.databases.player.PlayerDataRepository;
import ru.atom.game.socket.util.JsonHelper;

import java.util.Map;

@Controller
@RequestMapping("matchmaker")
public class MatchMakerController {
    private static final Logger log = LoggerFactory.getLogger(MatchMakerController.class);

    @Autowired
    private MatchMaker matchMaker;

    @Autowired
    private PlayerDataRepository playerDataRepository;

    @RequestMapping(
            path = "casual",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> join(@RequestBody String userData) {
        ResponseEntity<String> response;
        Map<String, Object> data = JsonHelper.fromJson(userData, Map.class);
        String name = ((Map) data.get("credentials")).get("name").toString();

        if((response = auth(name)) != null) {
            return response;
        }
        Integer id = matchMaker.getCommonSessionID(name);
        return new ResponseEntity<>(id.toString(), HttpStatus.OK);
    }

    @RequestMapping(
            path = "rating",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> rating(@RequestBody String userData) {
        ResponseEntity<String> response;
        Map<String, Object> data = JsonHelper.fromJson(userData, Map.class);
        String name = ((Map) data.get("credentials")).get("name").toString();

        if((response = auth(name)) != null) {
            return response;
        }

        Integer id = matchMaker.getRatingSessionID(name);
        return new ResponseEntity<>(id.toString(), HttpStatus.OK);
    }

    private ResponseEntity<String> auth(String name) {
        /*
        List<PlayerData> player = playerDataRepository.findByName(name);
        if(player.size() == 0) {
            return new ResponseEntity<>("Cant find this user", HttpStatus.NOT_FOUND);
        }
        */
        return null;
    }
}
