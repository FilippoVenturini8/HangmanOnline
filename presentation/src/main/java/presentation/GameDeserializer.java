package presentation;

import com.google.gson.*;
import common.Game;
import common.GameRole;
import common.User;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GameDeserializer implements JsonDeserializer<Game> {

    private String getPropertyAsString(JsonObject object, String name) {
        if (object.has(name)) {
            JsonElement value = object.get(name);
            if (value.isJsonNull()) return null;
            return value.getAsString();
        }
        return null;
    }

    private <T> T getPropertyAs(JsonObject object, String name, Class<T> type, JsonDeserializationContext context) {
        if (object.has(name)) {
            JsonElement value = object.get(name);
            if (value.isJsonNull()) return null;
            return context.deserialize(value, type);
        }
        return null;
    }

    @Override
    public Game deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            var object = json.getAsJsonObject();

            int round = getPropertyAs(object, "round", Integer.class, context);
            int attempts = getPropertyAs(object, "attempts", Integer.class, context);
            int lastRoundAttempts = getPropertyAs(object, "lastRoundAttempts", Integer.class, context);
            var toGuess = getPropertyAsString(object, "toGuess");
            var encodedToGuess = getPropertyAsString(object, "encodedToGuess");
            var lastEncoded = getPropertyAsString(object, "lastEncoded");
            var guesserRoundWon = getPropertyAs(object, "guesserRoundWon", Boolean.class, context);

            var playersArray = object.getAsJsonArray("players");

            List<User> players = new ArrayList<>(playersArray.size());
            for (JsonElement item : playersArray) {
                if (item.isJsonNull()) continue;
                players.add(context.deserialize(item, User.class));
            }

            var resultsArray = object.getAsJsonArray("results");

            List<Integer> results = new ArrayList<>(resultsArray.size());
            for (JsonElement item : resultsArray) {
                if (item.isJsonNull()) continue;
                results.add(item.getAsInt());
            }

            return new Game(round, attempts, lastRoundAttempts, toGuess, encodedToGuess, lastEncoded, players, results, guesserRoundWon);
        }catch (ClassCastException | NullPointerException e){
            throw new JsonParseException("Invalid game: " + json, e);
        }
    }
}
