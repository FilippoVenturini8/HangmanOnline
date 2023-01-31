package common;

public interface Hangman {
    void connectUser(User user) throws ConflictException;

    void createLobby(User user);
}
