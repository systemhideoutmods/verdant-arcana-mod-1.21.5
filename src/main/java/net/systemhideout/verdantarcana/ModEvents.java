package net.systemhideout.verdantarcana;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.systemhideout.verdantarcana.herbs.HerbDiscoveryManager;
import net.systemhideout.verdantarcana.util.ModTags;

import java.util.*;

public class ModEvents {

    private static final WeakHashMap<UUID, Set<String>> trackedInventory = new WeakHashMap<>();

    public static void registerEvents() {

        // Right-click with item in hand
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (world.isClient || !(player instanceof ServerPlayerEntity serverPlayer)) return ActionResult.PASS;

            ItemStack stack = serverPlayer.getStackInHand(hand);
            HerbDiscoveryManager.tryDiscoverHerb(serverPlayer, stack);
            return ActionResult.PASS;
        });

        // Break block
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
            if (!world.isClient && player instanceof ServerPlayerEntity serverPlayer) {
                for (RegistryEntry<Item> entry : Registries.ITEM.iterateEntries(ModTags.Items.WITCHY_CROPS)) {
                    HerbDiscoveryManager.tryDiscoverHerb(serverPlayer, new ItemStack(entry.value()));
                }
            }
        });

        // Inventory scan per tick
        ServerTickEvents.END_SERVER_TICK.register((MinecraftServer server) -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                Set<String> seen = trackedInventory.computeIfAbsent(player.getUuid(), p -> new HashSet<>());

                // Access inventory safely
                for (int i = 0; i < player.getInventory().size(); i++) {
                    ItemStack stack = player.getInventory().getStack(i);
                    if (stack.isEmpty()) continue;

                    String id = Registries.ITEM.getId(stack.getItem()).toString();
                    if (!seen.contains(id)) {
                        seen.add(id);
                        HerbDiscoveryManager.tryDiscoverHerb(player, stack);
                    }
                }
            }
        });
    }
}
