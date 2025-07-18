package net.systemhideout.verdantarcana.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.systemhideout.verdantarcana.quest.QuestLoader;
import net.systemhideout.verdantarcana.quest.QuestProgressHandler;
import net.systemhideout.verdantarcana.quest.QuestRewardManager;
import net.systemhideout.verdantarcana.screen.custom.QuestScreen;

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
        ClientPlayNetworking.registerGlobalReceiver(
                QuestCompletedPayload.ID,
                (payload, context) -> {
                    MinecraftClient client = MinecraftClient.getInstance();
                    if (client.currentScreen instanceof QuestScreen screen) {
                        client.execute(() -> {
                            screen.loadCurrentQuest();
                            screen.unlockCompleteButton(); // <-- unlock the button after update
                        });
                    }
                }
        );
    }

}
