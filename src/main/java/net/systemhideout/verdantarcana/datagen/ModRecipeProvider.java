package net.systemhideout.verdantarcana.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.systemhideout.verdantarcana.block.ModBlocks;
import net.systemhideout.verdantarcana.item.ModItems;

import java.util.List;
import java.util.concurrent.CompletableFuture;


public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup wrapperLookup, RecipeExporter exporter) {
        return new RecipeGenerator(wrapperLookup, exporter) {
            @Override
            public void generate() {
                List<ItemConvertible> MOONSTONE_SMELTABLES = List.of(
                        ModItems.RAW_MOONSTONE
                );

                offerSmelting(MOONSTONE_SMELTABLES, RecipeCategory.MISC, ModItems.MOONSTONE, 0.25f, 200, "moonstone");
                offerBlasting(MOONSTONE_SMELTABLES, RecipeCategory.MISC, ModItems.MOONSTONE, 0.25f, 100, "moonstone");

                offerReversibleCompactingRecipes(
                        RecipeCategory.BUILDING_BLOCKS,
                        ModItems.MOONSTONE,
                        RecipeCategory.DECORATIONS,
                        ModBlocks.MOONSTONE_BLOCK
                );

                createShaped(RecipeCategory.MISC, ModBlocks.RAW_MOONSTONE_BLOCK)
                        .pattern("RRR")
                        .pattern("RRR")
                        .pattern("RRR")
                        .input('R', ModItems.RAW_MOONSTONE)
                        .criterion(hasItem(ModItems.RAW_MOONSTONE), conditionsFromItem(ModItems.RAW_MOONSTONE))
                        .offerTo(exporter);

                createShapeless(RecipeCategory.MISC, ModItems.RAW_MOONSTONE, 9)
                        .input(ModBlocks.RAW_MOONSTONE_BLOCK)
                        .criterion(hasItem(ModBlocks.RAW_MOONSTONE_BLOCK), conditionsFromItem(ModBlocks.RAW_MOONSTONE_BLOCK))
                        .offerTo(exporter);

                createShapeless(RecipeCategory.MISC,ModItems.PINK_SALT, 1)
                        .input(Items.RED_SAND)
                        .input(Items.SUGAR)
                        .criterion(hasItem(Items.SUGAR), conditionsFromItem(Items.SUGAR))
                        .criterion(hasItem(Items.RED_SAND), conditionsFromItem(Items.RED_SAND))
                        .offerTo(exporter);

                generateMoonRecipes();

            }

            private void generateMoonRecipes() {
                createShaped(RecipeCategory.MISC, ModItems.BLANK_TOKEN)
                        .pattern(" M ")
                        .pattern(" B ")
                        .pattern("   ")
                        .input('M', ModItems.MOONSTONE)
                        .input('B', Items.IRON_NUGGET)
                        .criterion(hasItem(ModItems.MOONSTONE), conditionsFromItem(ModItems.MOONSTONE))
                        .offerTo(exporter, "blank_totem");

                createShapeless(RecipeCategory.MISC, ModItems.MOON_TOKEN_1)
                        .input(ModItems.BLANK_TOKEN)
                        .input(Items.INK_SAC)
                        .criterion(hasItem(ModItems.BLANK_TOKEN), conditionsFromItem(ModItems.BLANK_TOKEN))
                        .offerTo(exporter, "moon_totem_1");

                createShapeless(RecipeCategory.MISC, ModItems.MOON_TOKEN_2)
                        .input(ModItems.BLANK_TOKEN)
                        .input(Items.RED_MUSHROOM)
                        .criterion(hasItem(ModItems.BLANK_TOKEN), conditionsFromItem(ModItems.BLANK_TOKEN))
                        .offerTo(exporter, "moon_totem_2");

                createShapeless(RecipeCategory.MISC, ModItems.MOON_TOKEN_3)
                        .input(ModItems.BLANK_TOKEN)
                        .input(Items.GLOW_BERRIES)
                        .criterion(hasItem(ModItems.BLANK_TOKEN), conditionsFromItem(ModItems.BLANK_TOKEN))
                        .offerTo(exporter, "moon_totem_3");

                createShapeless(RecipeCategory.MISC, ModItems.MOON_TOKEN_4)
                        .input(ModItems.BLANK_TOKEN)
                        .input(Items.ENDER_EYE)
                        .criterion(hasItem(ModItems.BLANK_TOKEN), conditionsFromItem(ModItems.BLANK_TOKEN))
                        .offerTo(exporter, "moon_totem_4");
            }
        };
    }

    @Override
    public String getName() {
        return "VerdantArcana Recipes";
    }
}