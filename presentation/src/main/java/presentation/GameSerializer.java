package presentation;

import com.google.gson.*;
import common.Game;
import common.User;

import java.lang.reflect.Type;

public class GameSerializer implements JsonSerializer<Game> {
    @Override
    public JsonElement serialize(Game src, Type typeOfSrc, JsonSerializationContext context) {
        var object = new JsonObject();

        JsonArray players = new JsonArray();

        if(src.getPlayers() != null){
            for(User player : src.getPlayers()){
                players.add(context.serialize(player));
            }
        }

        JsonArray results = new JsonArray();

        if(src.getResults() != null){
            for(Integer result : src.getResults()){
                results.add(result);
            }
        }

        object.addProperty("round", src.getRound());
        object.addProperty("attempts", src.getAttempts());
        object.addProperty("lastRoundAttempts", src.getLastRoundAttempts());
        object.addProperty("toGuess", src.getWordToGuess());
        object.addProperty("encodedToGuess", src.getEncodedWordToGuess());
        object.addProperty("guesserRoundWon", src.getGuesserRoundWon());
        object.add("players", players);
        object.add("results", results);

        return object;
    }
}
