package ru.atom.game.socket.util.jsoncheck;

import ru.atom.game.http.message.ResponseOk;
import ru.atom.game.socket.util.JsonHelper;

import java.util.regex.Pattern;

public class AnotherCheck {

    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9-_\\.]{2,20}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9-_\\.]{6,16}$");
    public static void main(String[] args) {
        System.out.println(JsonHelper.toJson(new ResponseOk("123")));

        System.out.println(NAME_PATTERN.matcher("r123r").matches());
    }
}
