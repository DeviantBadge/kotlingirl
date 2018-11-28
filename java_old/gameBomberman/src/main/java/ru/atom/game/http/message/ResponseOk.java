package ru.atom.game.http.message;

public class ResponseOk {
    private final String successMessage;
    private final String advise;

    public ResponseOk(String successMessage, String advise) {
        this.successMessage = successMessage;
        this.advise = advise;
    }

    public ResponseOk(String successMessage) {
        this.successMessage = successMessage;
        this.advise = null;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public String getAdvise() {
        return advise;
    }
}
