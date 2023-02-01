package webserver.lobbies;

import io.javalin.http.Context;
import io.javalin.http.HttpResponseException;
import webserver.Controller;
import webserver.lobbies.impl.LobbyControllerImpl;
import webserver.users.UserController;
import webserver.users.impl.UserControllerImpl;

public interface LobbyController extends Controller {

    void postLobby(Context context) throws HttpResponseException;

    void getAllLobbies(Context context) throws HttpResponseException;

    void putLobby(Context context) throws HttpResponseException;

    static LobbyController of(String root) {
        return new LobbyControllerImpl(root);
    }

}
