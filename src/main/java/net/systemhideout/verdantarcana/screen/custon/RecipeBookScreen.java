package net.systemhideout.verdantarcana.screen.custon;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class RecipeBookScreen extends Screen {
    private static final Identifier BACKGROUND_TEXTURE =
            Identifier.of("verdant-arcana-mod", "textures/gui/recipe_book/recipe_book_gui.png");
    private static final Identifier LORE_JSON =
            Identifier.of("verdant-arcana-mod", "lore/lore.json");

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
        loadLoreFromFile();     // Load lore on screen creation
    }

    @Override
    protected void init() {
        super.init();
        guiLeft = (width - backgroundWidth) / 2;
        guiTop = (height - backgroundHeight) / 2;

        tabButtons.clear();
        subButtons.clear();

        // Add Moon Tabs
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

        // Lore tab
        ButtonWidget loreTab = ButtonWidget.builder(Text.literal("Lore"), btn -> switchTab(-1))
                .position(guiLeft + 10 + (4 * 55), guiTop + 5)
                .size(55, 20)
                .build();
        addDrawableChild(loreTab);

        // Lore subcategories
        for (int i = 0; i < loreDisplayNames.length; i++) {
            int index = i;
            ButtonWidget sub = ButtonWidget.builder(Text.literal(loreDisplayNames[i]), btn -> switchLoreCategory(index))
                    .position(guiLeft + 5, guiTop + 35 + (i * 25))
                    .size(80, 20)
                    .build();
            subButtons.add(sub);
        }
        switchTab(selectedTab);
    }

    private void switchTab(int index) {
        selectedTab = index;

        if (selectedTab == -1) {
            for (ButtonWidget sub : subButtons) {
                if (!children().contains(sub)) {
                    addDrawableChild(sub);
                }
            }
        } else {
            subButtons.forEach(this::remove);
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
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        context.drawTexture(
                RenderLayer::getGuiTextured,
                BACKGROUND_TEXTURE,
                guiLeft, guiTop,
                0f, 0f,
                backgroundWidth, backgroundHeight,
                336, 256
        );
        context.drawBorder(guiLeft, guiTop, backgroundWidth, backgroundHeight, 0xFF00FF00);

        if (selectedTab == -1) {
            renderLoreTab(context, mouseX, mouseY, delta);
        } else {
            renderRecipeTab(context, mouseX, mouseY, delta, selectedTab);
        }

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

    private void loadLoreFromFile() {
        ResourceManager resourceManager = MinecraftClient.getInstance().getResourceManager();
        try {
            Optional<Resource> optional = resourceManager.getResource(LORE_JSON);
            if (optional.isEmpty()) return;

            try (InputStreamReader reader = new InputStreamReader(optional.get().getInputStream(), StandardCharsets.UTF_8)) {
                JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
                JsonObject lore = root.getAsJsonObject("lore");

                for (Map.Entry<String, JsonElement> entry : lore.entrySet()) {
                    List<String> lines = new ArrayList<>();
                    for (JsonElement line : entry.getValue().getAsJsonArray()) {
                        lines.add(line.getAsString());
                    }
                    loreMap.put(entry.getKey(), lines);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Failed to load lore.json", e);
        }
    }
}
