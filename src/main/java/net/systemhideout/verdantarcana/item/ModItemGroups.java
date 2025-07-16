package net.systemhideout.verdantarcana.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.systemhideout.verdantarcana.VerdantArcanaMod;
import net.systemhideout.verdantarcana.block.ModBlocks;

public class ModItemGroups {
    public static final ItemGroup WITCH_ITEMS = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(VerdantArcanaMod.MOD_ID, "witch_items"),
            FabricItemGroup.builder().icon(() -> new ItemStack(Items.WRITTEN_BOOK))
                    .displayName(Text.translatable("itemgroup.witch_items"))
                    .entries((displayContext, entries) -> {
                        entries.add(ModItems.BLANK_TOKEN);
                        entries.add(ModItems.MOON_TOKEN_1);
                        entries.add(ModItems.MOON_TOKEN_2);
                        entries.add(ModItems.MOON_TOKEN_3);
                        entries.add(ModItems.MOON_TOKEN_4);
                        entries.add(ModItems.CRESENT_LEXICON);
                        // Add future charms, ingredients, and potion vials here
                        entries.add(ModBlocks.WITCH_ALTAR); // Optional: block added to group
                    }).build());

    public static void registerItemGroups() {
        VerdantArcanaMod.LOGGER.info("Registering Item Groups for " + VerdantArcanaMod.MOD_ID);
    }
}
