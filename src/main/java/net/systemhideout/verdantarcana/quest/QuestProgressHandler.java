package net.systemhideout.verdantarcana.quest;

import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.text.Text;
import net.minecraft.advancement.AdvancementProgress;

public class QuestProgressHandler {

    private static final String QUEST_NAMESPACE = "verdant-arcana";
    private static final String FALLBACK_QUEST_PATH = "quests/chapter0/quest0.json";
    private static final String INITIAL_QUEST_PATH = "quests/chapter1/quest1.json";

    public static String getCurrentQuestPath(ServerPlayerEntity player) {
        String path = INITIAL_QUEST_PATH;
        if (!isQuestCompleted(player, path)) {
            return path;
        }

        int chapter = 1;
        int quest = 2;

        while (true) {
            String next = "quests/chapter" + chapter + "/quest" + quest + ".json";
            if (!doesQuestExist(player, next)) break;
            if (!isQuestCompleted(player, next)) return next;
            quest++;
        }

        return FALLBACK_QUEST_PATH;
    }

    public static boolean isQuestCompleted(ServerPlayerEntity player, String questPath) {
        Identifier id = Identifier.of(QUEST_NAMESPACE, questPath);
        AdvancementEntry entry = player.server.getAdvancementLoader().get(id);
        if (entry == null) return false;
        AdvancementProgress progress = player.getAdvancementTracker().getProgress(entry);
        return progress != null && progress.isDone();
    }

    public static boolean doesQuestExist(ServerPlayerEntity player, String questPath) {
        Identifier id = Identifier.of(QUEST_NAMESPACE, questPath);
        return player.server.getAdvancementLoader().get(id) != null;
    }

    public static void completeQuest(ServerPlayerEntity player, String questPath) {
        Identifier id = Identifier.of(QUEST_NAMESPACE, questPath);
        AdvancementEntry entry = player.server.getAdvancementLoader().get(id);
        if (entry != null) {
            player.getAdvancementTracker().grantCriterion(entry, "complete");
        }
    }

    public static void resetProgress(ServerPlayerEntity player) {
        player.sendMessage(Text.literal("Resetting all quest progress..."), false);
        for (AdvancementEntry entry : player.server.getAdvancementLoader().getAdvancements()) {
            Identifier id = entry.id();
            if (id.getNamespace().equals(QUEST_NAMESPACE) && id.getPath().startsWith("quests/")) {
                AdvancementProgress progress = player.getAdvancementTracker().getProgress(entry);
                if (progress != null) {
                    for (String criterion : progress.getObtainedCriteria()) {
                        player.getAdvancementTracker().revokeCriterion(entry, criterion);
                    }
                }
            }
        }
    }
}
