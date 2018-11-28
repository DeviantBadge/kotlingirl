package ru.atom.game.http.registry;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.atom.game.databases.player.PlayerData;
import ru.atom.game.http.message.Credentials;
import ru.atom.game.http.util.ResponseFactory;

import java.util.regex.Pattern;

class CredentialsChecker {

    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9-_\\.]{2,20}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9-_\\.]{6,16}$");

    // *********************************************
    // Credentials
    // *********************************************

    ResponseEntity<String> checkCredentialsRegisterIn(Credentials credentials) {
        if(credentials.getName() == null
                || credentials.getPassword() == null
                || credentials.getPasswordCopy() == null) {
            return new ResponseEntity<>("Request without needed parameter", HttpStatus.BAD_REQUEST);
        }
        if (!matchesWithPattern(NAME_PATTERN, credentials.getName())) {
            return ResponseFactory.generateErrorResponse(
                    "Name does not matches with pattern.",
                    "Name must have 2-20 length and contains only latin letters or numbers",
                    HttpStatus.BAD_REQUEST
            );
        }
        if (!matchesWithPattern(PASSWORD_PATTERN, credentials.getPassword())) {
            return ResponseFactory.generateErrorResponse(
                    "Password does not matches with pattern.",
                    "Password must have 6-16 length and contains only latin letters or numbers",
                    HttpStatus.BAD_REQUEST
            );
        }
        if(!credentials.getPassword().equals(credentials.getPasswordCopy())) {
            return ResponseFactory.generateErrorResponse(
                    "Passwords does not equal.",
                    "Passwords must be equal",
                    HttpStatus.BAD_REQUEST
            );
        }

        return null;
    }

    ResponseEntity<String> checkCredentialsSignIn(Credentials credentials) {
        if(credentials.getName() == null
                || credentials.getPassword() == null) {
            return new ResponseEntity<>("Request without needed parameter", HttpStatus.BAD_REQUEST);
        }
        if (!matchesWithPattern(NAME_PATTERN, credentials.getName())) {
            return ResponseFactory.generateErrorResponse(
                    "Name does not matches with pattern.",
                    "Name must have 2-20 length and contains only latin letters or numbers",
                    HttpStatus.BAD_REQUEST
            );
        }
        if (!matchesWithPattern(PASSWORD_PATTERN, credentials.getPassword())) {
            return ResponseFactory.generateErrorResponse(
                    "Password does not matches with pattern.",
                    "Password must have 6-16 length and contains only latin letters or numbers",
                    HttpStatus.BAD_REQUEST
            );
        }
        return null;
    }

    private boolean matchesWithPattern(Pattern pattern, String value) {
        return pattern.matcher(value).matches();
    }

    // *********************************************
    // Player Data
    // *********************************************

    ResponseEntity<String> checkRegisterPayerData(Credentials credentials, PlayerData playerData) {
        if (playerData != null)
            return ResponseFactory.generateErrorResponse(
                    "Player with such name already exists.",
                    "Create another name.",
                    HttpStatus.BAD_REQUEST
            );

        return null;
    }

    ResponseEntity<String> checkSignInPayerData(Credentials credentials, PlayerData playerData) {
        if (playerData == null)
            return ResponseFactory.generateErrorResponse(
                    "We dont have any player with such name.",
                    "Create player with this name.",
                    HttpStatus.BAD_REQUEST
            );
        if (!credentials.getPassword().equals(playerData.getPassword()))
            return ResponseFactory.generateErrorResponse(
                    "Wrong password.",
                    "Remember your password and try again.",
                    HttpStatus.BAD_REQUEST
            );
        if (playerData.isLogged())
            return ResponseFactory.generateErrorResponse(
                    "Player with such name is already logged in.",
                    "Try again later.",
                    HttpStatus.BAD_REQUEST
            );

        return null;
    }
}
