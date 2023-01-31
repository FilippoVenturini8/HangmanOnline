package presentation;

import com.google.gson.*;
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

    @Override
    public User deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        var object = json.getAsJsonObject();
        var nickName = getPropertyAsString(object, "nickname");

        return new User(nickName);
    }
}
