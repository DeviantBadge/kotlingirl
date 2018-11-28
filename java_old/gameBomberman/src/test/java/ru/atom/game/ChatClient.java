package ru.atom.game;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


import java.io.IOException;


public class ChatClient {
    private static final OkHttpClient client = new OkHttpClient();
    private static final String PROTOCOL = "http://";
    private static final String HOST = "localhost";
    private static final String PORT = ":8080";

    //POST host:port/game/login?name=my_name&password=pass
    public static Response login(String name, String pass) throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        Request request = new Request.Builder()
                .post(RequestBody.create(mediaType, ""))
                .url(PROTOCOL + HOST + PORT + "/game/login?name=" + name + "&password=" + pass)
                .build();

        return client.newCall(request).execute();
    }

    //POST host:port/game/login?name=my_name&password=pass
    public static Response register(String name, String pass, String passCopy) throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        Request request = new Request.Builder()
                .post(RequestBody.create(mediaType, ""))
                .url(PROTOCOL + HOST + PORT +
                        "/game/register?name=" + name +
                        "&password=" + pass +
                        "&passCopy=" + passCopy)
                .build();

        return client.newCall(request).execute();
    }

    //GET host:port/game/game
    public static Response viewChat(String name) throws IOException {
        Request request = new Request.Builder()
                .get()
                .url(PROTOCOL + HOST + PORT + "/game/game?name=" + name)
                .addHeader("host", HOST + PORT)
                .build();
        return client.newCall(request).execute();
    }

    //POST host:port/game/say?name=my_name&password=my_pass&msg=my_message
    //Body: "msg='my_message'"
    public static Response say(String name, String pass, String msg) throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        Request request = new Request.Builder()
                .post(RequestBody.create(mediaType, ""))
                .url(PROTOCOL + HOST + PORT + "/game/say?name=" + name + "&password=" + pass + "&msg=" + msg)
                .build();
        return client.newCall(request).execute();
    }

    //GET host:port/game/online
    public static Response viewOnline() throws IOException {
        Request request = new Request.Builder()
                .get()
                .url(PROTOCOL + HOST + PORT + "/game/online")
                .addHeader("host", HOST + PORT)
                .build();
        return client.newCall(request).execute();
    }

    //POST host:port/game/logout?name=my_name
    public static Response logout(String name, String pass) throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        Request request = new Request.Builder()
                .post(RequestBody.create(mediaType, ""))
                .url(PROTOCOL + HOST + PORT + "/game/logout?name=" + name + "&password=" + pass)
                .build();

        return client.newCall(request).execute();
    }
}