package net.systemhideout.verdantarcana.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.network.ServerPlayerEntity;
import net.systemhideout.verdantarcana.VerdantArcanaMod;
import net.systemhideout.verdantarcana.herbs.HerbDiscoveryComponent;
import net.systemhideout.verdantarcana.herbs.HerbDiscoveryManager;
import net.systemhideout.verdantarcana.quest.QuestLoader;
import net.systemhideout.verdantarcana.quest.QuestProgressHandler;
import net.systemhideout.verdantarcana.quest.QuestRewardManager;
import net.systemhideout.verdantarcana.screen.custom.QuestScreen;

import java.util.HashSet;

public class ModNetworking {
    public static void register() {
        // Register codec
        PayloadTypeRegistry.playC2S().register(
                CompleteQuestPayload.ID,
                CompleteQuestPayload.CODEC
        );

        // Register handler
        ServerPlayNetworking.registerGlobalReceiver(
                CompleteQuestPayload.ID,
                (payload, context) -> {
                    context.player().server.execute(() -> {
                        String path = QuestProgressHandler.getCurrentQuestPath(context.player());
                        QuestProgressHandler.completeQuest(context.player(), path);
                        QuestRewardManager.applyRewards(
                                QuestLoader.loadQuest(path).getAsJsonObject("rewards"),
                                context.player()
                        );
                        ServerPlayNetworking.send(context.player(), new QuestCompletedPayload());
                    });
                }
        );
        PayloadTypeRegistry.playS2C().register(
                QuestCompletedPayload.ID,
                QuestCompletedPayload.CODEC
        );
    }
    public static void sendCompleteQuestPacket() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null && client.getNetworkHandler() != null) {
            ClientPlayNetworking.send(new CompleteQuestPayload());
        }
    }

    public static void registerClient() {
        // Register S2C payload type FIRST before setting any handlers
        PayloadTypeRegistry.playS2C().register(
                HerbDiscoverySyncPayload.ID,
                HerbDiscoverySyncPayload.CODEC
        );

        // Register handler for quest completion (S2C)
        ClientPlayNetworking.registerGlobalReceiver(
                QuestCompletedPayload.ID,
                (payload, context) -> {
                    MinecraftClient client = MinecraftClient.getInstance();
                    if (client.currentScreen instanceof QuestScreen screen) {
                        client.execute(() -> {
                            screen.loadCurrentQuest();
                            screen.unlockCompleteButton(); // unlock the button after update
                        });
                    }
                }
        );

        // Register handler for herb discovery sync (S2C)
        ClientPlayNetworking.registerGlobalReceiver(
                HerbDiscoverySyncPayload.ID,
                (payload, context) -> {
                    MinecraftClient client = MinecraftClient.getInstance();
                    client.execute(() -> {
                        HerbDiscoveryManager.setDiscoveredHerbs(payload.discoveredHerbs());
                    });
                }
        );
    }

    public static void registerEvents() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayerEntity player = handler.player;
            HerbDiscoveryComponent component = VerdantArcanaMod.getHerbData(player);

            if (component != null) {
                // Sync discovered herbs to the client
                ServerPlayNetworking.send(player,
                        new HerbDiscoverySyncPayload(
                                component.getDiscoveredHerbs().stream().toList() // convert Set to List
                        )
                );
            }
        });
    }
}
