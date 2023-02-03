package presentation;

import com.google.gson.*;
import common.Game;
import common.GameRole;

import java.lang.reflect.Type;

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

        return new Game(round, attempts, toGuess, encodedToGuess);
    }
}
