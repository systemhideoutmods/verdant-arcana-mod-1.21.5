package net.systemhideout.verdantarcana.quest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

public class QuestLoader {
    private static final Gson GSON = new Gson();
    private static final String QUEST_ROOT = "quests/";

    /**
     * Load a quest JSON from the resource path.
     * Example path: "quests/chapter1/quest1.json"
     */
    public static JsonObject loadQuest(String questPath) {
        try {
            String fullPath = QUEST_ROOT + questPath;
            Identifier id = Identifier.of("verdant-arcana", fullPath);

            // This loads from: resources/assets/verdant-arcana/quests/...
            InputStream stream = QuestLoader.class.getClassLoader().getResourceAsStream("assets/" + id.getNamespace() + "/" + id.getPath());
            if (stream == null) {
                System.err.println("Quest not found: " + questPath);
                return fallbackQuest();
            }

            Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
            JsonObject json = GSON.fromJson(reader, JsonObject.class);
            reader.close();
            return json;
        } catch (Exception e) {
            e.printStackTrace();
            return fallbackQuest();
        }
    }

    public static JsonObject getCurrentQuest(ServerPlayerEntity player) {
        String path = QuestProgressHandler.getCurrentQuestPath(player);
        return loadQuest(path);
    }

    private static JsonObject fallbackQuest() {
        JsonObject fallback = new JsonObject();
        fallback.addProperty("name", "No Quest");
        fallback.addProperty("flavor", "No quest data found.");
        return fallback;
    }
}
