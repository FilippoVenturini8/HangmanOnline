package webserver;

import common.Hangman;
import io.javalin.http.Context;
import webserver.utils.Filters;

public abstract class AbstractController {
    private final String path;

    public AbstractController(String path){
        this.path = path;
    }

    //Return the local instance of Hangman, instantiated in the HangmanService (Used in the API's)
    protected Hangman getHangmanInstance(Context context) {
        return Filters.getSingletonFromContext(Hangman.class, context);
    }

    public String path(){
        return this.path;
    }

    public String path(String subPath) {
        return this.path() + subPath;
    }

}
