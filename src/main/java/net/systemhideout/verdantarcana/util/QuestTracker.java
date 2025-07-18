package net.systemhideout.verdantarcana.util;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.advancement.PlacedAdvancement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientAdvancementManager;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class QuestTracker {
    private static final Logger LOGGER = LoggerFactory.getLogger("VerdantArcanaQuestTracker");

    private static final String FALLBACK_QUEST = "quests/chapter0/quest0.json";
    private static final String INITIAL_QUEST = "quests/chapter1/quest1.json";

    private static String currentQuest = INITIAL_QUEST;
    private static final Set<Identifier> completedChapters = new HashSet<>();
    private static final Map<Identifier, Advancement> advancementById = new HashMap<>();


    public static void init() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.player.networkHandler == null) return;

        ClientAdvancementManager manager = client.player.networkHandler.getAdvancementHandler();

        manager.setListener(new ClientAdvancementManager.Listener() {

            @Override
            public void setProgress(PlacedAdvancement advancement, AdvancementProgress progress) {
                Identifier id = advancement.getAdvancementEntry().id();
                if (isQuestChapterAdvancement(id) && progress.isDone()) {
                    LOGGER.info("Quest advancement completed: {}", id);
                    completedChapters.add(id);
                    updateQuestPath();
                }
            }

            @Override
            public void onClear() {
                advancementById.clear();
                completedChapters.clear();
            }

            @Override
            public void selectTab(AdvancementEntry entry) {
                // Optional: UI tab selected
            }

            @Override
            public void onDependentAdded(PlacedAdvancement advancement) {
                // Optional
            }

            @Override
            public void onDependentRemoved(PlacedAdvancement advancement) {
                // Optional
            }

            @Override
            public void onRootRemoved(PlacedAdvancement advancement) {
                // Optional
            }

            @Override
            public void onRootAdded(PlacedAdvancement advancement) {
                // Optional
            }
        });

    }

    private static boolean isQuestChapterAdvancement(Identifier id) {
        return id.getNamespace().equals("verdant-arcana-mod") && id.getPath().startsWith("quest/chapter");
    }

    private static void updateQuestPath() {
        MinecraftClient client = MinecraftClient.getInstance();
        int highest = 0;

        for (int i = 1; i <= 100; i++) {
            Identifier id = Identifier.of("verdant-arcana-mod", "quest/chapter" + i);
            if (completedChapters.contains(id)) {
                highest = i;
            } else {
                break;
            }
        }

        String nextQuest = "quests/chapter" + (highest + 1) + "/quest1.json";
        Identifier nextId = Identifier.of("verdant-arcana-mod", nextQuest);

        Optional<?> resource = client.getResourceManager().getResource(nextId);
        if (resource.isPresent()) {
            currentQuest = nextQuest;
            LOGGER.info("Quest updated to: {}", nextQuest);
        } else {
            currentQuest = FALLBACK_QUEST;
            LOGGER.info("No future quest found, using fallback.");
        }
    }

    public static String getCurrentQuestPath() {
        return currentQuest;
    }

    public static boolean isFutureQuestAvailable() {
        MinecraftClient client = MinecraftClient.getInstance();
        int highest = 0;

        for (int i = 1; i <= 100; i++) {
            Identifier id = Identifier.of("verdant-arcana-mod", "quest/chapter" + i);
            if (completedChapters.contains(id)) {
                highest = i;
            } else {
                break;
            }
        }

        String nextQuest = "quests/chapter" + (highest + 1) + "/quest1.json";
        Identifier nextId = Identifier.of("verdant-arcana-mod", nextQuest);
        return client.getResourceManager().getResource(nextId).isPresent();
    }
}
