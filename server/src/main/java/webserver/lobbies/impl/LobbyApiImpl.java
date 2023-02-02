package webserver.lobbies.impl;

import common.*;
import io.javalin.http.ConflictResponse;
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
    public CompletableFuture<Integer> createLobby(String nicknameUser) {
        return CompletableFuture.supplyAsync(
                () -> {
                    Integer lobbyId = null;
                    try {
                        lobbyId = storage().createLobby(nicknameUser);
                        return lobbyId;
                    } catch (MissingException e) {
                        throw new NotFoundResponse();
                    }
                }
        );
    }

    @Override
    public CompletableFuture<Lobby> getLobby(int idLobby) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        return storage().getLobby(idLobby);
                    }catch (MissingException e){
                        throw new NotFoundResponse();
                    }
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
                        throw new NotFoundResponse();
                    }catch (ConflictException e){
                        throw new ConflictResponse();
                    }
                }
        );
    }
}
