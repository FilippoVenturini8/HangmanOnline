package webserver.users.impl;

import com.google.gson.JsonParseException;
import common.User;
import io.javalin.Javalin;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.HttpResponseException;
import webserver.AbstractController;
import webserver.users.UserApi;
import webserver.users.UserController;
import webserver.utils.Filters;

public class UserControllerImpl extends AbstractController implements UserController {

    public UserControllerImpl(String path){
        super(path);
    }

    private UserApi getApi(Context context){
        return UserApi.of(getHangmanInstance(context));
    }

    @Override
    public void postUser(Context context) throws HttpResponseException {
        UserApi api = getApi(context);
        var user = context.bodyAsClass(User.class);

        var futureResult = api.connectUser(user);
        asyncReplyWithoutBody(context, "application/json", futureResult);
    }

    @Override
    public void getUser(Context context) throws HttpResponseException {
        UserApi api = getApi(context);

        var nicknameUser = context.pathParam("{userId}");
        var futureResult = api.findUser(nicknameUser);
        asyncReplyWithBody(context,"application/json", futureResult);
    }

    @Override
    public void deleteUser(Context context) throws HttpResponseException {
        UserApi api = getApi(context);

        var nicknameUser = context.pathParam("{userId}");
        var futureResult = api.disconnectUser(nicknameUser);
        asyncReplyWithoutBody(context,"application/json", futureResult);
    }

    @Override
    public void registerRoutes(Javalin app) {
        app.before(path("*"), Filters.ensureClientAcceptsMimeType("application", "json"));
        app.post(path("/"), this::postUser);
        app.get(path("/{userId}"), this::getUser);
        app.delete(path("/{userId}"), this::deleteUser);
    }
}
