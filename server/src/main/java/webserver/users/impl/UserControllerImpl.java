package webserver.users.impl;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpResponseException;
import webserver.AbstractController;
import webserver.users.UserApi;
import webserver.users.UserController;

public class UserControllerImpl extends AbstractController implements UserController {

    public UserControllerImpl(String path){
        super(path);
    }

    private UserApi getApi(Context context){
        return UserApi.of(getHangmanInstance(context));
    }

    @Override
    public void registerRoutes(Javalin app) {

    }

    @Override
    public void postUser(Context context) throws HttpResponseException {

    }
}
