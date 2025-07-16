package net.systemhideout.verdantarcana.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LeafEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.systemhideout.verdantarcana.block.ModBlocks;
import net.systemhideout.verdantarcana.block.custom.LavenderCropBlock;
import net.systemhideout.verdantarcana.block.custom.MandrakeCropBlock;
import net.systemhideout.verdantarcana.block.custom.MugwortCropBlock;
import net.systemhideout.verdantarcana.block.custom.SageCropBlock;
import net.systemhideout.verdantarcana.item.ModItems;

import java.util.concurrent.CompletableFuture;

public class ModLootTableProvider extends FabricBlockLootTableProvider {
    public ModLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        RegistryWrapper.Impl<Enchantment> impl = this.registries.getOrThrow(RegistryKeys.ENCHANTMENT);

        addDrop(ModBlocks.MOONSTONE_BLOCK);
        addDrop(ModBlocks.RAW_MOONSTONE_BLOCK);


        BlockStatePropertyLootCondition.Builder builder2 = BlockStatePropertyLootCondition.builder(ModBlocks.LAVENDER_CROP)
                .properties(StatePredicate.Builder.create().exactMatch(LavenderCropBlock.AGE, LavenderCropBlock.MAX_AGE));
        this.addDrop(ModBlocks.LAVENDER_CROP, this.cropDrops(ModBlocks.LAVENDER_CROP, ModItems.LAVENDER, ModItems.LAVENDER_SEEDS, builder2));

        BlockStatePropertyLootCondition.Builder builder3 = BlockStatePropertyLootCondition.builder(ModBlocks.SAGE_CROP)
                .properties(StatePredicate.Builder.create().exactMatch(SageCropBlock.AGE, SageCropBlock.MAX_AGE));
        this.addDrop(ModBlocks.SAGE_CROP, this.cropDrops(ModBlocks.SAGE_CROP, ModItems.SAGE, ModItems.SAGE_SEEDS, builder3));

        BlockStatePropertyLootCondition.Builder builder4 = BlockStatePropertyLootCondition.builder(ModBlocks.MUGWORT_CROP)
                .properties(StatePredicate.Builder.create().exactMatch(MugwortCropBlock.AGE, MugwortCropBlock.MAX_AGE));
        this.addDrop(ModBlocks.MUGWORT_CROP, this.cropDrops(ModBlocks.MUGWORT_CROP, ModItems.MUGWORT, ModItems.MUGWORT_SEEDS, builder4));

        BlockStatePropertyLootCondition.Builder builder5 = BlockStatePropertyLootCondition.builder(ModBlocks.MANDRAKE_CROP)
                .properties(StatePredicate.Builder.create().exactMatch(MandrakeCropBlock.AGE, MandrakeCropBlock.MAX_AGE));
        this.addDrop(ModBlocks.MANDRAKE_CROP, this.cropDrops(ModBlocks.MANDRAKE_CROP, ModItems.MANDRAKE, ModItems.MANDRAKE_SEEDS, builder5));

        addDrop(ModBlocks.MOONSTONE_ORE, multipleOreDrops(ModBlocks.MOONSTONE_ORE, ModItems.RAW_MOONSTONE, 1, 3));
        addDrop(ModBlocks.MOONSTONE_DEEPSLATE_ORE, multipleOreDrops(ModBlocks.MOONSTONE_DEEPSLATE_ORE, ModItems.RAW_MOONSTONE, 2, 4));

        addDrop(ModBlocks.WITCH_ALTAR);

    }

    public LootTable.Builder multipleOreDrops(Block drop, Item item, float minDrops, float maxDrops) {
        RegistryWrapper.Impl<Enchantment> impl = this.registries.getOrThrow(RegistryKeys.ENCHANTMENT);
        return this.dropsWithSilkTouch(drop, this.applyExplosionDecay(drop, ((LeafEntry.Builder<?>)
                ItemEntry.builder(item).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(minDrops, maxDrops))))
                .apply(ApplyBonusLootFunction.oreDrops(impl.getOrThrow(Enchantments.FORTUNE)))));
    }

}
