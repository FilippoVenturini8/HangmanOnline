package webserver;

import common.Hangman;
import io.javalin.http.Context;
import webserver.utils.Filters;

import java.util.concurrent.CompletableFuture;

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

    protected <T> void asyncReplyWithBody(Context ctx, String contentType, CompletableFuture<T> futureResult) {
        ctx.contentType(contentType);
        ctx.future(() -> futureResult.thenAccept(ctx::json));
    }

    protected void asyncReplyWithoutBody(Context ctx, String contentType, CompletableFuture<?> futureResult) {
        ctx.contentType(contentType);
        ctx.future(() -> futureResult);
    }

}
