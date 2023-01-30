package webserver.users;

import common.Hangman;
import common.User;
import webserver.users.impl.UserApiImpl;

import java.util.concurrent.CompletableFuture;

public interface UserApi {
    CompletableFuture<User> connectUser(User user);

    static UserApi of(Hangman storage){
        return new UserApiImpl(storage);
    }
}
