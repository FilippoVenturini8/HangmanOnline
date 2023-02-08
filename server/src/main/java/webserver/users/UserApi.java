package webserver.users;

import common.Hangman;
import common.User;
import webserver.users.impl.UserApiImpl;

import java.util.concurrent.CompletableFuture;

public interface UserApi {
    CompletableFuture<Void> connectUser(User user);

    CompletableFuture<Void> disconnectUser(String nicknameUser);

    CompletableFuture<User> findUser(String nicknameUser);

    static UserApi of(Hangman storage){
        return new UserApiImpl(storage);
    }
}
