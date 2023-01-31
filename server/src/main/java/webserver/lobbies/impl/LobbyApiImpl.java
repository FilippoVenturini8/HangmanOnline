package webserver.lobbies.impl;

import common.Hangman;
import common.Lobby;
import common.User;
import webserver.AbstractApi;
import webserver.lobbies.LobbyApi;

import java.util.concurrent.CompletableFuture;

public class LobbyApiImpl extends AbstractApi implements LobbyApi {

    public LobbyApiImpl(Hangman storage){
        super(storage);
    }
    @Override
    public CompletableFuture<Void> createLobby(User user) {
        return CompletableFuture.supplyAsync(
                () -> {
                    storage().createLobby(user);
                    return null;
                    //TODO forse ci vanno eccezioni
                }
        );
    }
}
