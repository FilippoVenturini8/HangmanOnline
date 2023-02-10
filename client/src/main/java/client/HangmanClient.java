package client;

import client.cli.HangmanGraphics;
import common.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class HangmanClient extends AbstractHttpClientStub implements Hangman {
    private static final int port = 10000;

    public HangmanClient(URI host) {
        super(host, "hangman", "0.1.0");
    }

    public HangmanClient(String host, int port){
        this(URI.create("http://" + host + ":" + port));
    }

    private CompletableFuture<?> connectUserAsync(User user) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(resourceUri("/users"))
                .header("Accept", "application/json")
                .POST(body(user))
                .build();
        return sendRequestToClient(request)
                .thenComposeAsync(checkResponse());
    }

    @Override
    public void connectUser(User user) throws ConflictException, IllegalArgumentException{
        try {
            connectUserAsync(user).join();
        } catch (CompletionException e) {
            if(e.getCause() instanceof ConflictException){
                throw getCauseAs(e, ConflictException.class);
            }else if(e.getCause() instanceof IllegalArgumentException){
                throw getCauseAs(e, IllegalArgumentException.class);
            }
        }
    }

    private CompletableFuture<?> disconnectUserAsync(String nicknameUser){
        HttpRequest request = HttpRequest.newBuilder()
                .uri(resourceUri("/users/"+nicknameUser))
                .DELETE()
                .build();
        return sendRequestToClient(request)
                .thenComposeAsync(checkResponse());
    }

    @Override
    public void disconnectUser(String nicknameUser) throws MissingException {
        try {
            disconnectUserAsync(nicknameUser).join();
        }catch (CompletionException e){
            throw getCauseAs(e, MissingException.class);
        }
    }

    private CompletableFuture<User> findUserAsync(String nicknameUser){
        HttpRequest request = HttpRequest.newBuilder()
                .uri(resourceUri("/users/"+nicknameUser))
                .header("Accept", "application/json")
                .GET()
                .build();
        return sendRequestToClient(request)
                .thenComposeAsync(checkResponse())
                .thenComposeAsync(deserializeOne(User.class));
    }

    @Override
    public User findUser(String nicknameUser) throws MissingException {
        try {
            return findUserAsync(nicknameUser).join();
        } catch (CompletionException e) {
            throw getCauseAs(e, MissingException.class);
        }
    }

    private CompletableFuture<Integer> createLobbyAsync(String nicknameUser) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(resourceUri("/lobbies"))
                .header("Accept", "application/json")
                .POST(body(nicknameUser))
                .build();
        return sendRequestToClient(request)
                .thenComposeAsync(checkResponse())
                .thenComposeAsync(deserializeOne(Integer.class));
    }

    @Override
    public int createLobby(String nicknameUser) throws MissingException{
        try {
            return createLobbyAsync(nicknameUser).join();
        } catch (CompletionException e) {
            throw getCauseAs(e, MissingException.class);
        }
    }

    private CompletableFuture<?> deleteLobbyAsync(int idLobby){
        HttpRequest request = HttpRequest.newBuilder()
                .uri(resourceUri("/lobbies/"+idLobby))
                .DELETE()
                .build();
        return sendRequestToClient(request)
                .thenComposeAsync(checkResponse());
    }

    @Override
    public void deleteLobby(int idLobby) throws MissingException {
        try {
            deleteLobbyAsync(idLobby).join();
        }catch (CompletionException e){
            throw getCauseAs(e, MissingException.class);
        }
    }

    private CompletableFuture<?> joinLobbyAsync(int idLobby, String nicknameUser) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(resourceUri("/lobbies/"+idLobby))
                .header("Accept", "application/json")
                .PUT(body(nicknameUser))
                .build();
        return sendRequestToClient(request)
                .thenComposeAsync(checkResponse());
    }

    @Override
    public void joinLobby(int idLobby, String nicknameUser) throws MissingException, ConflictException {
        try {
            joinLobbyAsync(idLobby, nicknameUser).join();
        } catch (CompletionException e) {
            if(e.getCause() instanceof MissingException){
                throw getCauseAs(e, MissingException.class);
            }
            if(e.getCause() instanceof ConflictException){
                throw getCauseAs(e, ConflictException.class);
            }
        }
    }

    private CompletableFuture<Lobby> getLobbyAsync(int idLobby) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(resourceUri("/lobbies/"+idLobby))
                .header("Accept", "application/json")
                .GET()
                .build();
        return sendRequestToClient(request)
                .thenComposeAsync(checkResponse())
                .thenComposeAsync(deserializeOne(Lobby.class));
    }

    @Override
    public Lobby getLobby(int idLobby) throws MissingException{
        try {
            return getLobbyAsync(idLobby).join();
        } catch (CompletionException e) {
            throw getCauseAs(e, MissingException.class);
        }
    }

    private CompletableFuture<List<Lobby>> getAllLobbiesAsync() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(resourceUri("/lobbies"))
                .header("Accept", "application/json")
                .GET()
                .build();
        return sendRequestToClient(request)
                .thenComposeAsync(checkResponse())
                .thenComposeAsync(deserializeMany(Lobby.class));
    }

    @Override
    public List<Lobby> getAllLobbies() {
        return getAllLobbiesAsync().join();
    }

    private CompletableFuture<?> startGameAsync(int idLobby, Game game){
        HttpRequest request = HttpRequest.newBuilder()
                .uri(resourceUri("/games/"+idLobby))
                .header("Accept", "application/json")
                .POST(body(game))
                .build();
        return sendRequestToClient(request)
                .thenComposeAsync(checkResponse());
    }

    @Override
    public void startGame(int idLobby, Game game) throws MissingException {
        try {
            startGameAsync(idLobby, game).join();
        }catch (CompletionException e) {
            throw getCauseAs(e, MissingException.class);
        }
    }

    private CompletableFuture<String> setWordToGuessAsync(int idLobby, String toGuess){
        HttpRequest request = HttpRequest.newBuilder()
                .uri(resourceUri("/games/"+idLobby))
                .header("Accept", "application/json")
                .PUT(body(toGuess))
                .build();
        return sendRequestToClient(request)
                .thenComposeAsync(checkResponse())
                .thenComposeAsync(deserializeOne(String.class));
    }

    @Override
    public String setWordToGuess(int idLobby, String toGuess)throws MissingException, IllegalArgumentException {
        try {
           return setWordToGuessAsync(idLobby, toGuess).join();
        }catch (CompletionException e) {
            if(e.getCause() instanceof MissingException){
                throw getCauseAs(e, MissingException.class);
            }
            else {
                throw getCauseAs(e, IllegalArgumentException.class);
            }
        }
    }

    private CompletableFuture<String> getEncodedWordToGuessAsync(int idLobby){
        HttpRequest request = HttpRequest.newBuilder()
                .uri(resourceUri("/games/"+idLobby))
                .header("Accept", "application/json")
                .GET()
                .build();
        return sendRequestToClient(request)
                .thenComposeAsync(checkResponse())
                .thenComposeAsync(deserializeOne(String.class));
    }

    private CompletableFuture<Game> getGameAsync(int idLobby){
        HttpRequest request = HttpRequest.newBuilder()
                .uri(resourceUri("/games/"+idLobby))
                .header("Accept", "application/json")
                .GET()
                .build();
        return sendRequestToClient(request)
                .thenComposeAsync(checkResponse())
                .thenComposeAsync(deserializeOne(Game.class));
    }

    @Override
    public Game getGame(int idLobby) throws MissingException {
        try {
            return getGameAsync(idLobby).join();
        }catch (CompletionException e){
            throw getCauseAs(e, MissingException.class);
        }
    }

    private CompletableFuture<Boolean> tryToGuessAsync(int idLobby, String attempt){
        HttpRequest request = HttpRequest.newBuilder()
                .uri(resourceUri("/games/attempt/"+idLobby))
                .header("Accept", "application/json")
                .PUT(body(attempt))
                .build();
        return sendRequestToClient(request)
                .thenComposeAsync(checkResponse())
                .thenComposeAsync(deserializeOne(Boolean.class));
    }

    @Override
    public Boolean tryToGuess(int idLobby, String attempt) throws MissingException {
        try {
            return tryToGuessAsync(idLobby, attempt).join();
        }catch (CompletionException e){
            throw getCauseAs(e, MissingException.class);
        }
    }

    public static void main(String[] args) {
        HangmanClient client = new HangmanClient("localhost",port);
        Scanner scanner = new Scanner(System.in);

        boolean nickNameOk = false;
        boolean wrongOption = false;
        boolean exit = false;

        User actualUser = null;

        while (!nickNameOk){
            System.out.print("Inserire un nickname: ");
            actualUser = new User(scanner.nextLine());
            try {
                client.connectUser(actualUser);
                nickNameOk = true;
            } catch (ConflictException e) {
                System.out.println("Nickname inserito gia in uso.");
                nickNameOk = false;
            }catch (IllegalArgumentException e){
                System.out.println("Il nickname inserito non puo essere vuoto.");
                nickNameOk = false;
            }
        }

        while (true){
            System.out.println("\n############## MENU ###############");
            System.out.println("[1] Crea una lobby");
            System.out.println("[2] Visualizza la lista delle lobby");
            System.out.println("[3] Esci");
            System.out.println("###################################\n");

            System.out.print("Seleziona un'operazione: ");
            String option = scanner.nextLine();

            int lobbyId = -1;

            switch (option){
                case "1":
                    try {
                        lobbyId = client.createLobby(actualUser.getNickName());
                    } catch (MissingException e) {
                        throw new RuntimeException(e.getMessage());
                    }
                    System.out.println("\n###################################");
                    System.out.println("Lobby: "+lobbyId);
                    System.out.println("\nIn attesa di un altro giocatore...");
                    System.out.println("###################################");
                    try {
                        while (true){
                            Lobby myLobby = client.getLobby(lobbyId);
                            if(myLobby.isFull()){ //Another player is connected
                                break;
                            }
                            Thread.sleep(500);
                        }
                    } catch (MissingException e) {
                        System.out.println("Lobby "+ lobbyId + " inesistente.");
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "2":

                    boolean lobbyOk = false;

                    while (!lobbyOk){
                        List<Lobby> allLobbies = client.getAllLobbies();
                        System.out.println("\n##############LOBBIES##############");
                        for(Lobby lobby : allLobbies){
                            System.out.println("Lobby " + lobby.getId() + " : (" + lobby.getConnectedUserNumber() + "/2)");
                        }

                        System.out.println("\n[r] Refresh lista lobbies");
                        System.out.println("[0] Torna al menu");
                        System.out.println("###################################\n");

                        System.out.print("Codice lobby a cui connettersi: ");
                        String lobbyOption = scanner.nextLine();
                        if(lobbyOption.equals("r")){
                            lobbyOk = false;
                            continue;
                        }else {
                            lobbyId = Integer.parseInt(lobbyOption);
                        }

                        if(lobbyId == 0){ //Back to the menu
                            break;
                        }

                        try {
                            client.joinLobby(lobbyId, actualUser.getNickName());
                            lobbyOk = true;
                        }catch (MissingException e){
                            System.out.println("\nLobby selezionata non presente.");
                            lobbyOk = false;
                        }catch (ConflictException e){
                            System.out.println("\nLobby piena.");
                            lobbyOk = false;
                        }
                    }

                    if(lobbyId == 0){ //Back to the menu
                        break;
                    }

                    try {
                        client.startGame(Integer.valueOf(lobbyId), new Game());
                    } catch (MissingException e) {
                        throw new RuntimeException(e.getMessage());
                    }

                    break;

                case "3":
                    try {
                        client.disconnectUser(actualUser.getNickName());
                    } catch (MissingException e) {
                        throw new RuntimeException(e.getMessage());
                    }
                    exit = true;
                    break;

                default:
                    wrongOption = true;
                    break;
            }

            if(exit){
                break;
            }

            if(wrongOption || lobbyId == 0){ //Back to the menu
               continue;
            }

            boolean isGameFinished = false;
            while (!isGameFinished){
                isGameFinished = client.oneRound(actualUser, lobbyId);
            }

            if(option.equals("1")){ //The creator of the lobby delete it at the end of the game
                try {
                    client.deleteLobby(lobbyId);
                } catch (MissingException e) {
                    throw new RuntimeException(e.getMessage());
                }
            }

            System.out.println("\nPartita terminata.");
        }
    }

    private boolean oneRound(User actualUser, int idLobby) {
        Scanner scanner = new Scanner(System.in);
        Game game = null;
        int initialRound;
        int actualRound;
        int previousRoundWon;
        boolean gameFinished = false;

        try {
            GameRole myRole = this.findUser(actualUser.getNickName()).getGameRole();


            System.out.println("\n***********");
            System.out.println("* "+myRole+" *");
            System.out.println("***********\n");

            String encodedToGuess = null;
            switch (myRole){
                case CHOOSER:
                    boolean wordOk = false;
                    String toGuess = "";

                    while (!wordOk){
                        try {
                            System.out.print("Parola da far indovinare: ");
                            toGuess = scanner.nextLine();
                            encodedToGuess = this.setWordToGuess(idLobby, toGuess);
                            wordOk = true;
                        }catch (IllegalArgumentException e){
                            System.out.println("La parola deve contenere solo lettere.");
                            wordOk = false;
                        }
                    }

                    System.out.print('\n');

                    for(String c : encodedToGuess.split("")){
                        System.out.print(c+" ");
                    }
                    System.out.println('\n');

                    int actualAttempts = this.getGame(idLobby).getAttempts();
                    game = this.getGame(idLobby);

                    initialRound = game.getRound();
                    previousRoundWon = game.getRoundWon(actualUser);

                    while (true){
                        game = this.getGame(idLobby);
                        actualRound = game.getRound();

                        if(actualRound != initialRound || game.isGameFinished()){ //Next Round
                            if(!game.isGameFinished()){
                                printRoundTable(game.getRoundWon(actualUser), game.getLastRoundAttempts(), false);
                                printHangman(game.getLastRoundAttempts());
                            }else {
                                printRoundTable(game.getRoundWon(actualUser), game.getAttempts(), false);
                                printHangman(game.getAttempts());
                            }

                            if(game.getRoundWon(actualUser) > previousRoundWon){
                                System.out.println("ROUND VINTO!");
                            }else {
                                System.out.println("ROUND PERSO!");
                            }
                            gameFinished = game.isGameFinished();
                            break;
                        }

                        if(!game.getEncodedWordToGuess().equals(encodedToGuess) || actualAttempts != game.getAttempts()){

                            this.printRoundTable(game.getRoundWon(actualUser), game.getAttempts(),false);

                            printHangman(game.getAttempts());

                            encodedToGuess = game.getEncodedWordToGuess();

                            for(String c : encodedToGuess.split("")){
                                System.out.print(c+" ");
                            }
                            System.out.println('\n');

                            actualAttempts = game.getAttempts();
                        }
                        Thread.sleep(500);
                    }
                    break;
                case GUESSER:
                    System.out.println("In attesa che l'altro giocatore scelga la parola...");
                    while (encodedToGuess == null || encodedToGuess.equals("")){
                        game = this.getGame(idLobby);
                        encodedToGuess = game.getEncodedWordToGuess();
                        Thread.sleep(500);
                    }
                    System.out.println("DA INDOVINARE: \n");

                    for(String c : encodedToGuess.split("")){
                        System.out.print(c+" ");
                    }
                    System.out.println('\n');

                    initialRound = game.getRound();
                    actualRound = initialRound;
                    previousRoundWon = game.getRoundWon(actualUser);

                    while(true){
                        if(actualRound != initialRound || game.isGameFinished()){ //Next Round
                            if(!game.isGameFinished()){
                                printRoundTable(game.getRoundWon(actualUser), game.getLastRoundAttempts(), false);
                                printHangman(game.getLastRoundAttempts());
                            }else {
                                printRoundTable(game.getRoundWon(actualUser), game.getAttempts(), false);
                                printHangman(game.getAttempts());
                            }
                            if(game.getRoundWon(actualUser) > previousRoundWon){
                                System.out.println("ROUND VINTO!");
                            }else {
                                System.out.println("ROUND PERSO!");
                            }
                            gameFinished = game.isGameFinished();
                            break;
                        }

                        System.out.print("Inserire una lettera o una parola: ");
                        String attempt = scanner.nextLine();
                        if(game.getAttempts() == 1){
                            System.out.println("");
                        }
                        boolean guessed = this.tryToGuess(idLobby, attempt);
                        game = this.getGame(idLobby);

                        actualRound = game.getRound();

                        if(actualRound != initialRound || game.isGameFinished()){
                            continue; //The last attempt of the round the print is above
                        }

                        this.printRoundTable(game.getRoundWon(actualUser), game.getAttempts(), true);

                        if(guessed){
                            System.out.println("INDOVINATO!\n");
                        }else{
                            System.out.println("ERRORE!\n");
                        }

                        printHangman(game.getAttempts());

                        for(String c : game.getEncodedWordToGuess().split("")){
                            System.out.print(c+" ");
                        }
                        System.out.println('\n');
                    }

                   break;
            }

        } catch (MissingException e) {
            throw new RuntimeException(e.getCause());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return gameFinished;
    }

    private void printRoundTable(int rounds, int attempts, boolean guesser){
        if(guesser){
            System.out.println("------------------------");
            System.out.println("| Round vinti: " + rounds + "/2     |");
            System.out.println("| Tentativi rimasti: "+attempts+" |");
            System.out.println("------------------------\n");
        }else {
            System.out.println("-----------------------------------");
            System.out.println("| Round vinti: " + rounds + "/2                |");
            System.out.println("| Tentativi rimasti al guesser: "+attempts+" |");
            System.out.println("-----------------------------------\n");
        }
    }

    private void printHangman(int attempts){
        switch (attempts){
            case 4:
                System.out.println(HangmanGraphics.FOUR_ATTEMPTS);
                break;
            case 3:
                System.out.println(HangmanGraphics.THREE_ATTEMPTS);
                break;
            case 2:
                System.out.println(HangmanGraphics.TWO_ATTEMPTS);
                break;
            case 1:
                System.out.println(HangmanGraphics.ONE_ATTEMPTS);
                break;
            case 0:
                System.out.println(HangmanGraphics.ZERO_ATTEMPTS);
                break;
            default:
                break;
        }
    }

}
