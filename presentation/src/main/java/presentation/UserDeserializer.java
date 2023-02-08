package presentation;

import com.google.gson.*;
import common.GameRole;
import common.User;

import java.lang.reflect.Type;

public class UserDeserializer implements JsonDeserializer<User> {
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
    public User deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            var object = json.getAsJsonObject();
            var nickName = getPropertyAsString(object, "nickname");
            GameRole gameRole = getPropertyAs(object, "gameRole", GameRole.class, context);
            return new User(nickName, gameRole);
        } catch (ClassCastException e) {
            throw new JsonParseException("Invalid user: " + json, e);
        }
    }
}
