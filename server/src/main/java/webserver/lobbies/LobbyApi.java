package webserver.lobbies;

import common.Hangman;
import common.Lobby;
import common.User;
import webserver.lobbies.impl.LobbyApiImpl;
import webserver.users.UserApi;
import webserver.users.impl.UserApiImpl;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface LobbyApi {
    CompletableFuture<Integer> createLobby(String nicknameUser);

    CompletableFuture<Void> deleteLobby(int idLobby);

    CompletableFuture<Lobby> getLobby(int idLobby);

    CompletableFuture<List<Lobby>> getAllLobbies();

    CompletableFuture<Void> addUserToLobby(int lobbyId, String nicknameUser);

    static LobbyApi of(Hangman storage){
        return new LobbyApiImpl(storage);
    }
}
