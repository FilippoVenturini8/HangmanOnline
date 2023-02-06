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
        var object = json.getAsJsonObject();

        int round = getPropertyAs(object, "round", Integer.class, context);
        int attempts = getPropertyAs(object, "attempts", Integer.class, context);
        var toGuess = getPropertyAsString(object, "toGuess");
        var encodedToGuess = getPropertyAsString(object, "encodedToGuess");

        var playersArray = object.getAsJsonArray("players");

        List<User> players = new ArrayList<>(playersArray.size());
        for (JsonElement item : playersArray) {
            if (item.isJsonNull()) continue;
            players.add(context.deserialize(item, User.class)); //TODO NOME PROPRIETà ?
        }

        var resultsArray = object.getAsJsonArray("results");

        List<Integer> results = new ArrayList<>(resultsArray.size());
        for (JsonElement item : resultsArray) {
            if (item.isJsonNull()) continue;
            results.add(item.getAsInt()); //TODO NOME PROPRIETà ?
        }

        return new Game(round, attempts, toGuess, encodedToGuess, players, results);
    }
}
