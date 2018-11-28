package ru.atom.game.http.matchmaker.mmunit;

import ru.atom.game.gamesession.session.GameSession;

public class RatingSessionWrapper {
    private static final double RADIUS_PER_MINUTE = 200;
    private double sessionOwnerRating = 1000;
    private double ratingRadius = 100;

    private final GameSession session;

    public RatingSessionWrapper(GameSession session /*, Player owner */) {
        this.session = session;
    }

    public double getSessionOwnerRating() {
        return sessionOwnerRating;
    }

    public double getRatingRadius() {
        return ratingRadius;
    }

    public GameSession getSession() {
        return session;
    }

    public void incRadius(long ms) {
        ratingRadius += RADIUS_PER_MINUTE * ms / 60_000;
    }

    public boolean inRadius(double rating) {
        return (rating <= (sessionOwnerRating + ratingRadius))
                && (rating >= (sessionOwnerRating - ratingRadius));
    }
}
