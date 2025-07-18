package net.systemhideout.verdantarcana;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.systemhideout.verdantarcana.item.ModItems;
import net.systemhideout.verdantarcana.network.ModNetworking;
import net.systemhideout.verdantarcana.screen.custom.WitchAltarScreen;
import net.systemhideout.verdantarcana.block.entity.client.WitchAltarBlockEntityRenderer;
import net.minecraft.client.render.RenderLayer;
import net.systemhideout.verdantarcana.block.ModBlocks;
import net.systemhideout.verdantarcana.block.entity.ModBlockEntities;
import net.systemhideout.verdantarcana.block.entity.client.model.WitchAltarBookModel;
import net.systemhideout.verdantarcana.item.custom.CrescentLexiconItem;
import net.systemhideout.verdantarcana.screen.ModScreenHandlers;
import net.systemhideout.verdantarcana.screen.custom.RecipeBookScreen;


public class Client implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModNetworking.registerClient();

        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(),
                ModBlocks.LAVENDER_CROP,
                ModBlocks.SAGE_CROP,
                ModBlocks.MUGWORT_CROP,
                ModBlocks.MANDRAKE_CROP,
                ModBlocks.WITCH_ALTAR
        );

        EntityModelLayerRegistry.registerModelLayer(
                WitchAltarBookModel.LAYER,
                WitchAltarBookModel::getTexturedModelData
        );

        BlockEntityRendererRegistry.register(
                ModBlockEntities.WITCH_ALTAR,
                WitchAltarBlockEntityRenderer::new
        );

        CrescentLexiconItem.onClientUse = (player) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client != null && client.mouse != null) {
                client.execute(() -> client.setScreen(new RecipeBookScreen()));
            }
        };

        HandledScreens.register(ModScreenHandlers.WITCH_ALTAR_SCREEN_HANDLER, WitchAltarScreen::new);

        ItemTooltipCallback.EVENT.register((itemStack, context, type, lines) -> {
            if (itemStack.isOf(ModItems.PHIAL_OF_HOLLOW_SIGHT)) {
                lines.add(Text.translatable("item.verdant-arcana-mod.phial_of_hollow_sight.effect.0").formatted(Formatting.BLUE));
                lines.add(Text.translatable("item.verdant-arcana-mod.phial_of_hollow_sight.effect.1").formatted(Formatting.BLUE));
                lines.add(Text.translatable("item.verdant-arcana-mod.phial_of_hollow_sight.effect.2").formatted(Formatting.BLUE));
                lines.add(Text.translatable("item.verdant-arcana-mod.phial_of_hollow_sight.effect.3").formatted(Formatting.BLUE));
                lines.add(Text.translatable("item.verdant-arcana-mod.phial_of_hollow_sight.flavor").formatted(Formatting.DARK_PURPLE, Formatting.ITALIC));
            }
        });
    }

}