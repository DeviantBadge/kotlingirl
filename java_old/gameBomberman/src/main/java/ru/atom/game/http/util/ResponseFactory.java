package ru.atom.game.http.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.atom.game.http.message.ResponseError;
import ru.atom.game.http.message.ResponseOk;
import ru.atom.game.socket.util.JsonHelper;

public class ResponseFactory {

    public static ResponseEntity<String> generateErrorResponse(String errorMessage, String solution, HttpStatus status) {
        ResponseError error = new ResponseError(errorMessage, solution);
        return new ResponseEntity<>(JsonHelper.toJson(error), status);
    }

    public static ResponseEntity<String> generateErrorResponse(String errorMessage, HttpStatus status) {
        ResponseError error = new ResponseError(errorMessage);
        return new ResponseEntity<>(JsonHelper.toJson(error), status);
    }

    public static ResponseEntity<String> generateOkResponse(String successMessage, String advise, HttpStatus status) {
        ResponseOk ok = new ResponseOk(successMessage, advise);
        return new ResponseEntity<>(JsonHelper.toJson(ok), status);
    }

    public static ResponseEntity<String> generateOkResponse(String successMessage, HttpStatus status) {
        ResponseOk ok = new ResponseOk(successMessage);
        return new ResponseEntity<>(JsonHelper.toJson(ok), status);
    }
}
