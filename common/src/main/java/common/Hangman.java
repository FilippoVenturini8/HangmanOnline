package common;

import java.util.List;

public interface Hangman {
    void connectUser(User user) throws ConflictException;

    User findUser(String nicknameUser) throws MissingException;

    int createLobby(String nicknameUser) throws MissingException;

    Lobby getLobby(int idLobby) throws MissingException;

    List<Lobby> getAllLobbies();

    void startGame(int idLobby, Game game) throws MissingException;

    String setWordToGuess(int idLobby, String toGuess) throws MissingException;

    String getEncodedWordToGuess(int idLobby) throws MissingException;

    void joinLobby(int idLobby, String nicknameUser) throws MissingException, ConflictException;
}
