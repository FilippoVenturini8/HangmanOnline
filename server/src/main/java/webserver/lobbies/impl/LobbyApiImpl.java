package webserver.lobbies.impl;

import common.Hangman;
import common.Lobby;
import common.User;
import webserver.AbstractApi;
import webserver.lobbies.LobbyApi;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class LobbyApiImpl extends AbstractApi implements LobbyApi {

    public LobbyApiImpl(Hangman storage){
        super(storage);
    }
    @Override
    public CompletableFuture<Integer> createLobby(User user) {
        return CompletableFuture.supplyAsync(
                () -> {
                    Integer lobbyId = storage().createLobby(user);
                    return lobbyId;
                    //TODO forse ci vanno eccezioni
                }
        );
    }

    @Override
    public CompletableFuture<List<Lobby>> getAllLobbies() {
        return CompletableFuture.supplyAsync(
                () -> {
                    List<Lobby> allLobbies = storage().getAllLobbies();
                    return allLobbies;
                }
        );
    }
}
