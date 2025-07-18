package net.systemhideout.verdantarcana.lore;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class LoreManager {
    private static final Logger LOGGER = LoggerFactory.getLogger("VerdantArcanaLore");
    private static final Map<String, List<String>> LORE_ENTRIES = new HashMap<>();
    private static final String LORE_PATH = "lore";

    public static void loadAllLore(ResourceManager resourceManager) {
        LORE_ENTRIES.clear();

        try {
            Map<Identifier, Resource> resources = resourceManager.findResources(
                    LORE_PATH, id -> id.getPath().endsWith(".json")
            );

            for (Map.Entry<Identifier, Resource> entry : resources.entrySet()) {
                Identifier id = entry.getKey();
                Resource resource = entry.getValue();

                try (InputStreamReader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
                    JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();

                    if (json.has("lines")) {
                        JsonArray lines = json.getAsJsonArray("lines");
                        List<String> loreLines = new ArrayList<>();
                        for (JsonElement line : lines) {
                            loreLines.add(line.getAsString());
                        }

                        // Normalize the key: remove "lore/" prefix and ".json" suffix
                        String loreId = id.getPath().substring(LORE_PATH.length() + 1).replace(".json", "");
                        LORE_ENTRIES.put(loreId, loreLines);
                    }
                } catch (Exception e) {
                    LOGGER.warn("Failed to parse lore file: " + id, e);
                }
            }

            LOGGER.info("Loaded {} lore entries.", LORE_ENTRIES.size());
        } catch (Exception e) {
            LOGGER.error("Failed to load lore resources.", e);
        }
    }

    public static List<String> getLore(String key) {
        return LORE_ENTRIES.getOrDefault(key, Collections.emptyList());
    }

    public static boolean hasLore(String key) {
        return LORE_ENTRIES.containsKey(key);
    }
}
