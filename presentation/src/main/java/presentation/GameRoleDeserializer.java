package presentation;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import common.GameRole;

import java.lang.reflect.Type;

public class GameRoleDeserializer implements JsonDeserializer<GameRole> {
    @Override
    public GameRole deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            return GameRole.valueOf(json.getAsString().toUpperCase());
        } catch (IllegalArgumentException | ClassCastException e) {
            throw new JsonParseException("Invalid game role: " + json, e);
        }
    }
}
