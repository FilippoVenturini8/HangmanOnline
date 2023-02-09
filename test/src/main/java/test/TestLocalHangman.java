package test;

import common.ConflictException;
import common.Hangman;
import common.LocalHangman;
import common.MissingException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TestLocalHangman extends AbstractTestHangman {
    @Override
    protected void beforeCreatingHangman() throws IOException {

    }

    @Override
    protected Hangman createHangman() throws ConflictException {
        return new LocalHangman();
    }

    @Override
    protected void shutdownHangman(Hangman hangman) {

    }

    @Override
    protected void afterShuttingAuthenticatorDown() throws InterruptedException {

    }

    @Override
    @Test
    public void testUserConnections() throws ConflictException, MissingException {
        super.testUserConnections();
    }

    @Override
    @Test
    public void testUserDisconnections() throws ConflictException, MissingException {
        super.testUserDisconnections();
    }

    @Override
    @Test
    public void testConnectionError() {
        super.testConnectionError();
    }

    @Override
    @Test
    public void testLobbyCreation() throws MissingException {
        super.testLobbyCreation();
    }

    @Override
    @Test
    public void testLobbyCreationError() {
        super.testLobbyCreationError();
    }

    @Override
    @Test
    public void testLobbyConnection() throws MissingException, ConflictException {
        super.testLobbyConnection();
    }

    @Override
    @Test
    public void testStartGame() throws MissingException, ConflictException {
        super.testStartGame();
    }

    @Override
    @Test
    public void testChooseAndEncodeWord() throws MissingException, ConflictException {
        super.testChooseAndEncodeWord();
    }

    @Override
    @Test
    public void testFailedAttempt() throws MissingException, ConflictException {
        super.testFailedAttempt();
    }

    @Override
    @Test
    public void testCorrectAttempt() throws MissingException, ConflictException {
        super.testCorrectAttempt();
    }
}