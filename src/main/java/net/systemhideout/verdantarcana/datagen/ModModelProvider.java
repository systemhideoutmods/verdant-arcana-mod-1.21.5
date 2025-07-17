package net.systemhideout.verdantarcana.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.*;
import net.systemhideout.verdantarcana.block.ModBlocks;
import net.systemhideout.verdantarcana.block.custom.LavenderCropBlock;
import net.systemhideout.verdantarcana.block.custom.MandrakeCropBlock;
import net.systemhideout.verdantarcana.block.custom.MugwortCropBlock;
import net.systemhideout.verdantarcana.block.custom.SageCropBlock;
import net.systemhideout.verdantarcana.item.ModItems;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        // Standard blocks
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.MOONSTONE_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.RAW_MOONSTONE_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.MOONSTONE_ORE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.MOONSTONE_DEEPSLATE_ORE);

        // Crops
        blockStateModelGenerator.registerCrop(ModBlocks.LAVENDER_CROP, LavenderCropBlock.AGE, 0, 1, 2, 3, 4, 5, 6, 7);
        blockStateModelGenerator.registerCrop(ModBlocks.SAGE_CROP, SageCropBlock.AGE, 0, 1, 2, 3, 4, 5, 6, 7);
        blockStateModelGenerator.registerCrop(ModBlocks.MUGWORT_CROP, MugwortCropBlock.AGE, 0, 1, 2, 3, 4, 5, 6, 7);
        blockStateModelGenerator.registerCrop(ModBlocks.MANDRAKE_CROP, MandrakeCropBlock.AGE, 0, 1, 2, 3, 4, 5, 6, 7);


    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        // Ore
        itemModelGenerator.register(ModItems.MOONSTONE, Models.GENERATED);
        itemModelGenerator.register(ModItems.RAW_MOONSTONE, Models.GENERATED);

        // Herbs
        itemModelGenerator.register(ModItems.LAVENDER, Models.GENERATED);
        itemModelGenerator.register(ModItems.SAGE, Models.GENERATED);
        itemModelGenerator.register(ModItems.MUGWORT, Models.GENERATED);
        itemModelGenerator.register(ModItems.MANDRAKE, Models.GENERATED);

        // Witchy Items
        itemModelGenerator.register(ModItems.CRESCENT_LEXICON, Models.GENERATED);
        itemModelGenerator.register(ModItems.BLANK_TOKEN, Models.GENERATED);
        itemModelGenerator.register(ModItems.MOON_TOKEN_1, Models.GENERATED);
        itemModelGenerator.register(ModItems.MOON_TOKEN_2, Models.GENERATED);
        itemModelGenerator.register(ModItems.MOON_TOKEN_3, Models.GENERATED);
        itemModelGenerator.register(ModItems.MOON_TOKEN_4, Models.GENERATED);

        //Block Items
        itemModelGenerator.register(ModBlocks.WITCH_ALTAR.asItem(), Models.GENERATED);

    }
}
