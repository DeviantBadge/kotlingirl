package ru.atom.game.http.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Credentials {
    private final String name;
    private final String password;
    private final String passwordCopy;

    @JsonCreator
    public Credentials (@JsonProperty("name") String name,
                           @JsonProperty("password") String password,
                           @JsonProperty("passwordCopy") String passwordCopy) {
        this.name = name;
        this.password = password;
        this.passwordCopy = passwordCopy;
    }

    public String getPasswordCopy() {
        return passwordCopy;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "{name:" + name + ", password:" + password + ", passwordCopy:" + passwordCopy + "}";
    }
}
