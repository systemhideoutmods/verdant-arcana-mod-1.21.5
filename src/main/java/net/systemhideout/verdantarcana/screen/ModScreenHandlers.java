package net.systemhideout.verdantarcana.screen;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.systemhideout.verdantarcana.VerdantArcanaMod;
import net.systemhideout.verdantarcana.screen.custom.RecipeBookScreenHandler;
import net.systemhideout.verdantarcana.screen.custom.WitchAltarScreenHandler;


public class ModScreenHandlers {

    // Optional constant for reuse/debugging
    public static final Identifier WITCH_ALTAR_SCREEN_HANDLER_ID =
            Identifier.of(VerdantArcanaMod.MOD_ID, "witch_altar_screen_handler");

    public static final ScreenHandlerType<WitchAltarScreenHandler> WITCH_ALTAR_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, WITCH_ALTAR_SCREEN_HANDLER_ID,
                    new ExtendedScreenHandlerType<>(WitchAltarScreenHandler::new, BlockPos.PACKET_CODEC));

    // New identifier and screen handler for the recipe book
    public static final Identifier RECIPE_BOOK_SCREEN_HANDLER_ID =
            Identifier.of(VerdantArcanaMod.MOD_ID, "recipe_book_screen_handler");

    public static final ScreenHandlerType<RecipeBookScreenHandler> RECIPE_BOOK_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, RECIPE_BOOK_SCREEN_HANDLER_ID,
                    new ScreenHandlerType<>(
                            (syncId, inventory) -> new RecipeBookScreenHandler(syncId, inventory),
                            FeatureFlags.VANILLA_FEATURES
                    )
            );


    public static void registerScreenHandlers() {
        VerdantArcanaMod.LOGGER.info("Registering Screen Handlers for " + VerdantArcanaMod.MOD_ID);
    }
}
