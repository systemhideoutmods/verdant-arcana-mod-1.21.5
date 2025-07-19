package net.systemhideout.verdantarcana.herbs;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.systemhideout.verdantarcana.VerdantArcanaMod;
import net.systemhideout.verdantarcana.network.HerbDiscoverySyncPayload;
import net.systemhideout.verdantarcana.util.ModTags;

import java.util.*;

public class HerbDiscoveryManager {

    // Client-only herb tracker
    private static final Set<String> discoveredHerbIds = new HashSet<>();

    // Called on server when player uses or picks up a herb
    public static void tryDiscoverHerb(ServerPlayerEntity player, ItemStack stack) {
        Item item = stack.getItem();
        if (!item.getRegistryEntry().isIn(ModTags.Items.WITCHY_CROPS)) return;

        String id = Registries.ITEM.getId(item).toString();
        HerbDiscoveryComponent component = VerdantArcanaMod.getHerbData(player);
        if (component == null) return;

        if (!component.isDiscovered(id)) {
            component.discover(id);
            player.sendMessage(Text.literal("Discovered herb: " + item.getName().getString()), false);
            //Trigger Sync
            ServerPlayNetworking.send(player,
                    new HerbDiscoverySyncPayload(component.getDiscoveredHerbs().stream().toList())
            );
        }
    }

    // Used on client-side only
    public static boolean hasDiscovered(Item item) {
        Identifier id = Registries.ITEM.getId(item);
        return discoveredHerbIds.contains(id.toString());
    }

    // Called when payload is received on client
    public static void setDiscoveredHerbs(List<String> herbs) {
        discoveredHerbIds.clear();
        discoveredHerbIds.addAll(herbs);
    }

    // If needed elsewhere (e.g., lore tab filtering)
    public static Set<String> getDiscoveredHerbIds() {
        return Collections.unmodifiableSet(discoveredHerbIds);
    }
}

