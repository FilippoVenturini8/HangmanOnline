package presentation;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import common.Game;

import java.lang.reflect.Type;

public class GameSerializer implements JsonSerializer<Game> {
    @Override
    public JsonElement serialize(Game src, Type typeOfSrc, JsonSerializationContext context) {
        var object = new JsonObject();
        object.addProperty("round", src.getRound());
        object.addProperty("attempts", src.getAttempts());
        object.addProperty("toGuess", src.getWordToGuess());
        object.addProperty("encodedToGuess", src.getEncodedWordToGuess());
        return object;
    }
}
