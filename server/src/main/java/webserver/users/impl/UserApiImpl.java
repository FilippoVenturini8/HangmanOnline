package webserver.users.impl;

import common.ConflictException;
import common.Hangman;
import common.User;
import io.javalin.http.ConflictResponse;
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
}
