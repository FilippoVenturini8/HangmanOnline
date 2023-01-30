package webserver;

import io.javalin.http.Context;

public abstract class AbstractController {
    private final String path;

    public AbstractController(String path){
        this.path = path;
    }

    //TODO capire e aggiungere
//    protected Authenticator getAuthenticatorInstance(Context context) {
//        return Filters.getSingletonFromContext(Authenticator.class, context);
//    }

    public String path(){
        return this.path;
    }

    public String path(String subPath) {
        return this.path() + subPath;
    }

}
