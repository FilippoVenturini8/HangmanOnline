package common;

import java.util.List;

public interface Hangman {
    void connectUser(User user) throws ConflictException;

    int createLobby(User user);

    Lobby getLobby(int idLobby) throws MissingException;

    List<Lobby> getAllLobbies();

    void joinLobby(int idLobby, User user) throws MissingException, ConflictException;
}
