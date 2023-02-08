package webserver.users.impl;

import common.ConflictException;
import common.Hangman;
import common.MissingException;
import common.User;
import io.javalin.http.ConflictResponse;
import io.javalin.http.NotFoundResponse;
import webserver.AbstractApi;
import webserver.users.UserApi;

import java.util.concurrent.CompletableFuture;

public class UserApiImpl extends AbstractApi implements UserApi {

    public UserApiImpl(Hangman storage){
        super(storage);
    }

    @Override
    public CompletableFuture<Void> connectUser(User user) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try{
                        storage().connectUser(user);
                        return null;
                    }catch (ConflictException e) {
                        throw new ConflictResponse();
                    }catch (Exception e){
                        //TODO aggiungere eccezione
                        System.out.println("ERRORE CONNESSIONE UTENTE");
                        return null;
                    }
                }
        );
    }

    @Override
    public CompletableFuture<Void> disconnectUser(String nicknameUser) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                        storage().disconnectUser(nicknameUser);
                        return null;
                    } catch (MissingException e) {
                        throw new NotFoundResponse();
                    }
                }
        );
    }

    @Override
    public CompletableFuture<User> findUser(String nicknameUser) {
        return CompletableFuture.supplyAsync(
                () -> {
                    try {
                       return storage().findUser(nicknameUser);
                    }catch (MissingException e){
                        throw new NotFoundResponse();
                    }
                }
        );
    }
}
