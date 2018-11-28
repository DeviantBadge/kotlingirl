package ru.atom.game.http.message;

public class ResponseError {
    private final String errorMessage;
    private final String solution;

    public ResponseError(String errorMessage, String solution) {
        this.errorMessage = errorMessage;
        this.solution = solution;
    }

    public ResponseError(String errorMessage) {
        this.errorMessage = errorMessage;
        this.solution = "This problem cant be solved, try to reload page or contact the developer.";
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getSolution() {
        return solution;
    }
}
