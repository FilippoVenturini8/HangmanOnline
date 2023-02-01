package webserver.lobbies.impl;

import common.Hangman;
import common.Lobby;
import common.MissingException;
import common.User;
import io.javalin.http.NotFoundResponse;
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

    @Override
    public CompletableFuture<Void> addUserToLobby(int lobbyId, User user) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        storage().joinLobby(lobbyId, user);
                        return null;
                    }catch (MissingException e){
                        throw  new NotFoundResponse();
                    }
                }
        );
    }
}
