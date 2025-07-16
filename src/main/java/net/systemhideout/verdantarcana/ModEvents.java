package net.systemhideout.verdantarcana;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ModEvents {

    private static final Identifier BOOK_ADVANCEMENT_ID = Identifier.of("verdant-arcana", "moon_fledgling");
    private static final Set<UUID> processedPlayers = new HashSet<>();

    public static void registerEvents() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {

                // Skip players we've already processed
                if (processedPlayers.contains(player.getUuid())) continue;

                AdvancementEntry advancement = server.getAdvancementLoader().get(BOOK_ADVANCEMENT_ID);
                if (advancement != null &&
                        !player.getAdvancementTracker().getProgress(advancement).isDone()) {

                    VerdantArcanaMod.LOGGER.info("Granting advancement to {}", player.getGameProfile().getName());

                    player.getAdvancementTracker().grantCriterion(advancement, "joined");
                    processedPlayers.add(player.getUuid());
                }
            }
        });
    }
}
