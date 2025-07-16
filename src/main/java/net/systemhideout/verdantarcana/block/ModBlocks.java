package net.systemhideout.verdantarcana.block;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.systemhideout.verdantarcana.VerdantArcanaMod;
import net.systemhideout.verdantarcana.block.custom.LavenderCropBlock;
import net.systemhideout.verdantarcana.block.custom.MandrakeCropBlock;
import net.systemhideout.verdantarcana.block.custom.MugwortCropBlock;
import net.systemhideout.verdantarcana.block.custom.SageCropBlock;
import net.systemhideout.verdantarcana.block.custom.WitchAltarBlock;

import java.util.function.Function;

public class ModBlocks {

    public static final Block MOONSTONE_BLOCK = registerBlock("moonstone_block",
            properties -> new Block(properties.strength(4f)
                    .requiresTool().sounds(BlockSoundGroup.AMETHYST_BLOCK)));
    public static final Block RAW_MOONSTONE_BLOCK = registerBlock("raw_moonstone_block",
            properties -> new Block(properties.strength(3f)
                    .requiresTool()));

    public static final Block MOONSTONE_ORE = registerBlock("moonstone_ore",
            properties -> new ExperienceDroppingBlock(UniformIntProvider.create(2, 5),
                    properties.strength(3f).requiresTool()));
    public static final Block MOONSTONE_DEEPSLATE_ORE = registerBlock("moonstone_deepslate_ore",
            properties -> new ExperienceDroppingBlock(UniformIntProvider.create(3, 6),
                    properties.strength(4f).requiresTool().sounds(BlockSoundGroup.DEEPSLATE)));

    public static final Block LAVENDER_CROP = registerBlockWithoutBlockItem("lavender_crop",
            properties -> new LavenderCropBlock(properties.noCollision()
                    .ticksRandomly().breakInstantly().sounds(BlockSoundGroup.CROP)
                    .pistonBehavior(PistonBehavior.DESTROY)));

    public static final Block SAGE_CROP = registerBlockWithoutBlockItem("sage_crop",
            properties -> new SageCropBlock(properties.noCollision()
                    .ticksRandomly().breakInstantly().sounds(BlockSoundGroup.CROP)
                    .pistonBehavior(PistonBehavior.DESTROY)));

    public static final Block MUGWORT_CROP = registerBlockWithoutBlockItem("mugwort_crop",
            properties -> new MugwortCropBlock(properties.noCollision()
                    .ticksRandomly().breakInstantly().sounds(BlockSoundGroup.CROP)
                    .pistonBehavior(PistonBehavior.DESTROY)));

    public static final Block MANDRAKE_CROP = registerBlockWithoutBlockItem("mandrake_crop",
            properties -> new MandrakeCropBlock(properties.noCollision()
                    .ticksRandomly().breakInstantly().sounds(BlockSoundGroup.CROP)
                    .pistonBehavior(PistonBehavior.DESTROY)));

    public static final Block WITCH_ALTAR = registerNonOpaqueBlock("witch_altar",  settings ->
            new WitchAltarBlock(settings
                    .strength(5.0f, 1200.0f)
                    .sounds(BlockSoundGroup.STONE))

    );



    private static Block registerBlock(String name, Function<AbstractBlock.Settings, Block> function) {
        Block toRegister = function.apply(AbstractBlock.Settings.create().registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(VerdantArcanaMod.MOD_ID, name))));
        registerBlockItem(name, toRegister);
        return Registry.register(Registries.BLOCK, Identifier.of(VerdantArcanaMod.MOD_ID, name), toRegister);
    }

    private static Block registerNonOpaqueBlock(String name, Function<AbstractBlock.Settings, Block> function) {
        Block toRegister = function.apply(AbstractBlock.Settings.create().nonOpaque()
                .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(VerdantArcanaMod.MOD_ID, name)))
        );
        registerBlockItem(name, toRegister);
        return Registry.register(Registries.BLOCK, Identifier.of(VerdantArcanaMod.MOD_ID, name), toRegister);
    }


    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(VerdantArcanaMod.MOD_ID, name),
                new BlockItem(block, new Item.Settings().useBlockPrefixedTranslationKey()
                        .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(VerdantArcanaMod.MOD_ID, name)))));
    }

    private static Block registerBlockWithoutBlockItem(String name, Function<AbstractBlock.Settings, Block> function) {
        return Registry.register(Registries.BLOCK, Identifier.of(VerdantArcanaMod.MOD_ID, name),
                function.apply(AbstractBlock.Settings.create().registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(VerdantArcanaMod.MOD_ID, name)))));
    }

    public static void registerModBlocks() {
        VerdantArcanaMod.LOGGER.info("Registering Mod Blocks for " + VerdantArcanaMod.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> {
            entries.add(ModBlocks.MOONSTONE_BLOCK);
            entries.add(ModBlocks.RAW_MOONSTONE_BLOCK);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> {
            entries.add(ModBlocks.WITCH_ALTAR);
        });
    }
}
