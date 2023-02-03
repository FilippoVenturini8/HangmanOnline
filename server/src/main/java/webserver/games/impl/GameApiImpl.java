package webserver.games.impl;

import common.Game;
import common.Hangman;
import common.MissingException;
import io.javalin.http.NotFoundResponse;
import webserver.AbstractApi;
import webserver.games.GameApi;

import java.util.concurrent.CompletableFuture;

public class GameApiImpl extends AbstractApi implements GameApi {
    public GameApiImpl(Hangman storage){
        super(storage);
    }

    @Override
    public CompletableFuture<Void> startGame(int idLobby, Game game) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        storage().startGame(idLobby, game);
                        return null;
                    } catch (MissingException e) {
                        throw new NotFoundResponse();
                    }
                }
        );
    }

    @Override
    public CompletableFuture<String> setWordToGuess(int idLobby, String toGuess) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        return storage().setWordToGuess(idLobby, toGuess);
                    } catch (MissingException e) {
                        throw new NotFoundResponse();
                    }
                }
        );
    }

    @Override
    public CompletableFuture<String> getEncodedWordToGuess(int idLobby) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        return storage().getEncodedWordToGuess(idLobby);
                    } catch (MissingException e) {
                        throw new NotFoundResponse();
                    }
                }
        );
    }
}
