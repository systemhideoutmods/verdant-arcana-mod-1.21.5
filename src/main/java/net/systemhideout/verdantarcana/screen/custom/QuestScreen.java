package net.systemhideout.verdantarcana.screen.custom;

import com.google.gson.JsonObject;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.systemhideout.verdantarcana.network.ModNetworking;
import net.systemhideout.verdantarcana.quest.QuestLoader;

public class QuestScreen extends Screen {
    private static final Identifier BACKGROUND_TEXTURE = Identifier.of("verdant-arcana-mod", "textures/gui/quest_gui.png");

    private String questName = "";
    private String flavorText = "";
    private String rewardsPreview = "";

    private int guiLeft;
    private int guiTop;
    private final int backgroundWidth = 256;
    private final int backgroundHeight = 200;

    private ButtonWidget completeButton;
    private JsonObject currentQuestJson;

    public QuestScreen() {
        super(Text.literal("Quest Log"));
    }

    @Override
    protected void init() {
        guiLeft = (width - backgroundWidth) / 2;
        guiTop = (height - backgroundHeight) / 2;

        completeButton = ButtonWidget.builder(Text.literal("Complete Quest"), button -> completeCurrentQuest())
                .position(guiLeft + 80, guiTop + 170)
                .size(100, 20)
                .build();

        addDrawableChild(completeButton);

        loadCurrentQuest(); // Initialize quest content
    }

    private void completeCurrentQuest() {
        if (client != null && client.player != null) {
            completeButton.active = false; // lock button to prevent double submit
            ModNetworking.sendCompleteQuestPacket();
        }
    }

    public void unlockCompleteButton() {
        if (completeButton != null) {
            completeButton.active = true;
            client.player.playSound(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, 1.0F, 1.0F);
        }
    }

    public void loadCurrentQuest() {
        if (client == null || client.player == null) return;

        currentQuestJson = QuestLoader.getCurrentQuest(client.player.getServer().getPlayerManager().getPlayer(client.player.getUuid()));
        if (currentQuestJson == null) {
            questName = "Unknown";
            flavorText = "No quest data found.";
            rewardsPreview = "-";
            return;
        }

        questName = currentQuestJson.has("name") ? currentQuestJson.get("name").getAsString() : "Unnamed Quest";
        flavorText = currentQuestJson.has("flavor") ? currentQuestJson.get("flavor").getAsString() : "";
        rewardsPreview = currentQuestJson.has("rewards") ? summarizeRewards(currentQuestJson.getAsJsonObject("rewards")) : "-";
    }

    private String summarizeRewards(JsonObject rewards) {
        StringBuilder sb = new StringBuilder();
        if (rewards.has("xp")) sb.append(rewards.get("xp").getAsInt()).append(" XP, ");
        if (rewards.has("items")) sb.append("Items, ");
        if (rewards.has("lore")) sb.append("Lore, ");
        return sb.length() > 0 ? sb.substring(0, sb.length() - 2) : "-";
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        context.drawTexture(RenderLayer::getGuiTextured, BACKGROUND_TEXTURE, guiLeft, guiTop, 0f, 0f, backgroundWidth, backgroundHeight, 256, 256);

        int textX = guiLeft + 15;
        context.drawText(textRenderer, Text.literal("Quest: " + questName), textX, guiTop + 20, 0xFFFFFF, false);
        context.drawText(textRenderer, Text.literal("Flavor: " + flavorText), textX, guiTop + 40, 0xAAAAAA, false);
        context.drawText(textRenderer, Text.literal("Rewards: " + rewardsPreview), textX, guiTop + 70, 0x44FF44, false);

        super.render(context, mouseX, mouseY, delta);
    }
}

