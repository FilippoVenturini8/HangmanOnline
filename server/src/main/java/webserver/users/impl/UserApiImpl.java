package webserver.users.impl;

import common.Hangman;
import common.User;
import webserver.AbstractApi;
import webserver.users.UserApi;

import java.util.concurrent.CompletableFuture;

public class UserApiImpl extends AbstractApi implements UserApi {

    public UserApiImpl(Hangman storage){
        super(storage);
    }

    @Override
    public CompletableFuture<User> connectUser(User user) {
        //TODO implementare
        return null;
    }
}
