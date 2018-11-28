package ru.atom.game.repos;

import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Repository;
import ru.atom.game.gamesession.session.GameSession;
import ru.atom.game.gamesession.properties.GameSessionProperties;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Repository
public class GameSessionRepo {
    private final ConcurrentHashMap<String, GameSession> allSessions;


    private SimpleAsyncTaskExecutor executor;

    private BeanFactory beans;

    @Autowired
    private void setExecutor(SimpleAsyncTaskExecutor executor) {
        this.executor = executor;
    }

    @Autowired
    private void setBeans(BeanFactory beans) {
        this.beans = beans;
    }

    protected GameSessionRepo() {
        allSessions = new ConcurrentHashMap<>();
    }

    public GameSession getSession(String id) {
        return allSessions.get(id);
    }

    public GameSession createSession(GameSessionProperties properties) {
        GameSession session = beans.getBean(GameSession.class, properties);

        allSessions.put(session.getId().toString(), session);
        executor.execute(session);
        return session;
    }

    @Override
    public String toString() {
        return allSessions.size() + "";
    }

    public void onSessionEnd(GameSession session) {
        allSessions.remove(session.getId().toString());
    }
}
