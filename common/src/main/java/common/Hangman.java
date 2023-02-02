package common;

import java.util.List;

public interface Hangman {
    void connectUser(User user) throws ConflictException;

    User findUser(String nicknameUser) throws MissingException;

    int createLobby(String nicknameUser) throws MissingException;

    Lobby getLobby(int idLobby) throws MissingException;

    List<Lobby> getAllLobbies();

    void startGame(int idLobby, Game game) throws MissingException;

    void joinLobby(int idLobby, User user) throws MissingException, ConflictException;
}
