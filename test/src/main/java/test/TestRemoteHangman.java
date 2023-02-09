package test;

import client.HangmanClient;
import common.ConflictException;
import common.Hangman;
import common.MissingException;
import org.junit.jupiter.api.Test;
import webserver.HangmanService;

import java.io.IOException;

public class TestRemoteHangman extends AbstractTestHangman{

    private static final int port = 10000;

    private HangmanService service;

    @Override
    protected void beforeCreatingHangman() throws IOException {
        service = new HangmanService(port);
        service.start();
    }

    @Override
    protected Hangman createHangman() throws ConflictException {
        return new HangmanClient("localhost", port);
    }

    @Override
    protected void shutdownHangman(Hangman hangman) {

    }

    @Override
    protected void afterShuttingAuthenticatorDown() throws InterruptedException {
        service.stop();
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
