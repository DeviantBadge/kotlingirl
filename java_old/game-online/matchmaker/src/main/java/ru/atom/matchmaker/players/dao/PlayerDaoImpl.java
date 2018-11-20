package ru.atom.matchmaker.players.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import ru.atom.matchmaker.controller.MatchmakerController;
import ru.atom.matchmaker.players.model.Player;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Repository
@Transactional
public class PlayerDaoImpl implements PlayerDao{

    private static final Logger logger = LoggerFactory.getLogger(MatchmakerController.class);

    private final SessionFactory sessionFactory;


    @Autowired
    public PlayerDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void save(Player player) {
        getSession().save(player);
        return;
    }

    public void delete(Player player) {
        getSession().delete(player);
        return;
    }

    @Override
    public void update(Player player) {
        getSession().update(player);
        return;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Player> getPlayerList() {
        return getSession().createQuery("from Player").list();
    }

    @Override
    public Player getByLogin(String login) {
        return (Player) getSession().load(Player.class, login);
    }


    @Override
    public Player getById(long id) {
        return (Player) getSession().load(Player.class, id);
    }
}
