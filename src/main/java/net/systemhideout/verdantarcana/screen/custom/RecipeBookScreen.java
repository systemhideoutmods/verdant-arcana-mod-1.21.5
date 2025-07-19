package net.systemhideout.verdantarcana.screen.custom;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.systemhideout.verdantarcana.herbs.HerbDiscoveryManager;
import net.systemhideout.verdantarcana.util.ModTags;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class RecipeBookScreen extends Screen {
    private static final Identifier BACKGROUND_TEXTURE =
            Identifier.of("verdant-arcana-mod", "textures/gui/recipe_book/recipe_book_gui.png");

    private static final Logger LOGGER = LoggerFactory.getLogger("SystemHideout");

    private final List<ButtonWidget> tabButtons = new ArrayList<>();
    private final List<ButtonWidget> subButtons = new ArrayList<>();

    private final Map<String, List<String>> loreMap = new HashMap<>();
    private final String[] loreCategories = {"origins", "moon_phases", "charms", "rituals", "entities", "spells"};
    private final String[] loreDisplayNames = { "Origins", "Moon Phases", "Charms", "Rituals", "Entities", "Spells" };

    private int guiLeft;
    private int guiTop;
    private final int backgroundWidth = 325;
    private final int backgroundHeight = 240;
    private int selectedTab = 0;
    private int selectedLoreCategory = 0;
    private int loreScrollOffset = 0;
    private int loreMaxScroll = 0;

    public RecipeBookScreen() {
        super(Text.translatable("screen.verdant-arcana-mod.recipe_book"));
        this.selectedTab = -1;  // Default to Lore
    }

    @Override
    protected void init() {
        super.init();
        guiLeft = (width - backgroundWidth) / 2;
        guiTop = (height - backgroundHeight) / 2;

        tabButtons.clear();
        subButtons.clear();

        // Add 4 Moon Tabs + 1 Lore Tab
        for (int i = 0; i < 4; i++) {
            int x = guiLeft + 10 + (i * 55);
            int y = guiTop + 5;
            int index = i;
            ButtonWidget tab = ButtonWidget.builder(Text.literal("Moon " + (i + 1)), btn -> switchTab(index))
                    .position(x, y)
                    .size(50, 20)
                    .build();
            addDrawableChild(tab);
            tabButtons.add(tab);
        }

        ButtonWidget loreTab = ButtonWidget.builder(Text.literal("Lore"), btn -> switchTab(-1))
                .position(guiLeft + 10 + (4 * 55), guiTop + 5)
                .size(55, 20)
                .build();
        addDrawableChild(loreTab);

        loadLoreFromManager();
        switchTab(selectedTab);
    }

    private void switchTab(int index) {
        selectedTab = index;

        // Clear lore sub buttons from screen
        subButtons.forEach(this::remove);

        if (selectedTab == -1) {
            for (ButtonWidget sub : subButtons) {
                if (!children().contains(sub)) {
                    addDrawableChild(sub);
                }
            }
        }
    }

    private void switchLoreCategory(int index) {
        selectedLoreCategory = index;
        loreScrollOffset = 0;
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (selectedTab == -1) {
            loreScrollOffset = MathHelper.clamp(loreScrollOffset - (int) verticalAmount, 0, loreMaxScroll);
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        int left = guiLeft;
        int top = guiTop;
        int right = guiLeft + backgroundWidth;
        int bottom = guiTop + backgroundHeight;

        // Top area
        context.fill(0, 0, width, top, 0xB0000000);
        // Bottom area
        context.fill(0, bottom, width, height, 0xB0000000);
        // Left area
        context.fill(0, top, left, bottom, 0xB0000000);
        // Right area
        context.fill(right, top, width, bottom, 0xB0000000);
    }




    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // This draws the darkened screen background (vanilla behavior)
        renderBackground(context, mouseX, mouseY, delta);

        // Draw your GUI texture
        context.drawTexture(
                RenderLayer::getGuiTextured,
                BACKGROUND_TEXTURE,
                guiLeft, guiTop,
                0f, 0f,
                backgroundWidth, backgroundHeight,
                336, 256
        );

        // Optional: draw a debug border
        // context.drawBorder(guiLeft, guiTop, backgroundWidth, backgroundHeight, 0xFF00FF00);

        // Draw tab-specific contents
        if (selectedTab == -1) {
            renderLoreTab(context, mouseX, mouseY, delta);
        } else {
            renderRecipeTab(context, mouseX, mouseY, delta, selectedTab);
        }

        // Draw tooltips or buttons
        super.render(context, mouseX, mouseY, delta);
    }


    private void renderLoreTab(DrawContext context, int mouseX, int mouseY, float delta) {
        int textX = guiLeft + 95;
        int textY = guiTop + 35;

        String categoryKey = loreCategories[selectedLoreCategory];
        List<String> lines = loreMap.getOrDefault(categoryKey, List.of("No lore found."));
        loreMaxScroll = Math.max(0, lines.size() - 10);

        for (int i = 0; i < 10 && loreScrollOffset + i < lines.size(); i++) {
            context.drawText(this.textRenderer, lines.get(loreScrollOffset + i), textX, textY + (i * 10), 0xFFFFFF, false);
        }
    }

    private void renderRecipeTab(DrawContext context, int mouseX, int mouseY, float delta, int index) {
        if (index == 0) {
            context.drawText(this.textRenderer, "Discovered Herbs", (guiLeft + 10), (guiTop + 35), 0xFFFFFF, false);

            int startX = guiLeft + 10;
            int startY = guiTop + 50;
            int slot = 0;

            for (RegistryEntry<Item> entry : Registries.ITEM.iterateEntries(ModTags.Items.WITCHY_CROPS)) {
                Item herb = entry.value();
                ItemStack stack = new ItemStack(herb);

                int x = startX + (slot % 4) * 60;
                int y = startY + (slot / 4) * 60;

                boolean discovered = HerbDiscoveryManager.hasDiscovered(herb);

                if (discovered) {
                    context.drawItem(stack, x, y);
                    context.drawText(this.textRenderer, stack.getName(), x, y + 20, 0xFFFFFF, false);
                } else {
                    context.fill(x, y, x + 16, y + 16, 0xFF444444);
                    context.drawText(this.textRenderer, "???", x, y + 20, 0x888888, false);
                }

                slot++;
            }
        } else {
            context.drawText(this.textRenderer, "Recipes for Moon Totem " + (index + 1), guiLeft + 10, guiTop + 35, 0xFFFFFF, false);

            int startX = guiLeft + 10;
            int startY = guiTop + 50;

            for (int i = 0; i < 6; i++) {
                int x = startX + (i % 3) * 50;
                int y = startY + (i / 3) * 40;
                context.fill(x, y, x + 40, y + 30, 0xFF333333);
                context.drawText(this.textRenderer, "???", x + 12, y + 10, 0xFFFFFF, false);
            }
        }
    }

    private void loadLoreFromManager() {
        for (int i = 0; i < loreCategories.length; i++) {
            String key = loreCategories[i];
            if (net.systemhideout.verdantarcana.lore.LoreManager.hasLore(key)) {
                List<String> lines = net.systemhideout.verdantarcana.lore.LoreManager.getLore(key);
                loreMap.put(key, lines);

                int index = i;
                ButtonWidget sub = ButtonWidget.builder(Text.literal(loreDisplayNames[i]), btn -> switchLoreCategory(index))
                        .position(guiLeft + 5, guiTop + 35 + (subButtons.size() * 25))
                        .size(80, 20)
                        .build();
                subButtons.add(sub);
            }
        }
    }
}
