package common;

import java.util.ArrayList;
import java.util.List;

public class LocalHangman implements Hangman{

    private List<User> users = new ArrayList<>();

    private List<Lobby> lobbies = new ArrayList<>();

    private int lobbiesCounter = 0;

    @Override
    public void connectUser(User user) throws ConflictException{
        var copy = new User(user.getNickName()); //Defensive copy
        if (copy.getNickName() == null || copy.getNickName().isBlank()) {
            //TODO AGGIUNGERE ECCEZIONE: NICKNAME VUOTO
        }
        if(users.contains(copy)){
            throw new ConflictException("Nickname " + copy.getNickName() + " già in uso!");
        }
        users.add(copy);
        System.out.println("Utente aggiunto: "+copy.getNickName());
    }

    @Override
    public int createLobby(User user) {
        Lobby newLobby = new Lobby(lobbiesCounter++);
        newLobby.addUser(user);
        lobbies.add(newLobby);
        System.out.println("Lobby creata!");
        return newLobby.getId();
    }

    @Override
    public Lobby getLobby(int idLobby) throws MissingException{
        boolean foundLobby = false;
        int lobbyIndex = -1;
        for(int i = 0; i < this.lobbies.size(); i++){
            if(lobbies.get(i).getId() == idLobby){
                foundLobby = true;
                lobbyIndex = i;
                break;
            }
        }
        if(!foundLobby){
            throw new MissingException("La lobby con codice " + idLobby + " è inesistente.");
        }
        return this.lobbies.get(lobbyIndex);
    }

    @Override
    public List<Lobby> getAllLobbies() {
        return List.copyOf(this.lobbies);
    }

    @Override
    public void joinLobby(int idLobby, User user) throws MissingException, ConflictException{
        Lobby lobby = this.getLobby(idLobby); //Se non è presente la lobby viene generata una MissingException qui

        if(lobby.isFull()){
            throw new ConflictException("Lobby " + idLobby + " piena.");
        }
        lobby.addUser(user);
    }
}
