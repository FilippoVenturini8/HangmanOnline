package webserver.lobbies.impl;

import common.Lobby;
import common.User;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpResponseException;
import webserver.AbstractController;
import webserver.lobbies.LobbyApi;
import webserver.lobbies.LobbyController;
import webserver.users.UserApi;
import webserver.utils.Filters;

public class LobbyControllerImpl extends AbstractController implements LobbyController {

    public LobbyControllerImpl(String path){
        super(path);
    }

    private LobbyApi getApi(Context context){
        return LobbyApi.of(getHangmanInstance(context));
    }

    @Override
    public void postLobby(Context context) throws HttpResponseException {
        LobbyApi api = getApi(context);

        var user = context.bodyAsClass(User.class);
        var futureResult = api.createLobby(user);
        asyncReplyWithBody(context, "application/json", futureResult);
    }

    @Override
    public void getAllLobbies(Context context) throws HttpResponseException {
        LobbyApi api = getApi(context);

        var futureResult = api.getAllLobbies();
        asyncReplyWithBody(context, "application/json", futureResult);
    }

    @Override
    public void getLobby(Context context) throws HttpResponseException {
        LobbyApi api = getApi(context);

        var lobbyId = context.pathParam("{lobbyId}");
        var futureResult = api.getLobby(Integer.valueOf(lobbyId));
        asyncReplyWithBody(context, "application/json", futureResult);
    }

    @Override
    public void putLobby(Context context) throws HttpResponseException {
        LobbyApi api = getApi(context);

        var lobbyId = context.pathParam("{lobbyId}");
        var user = context.bodyAsClass(User.class);
        var futureResult = api.addUserToLobby(Integer.valueOf(lobbyId), user);
        asyncReplyWithoutBody(context, "application/json", futureResult);
    }

    @Override
    public void registerRoutes(Javalin app) {
        app.before(path("*"), Filters.ensureClientAcceptsMimeType("application", "json"));
        app.post(path("/"), this::postLobby);
        app.put(path("/{lobbyId}"), this::putLobby);
        app.get(path("/"), this::getAllLobbies);
        app.get(path("/{lobbyId}"), this::getLobby);

    }


}
