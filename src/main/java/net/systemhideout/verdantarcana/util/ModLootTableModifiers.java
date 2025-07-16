package net.systemhideout.verdantarcana.util;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.systemhideout.verdantarcana.item.ModItems;

public class ModLootTableModifiers {
    private static final RegistryKey<LootTable> GRASS_LOOT_TABLE =
            RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of("minecraft", "blocks/short_grass"));

    public static void modifyLootTables() {
        LootTableEvents.MODIFY.register((id, builder, source, registries) -> {
            if (source.isBuiltin() && id.equals(GRASS_LOOT_TABLE)) {
                LootPool.Builder pool = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.2f))
                        .with(ItemEntry.builder(ModItems.LAVENDER_SEEDS));

                builder.pool(pool.build());
            }
        });
    }
}
