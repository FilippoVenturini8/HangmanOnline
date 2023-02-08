package common;

import java.util.List;

public interface Hangman {
    void connectUser(User user) throws ConflictException, IllegalArgumentException;

    void disconnectUser(String nicknameUser) throws MissingException;

    User findUser(String nicknameUser) throws MissingException;

    int createLobby(String nicknameUser) throws MissingException;

    void deleteLobby(int idLobby) throws MissingException;

    void joinLobby(int idLobby, String nicknameUser) throws MissingException, ConflictException;

    Lobby getLobby(int idLobby) throws MissingException;

    List<Lobby> getAllLobbies();

    void startGame(int idLobby, Game game) throws MissingException;

    String setWordToGuess(int idLobby, String toGuess) throws MissingException;

    Game getGame(int idLobby) throws MissingException;

    Boolean tryToGuess(int idLobby, String attempt) throws MissingException;

}
