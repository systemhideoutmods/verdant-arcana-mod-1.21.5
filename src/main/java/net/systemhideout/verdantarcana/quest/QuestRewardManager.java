package net.systemhideout.verdantarcana.quest;

import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.systemhideout.verdantarcana.lore.LoreManager;

import java.util.List;

public class QuestRewardManager {

    public static void applyRewards(JsonObject rewardsJson, ServerPlayerEntity player) {
        if (rewardsJson == null || rewardsJson.isJsonNull()) return;

        // Give XP
        if (rewardsJson.has("xp")) {
            int xp = JsonHelper.getInt(rewardsJson, "xp", 0);
            if (xp > 0) {
                player.addExperience(xp);
                player.sendMessage(Text.literal("You gained " + xp + " XP!"), false);
            }
        }

        // Give items
        if (rewardsJson.has("items")) {
            for (JsonElement element : rewardsJson.getAsJsonArray("items")) {
                if (!element.isJsonObject()) continue;
                JsonObject itemObj = element.getAsJsonObject();

                String itemId = JsonHelper.getString(itemObj, "id");
                int count = JsonHelper.getInt(itemObj, "count", 1);

                Identifier id = Identifier.tryParse(itemId);
                if (id == null || !Registries.ITEM.containsId(id)) {
                    player.sendMessage(Text.literal("Invalid reward item: " + itemId), false);
                    continue;
                }

                ItemStack stack = new ItemStack(Registries.ITEM.get(id), count);
                if (!player.getInventory().insertStack(stack)) {
                    player.dropItem(stack, false);
                }
                player.sendMessage(Text.literal("You received: " + stack.getCount() + "x " + stack.getName().getString()), false);
            }
        }

        // Unlock lore
        if (rewardsJson.has("lore")) {
            String loreKey = JsonHelper.getString(rewardsJson, "lore");
            if (LoreManager.hasLore(loreKey)) {
                List<String> lines = LoreManager.getLore(loreKey);
                player.sendMessage(Text.literal("Lore unlocked: " + loreKey), false);
                for (String line : lines) {
                    player.sendMessage(Text.literal("§7" + line), false); // Gray formatting
                }
            } else {
                player.sendMessage(Text.literal("§cLore not found: " + loreKey), false);
            }
        }
    }
}
