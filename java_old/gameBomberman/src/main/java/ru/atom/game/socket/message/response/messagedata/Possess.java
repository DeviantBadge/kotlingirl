package ru.atom.game.socket.message.response.messagedata;

public class Possess {
    private final int h;
    private final int w;
    private final int possess;


    public Possess(int h, int w, int possess){
        this.h = h;
        this.w = w;
        this.possess = possess;
    }

    public int getH() {
        return h;
    }

    public int getW() {
        return w;
    }

    public int getPossess() {
        return possess;
    }
}
