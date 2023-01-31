package webserver.lobbies;

import common.Hangman;
import common.Lobby;
import common.User;
import webserver.lobbies.impl.LobbyApiImpl;
import webserver.users.UserApi;
import webserver.users.impl.UserApiImpl;

import java.util.concurrent.CompletableFuture;

public interface LobbyApi {
    CompletableFuture<Integer> createLobby(User user);

    static LobbyApi of(Hangman storage){
        return new LobbyApiImpl(storage);
    }
}
