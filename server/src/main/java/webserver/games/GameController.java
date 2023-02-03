package webserver.games;

import io.javalin.http.Context;
import io.javalin.http.HttpResponseException;
import webserver.Controller;
import webserver.games.impl.GameControllerImpl;
import webserver.lobbies.LobbyController;
import webserver.lobbies.impl.LobbyControllerImpl;

public interface GameController extends Controller {
    void postGame(Context context) throws HttpResponseException;

    void putWordToGuess(Context context) throws HttpResponseException;

    void getGame(Context context) throws HttpResponseException;

    void putGuessAttempt(Context context) throws HttpResponseException;

    static GameController of(String root) {
        return new GameControllerImpl(root);
    }
}
