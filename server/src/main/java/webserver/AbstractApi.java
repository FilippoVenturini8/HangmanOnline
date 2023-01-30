package webserver;

import common.Hangman;

public abstract class AbstractApi {
    private final Hangman storage;

    public AbstractApi(Hangman storage){
        this.storage = storage;
    }

    public Hangman storage(){
        return storage;
    }
}
