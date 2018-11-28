package ru.atom.game.http.matchmaker;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.atom.game.gamesession.lists.OnlinePlayer;
import ru.atom.game.gamesession.properties.GameSessionPropertiesCreator;
import ru.atom.game.gamesession.session.GameSession;
import ru.atom.game.http.matchmaker.mmunit.RatingSessionWrapper;
import ru.atom.game.repos.ConnectionPool;

import java.util.ArrayList;


// TODO we can make it ticker, so he will check every loop each session, how long is player waiting, if very long, we will try to put him (or them) to another sessions
// TODO move it to another project, rebase repo, all projects easier to hold in one dir. (google how to do it)
// NOT this class will be ticker, but another one, that will be daemon
@Service
public class MatchMaker {
    // TODO make matchMaker daemon, so he will be processing every second like session (its better to create daemon, that will handle all tasks once per second)

    @Autowired
    private BeanFactory beans;

    @Autowired
    private MatchMakerSessionRepo sessionRepo;

    @Autowired
    private ConnectionPool pool;


    // TODO рейтинг будет переделан в соответствии с моделью - много серверов держащих бомберман, мм является что то вроде распределителя нагрузки
    // TODO сделать рейтинг - принцип, один большой массив / очередь, хранит все сессии которые она обрабатывает, когда проходим еще раз, мы смотрим,есть ли доступная игра, если нет
    // TODO то создаем новую, а у пройденных расширяем ненамного область видимости
    // todo перенести mm в другой проект, остальные отправляют mm запросы после создания и принимают запросы на игры

    Integer getCommonSessionID(String name) {
        GameSessionPropertiesCreator creator = beans.getBean(GameSessionPropertiesCreator.class)
                .setMaxPlayerAmount(3)
                // .setBlowStopsOnWall(false)
                .setSpeedOnStart(3)
                .setBonusProbability(1)
                .setProbabilities(0.4,1,1)
                .setPlayerStopsPlayer(true)
                .setBombBlowAsOne(false);
        GameSession session = sessionRepo.pollOrCreateCommonSession(creator.createProperties());
        session.addPlayer(createPlayer(name));
        sessionRepo.putCommonSessionBack(session);

        return session.getId();
    }

    Integer getRatingSessionID(String name) {
        boolean found = false;
        RatingSessionWrapper ratingSession;
        final ArrayList<RatingSessionWrapper> ratingSessions = sessionRepo.getRatingSessions();
        GameSessionPropertiesCreator creator = beans.getBean(GameSessionPropertiesCreator.class)
                .setRanked(true);

        synchronized (sessionRepo.getRatingSessions()) {
            for(int i = 0; i < ratingSessions.size(); i ++) {
                ratingSession = ratingSessions.get(i);
                if(ratingSession.inRadius(1000)) {
                    ratingSession.getSession().addPlayer(createPlayer(name));
                    if(ratingSession.getSession().isFull())
                        ratingSessions.remove(i);
                    found = true;
                    break;
                }
            }
            if(!found) {
                ratingSession = sessionRepo.createRatingSession(creator.createProperties());
                ratingSessions.add(ratingSession);
            } else {
                // impossible
                return -1;
            }
            return ratingSession.getSession().getId();
        }
    }

    private OnlinePlayer createPlayer(String name) {
        //todo - move it to another mb func?
        OnlinePlayer player = new OnlinePlayer(name);
        pool.addNewPlayer(player);
        return player;
    }
}
