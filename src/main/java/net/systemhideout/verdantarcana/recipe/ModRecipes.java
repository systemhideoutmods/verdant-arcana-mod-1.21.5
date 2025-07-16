package net.systemhideout.verdantarcana.recipe;

import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.systemhideout.verdantarcana.VerdantArcanaMod;

public class ModRecipes {
    public static final RecipeSerializer<WitchAltarRecipe> WITCH_ALTAR_SERIALIZER = Registry.register(
            Registries.RECIPE_SERIALIZER, Identifier.of(VerdantArcanaMod.MOD_ID, "witch_altar"),
            new WitchAltarRecipe.Serializer());
    public static final RecipeType<WitchAltarRecipe> WITCH_ALTAR_TYPE = Registry.register(
            Registries.RECIPE_TYPE, Identifier.of(VerdantArcanaMod.MOD_ID, "witch_altar"), new RecipeType<WitchAltarRecipe>() {
                @Override
                public String toString() {
                    return "witch_altar";
                }
            });

    public static void registerRecipes() {
        VerdantArcanaMod.LOGGER.info("Registering Custom Recipes for " + VerdantArcanaMod.MOD_ID);
    }
}
