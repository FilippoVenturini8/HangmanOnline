package presentation;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import common.GameRole;

import java.lang.reflect.Type;

public class GameRoleSerializer implements JsonSerializer<GameRole> {
    @Override
    public JsonElement serialize(GameRole src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.name().toLowerCase());
    }
}
