package presentation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import common.Game;
import common.GameRole;
import common.Lobby;
import common.User;

public class GsonUtils {
    public static Gson createGson() {
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .serializeNulls()
                .registerTypeAdapter(User.class, new UserSerializer())
                .registerTypeAdapter(User.class, new UserDeserializer())
                .registerTypeAdapter(Lobby.class, new LobbySerializer())
                .registerTypeAdapter(Lobby.class, new LobbyDeserializer())
                .registerTypeAdapter(GameRole.class, new GameRoleSerializer())
                .registerTypeAdapter(GameRole.class, new GameRoleDeserializer())
                .registerTypeAdapter(Game.class, new GameSerializer())
                .registerTypeAdapter(Game.class, new GameDeserializer())
                .create();
    }
}
