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
    @Test
    public void testUserConnections() throws ConflictException, MissingException {
        super.testUserConnections();
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
}
