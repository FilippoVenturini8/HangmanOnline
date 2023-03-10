package webserver.games.impl;

import common.Game;
import common.User;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpResponseException;
import webserver.AbstractController;
import webserver.games.GameApi;
import webserver.games.GameController;
import webserver.lobbies.LobbyApi;
import webserver.utils.Filters;

public class GameControllerImpl extends AbstractController implements GameController {


    public GameControllerImpl(String path) {
        super(path);
    }

    private GameApi getApi(Context context){
        return GameApi.of(getHangmanInstance(context));
    }

    @Override
    public void postGame(Context context) throws HttpResponseException {
        GameApi api = getApi(context);

        var game = context.bodyAsClass(Game.class);
        var idLobby = context.pathParam("{lobbyId}");
        var futureResult = api.startGame(Integer.parseInt(idLobby), game);
        asyncReplyWithoutBody(context, "application/json", futureResult);
    }

    @Override
    public void putWordToGuess(Context context) throws HttpResponseException {
        GameApi api = getApi(context);

        var toGuess = context.bodyAsClass(String.class);
        var idLobby = context.pathParam("{lobbyId}");
        var futureResult = api.setWordToGuess(Integer.parseInt(idLobby), toGuess);
        asyncReplyWithBody(context, "application/json", futureResult);
    }

    @Override
    public void getGame(Context context) throws HttpResponseException {
        GameApi api = getApi(context);

        var idLobby=context.pathParam("{lobbyId}");
        var futureResult = api.getGame(Integer.parseInt(idLobby));
        asyncReplyWithBody(context, "application/json", futureResult);
    }

    @Override
    public void putGuessAttempt(Context context) throws HttpResponseException {
        GameApi api = getApi(context);

        var idLobby=context.pathParam("{lobbyId}");
        var attempt = context.bodyAsClass(String.class);
        var futureResult = api.tryToGuess(Integer.parseInt(idLobby), attempt);
        asyncReplyWithBody(context, "application/json", futureResult);
    }

    @Override
    public void registerRoutes(Javalin app) {
        app.before(path("*"), Filters.ensureClientAcceptsMimeType("application", "json"));
        app.post(path("/{lobbyId}"), this::postGame);
        app.put(path("/{lobbyId}"),this::putWordToGuess);
        app.put(path("/attempt/{lobbyId}"),this::putGuessAttempt);
        app.get(path("/{lobbyId}"),this::getGame);
    }
}
