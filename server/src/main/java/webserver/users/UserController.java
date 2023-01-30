package webserver.users;

import io.javalin.http.Context;
import io.javalin.http.HttpResponseException;
import webserver.Controller;
import webserver.users.impl.UserControllerImpl;

public interface UserController extends Controller {
    void postUser(Context context) throws HttpResponseException;

    static UserController of(String root) {
        return new UserControllerImpl(root);
    }
}
