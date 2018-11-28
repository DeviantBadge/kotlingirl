package ru.atom.game.http.matchmaker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.atom.game.gamesession.properties.GameSessionProperties;
import ru.atom.game.gamesession.session.GameSession;
import ru.atom.game.http.matchmaker.mmunit.RatingSessionWrapper;
import ru.atom.game.repos.GameSessionRepo;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class MatchMakerSessionRepo {
    private final ConcurrentLinkedQueue<GameSession> commonReadyToPlaySessions;
    private final ArrayList<RatingSessionWrapper> ratingSessions;

    @Autowired
    private GameSessionRepo gameSessionRepo;

    protected MatchMakerSessionRepo() {
        commonReadyToPlaySessions = new ConcurrentLinkedQueue<>();
        ratingSessions = new ArrayList<>();
    }


    public GameSession pollOrCreateCommonSession(GameSessionProperties properties) {
        GameSession commonSession = commonReadyToPlaySessions.poll();
        if(commonSession==null) {
            return gameSessionRepo.createSession(properties);
        }
        return commonSession;
    }

    public void putCommonSessionBack(GameSession session) {
        if (session.isFull()) {
            /* smth */
        } else {
            commonReadyToPlaySessions.add(session);
        }
    }

    public ArrayList<RatingSessionWrapper> getRatingSessions() {
        return ratingSessions;
    }

    public RatingSessionWrapper createRatingSession(/*Player */ GameSessionProperties properties) {
        GameSession session = gameSessionRepo.createSession(properties);
        return new RatingSessionWrapper(session);
    }
}
