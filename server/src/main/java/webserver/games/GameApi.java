package webserver.games;

import common.Game;
import common.Hangman;
import webserver.games.impl.GameApiImpl;
import webserver.lobbies.LobbyApi;
import webserver.lobbies.impl.LobbyApiImpl;

import java.util.concurrent.CompletableFuture;

public interface GameApi {
    CompletableFuture<Void> startGame(int idLobby, Game game);

    CompletableFuture<String> setWordToGuess(int idLobby, String toGuess);

    CompletableFuture<Game> getGame(int idLobby);

    static GameApi of(Hangman storage){
        return new GameApiImpl(storage);
    }
}
