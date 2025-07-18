package net.systemhideout.verdantarcana.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.systemhideout.verdantarcana.VerdantArcanaMod;
import net.systemhideout.verdantarcana.block.ModBlocks;
import net.systemhideout.verdantarcana.item.custom.CrescentLexiconItem;
import net.systemhideout.verdantarcana.item.custom.PhialOfHollowSightItem;

import java.util.function.Function;

public class ModItems {

    // Food
    public static final Item PHIAL_OF_HOLLOW_SIGHT = registerItem("phial_of_hollow_sight",
            settings -> new PhialOfHollowSightItem(settings
                    .maxCount(16)
                    .rarity(Rarity.UNCOMMON)
                    .fireproof()
            )
    );

    //public static final Item CHEESE = registerItem("cheese",
    //        settings -> new Item(settings.food(ModFoodComponents.CHEESE)));

    // Ingredients
    public static final Item PINK_SALT = registerItem("pink_salt", Item::new);

    // Gems
    public static final Item MOONSTONE = registerItem("moonstone", Item::new);
    public static final Item RAW_MOONSTONE = registerItem("raw_moonstone", Item::new);


    // Crops & Seeds
    public static final Item LAVENDER = registerItem("lavender", Item::new);
    public static final Item LAVENDER_SEEDS = registerItem("lavender_seeds", settings -> new BlockItem(ModBlocks.LAVENDER_CROP, settings));

    public static final Item SAGE = registerItem("sage", Item::new);
    public static final Item SAGE_SEEDS = registerItem("sage_seeds", settings -> new BlockItem(ModBlocks.SAGE_CROP, settings));

    public static final Item MUGWORT = registerItem("mugwort", Item::new);
    public static final Item MUGWORT_SEEDS = registerItem("mugwort_seeds", settings -> new BlockItem(ModBlocks.MUGWORT_CROP, settings));

    public static final Item MANDRAKE = registerItem("mandrake", Item::new);
    public static final Item MANDRAKE_SEEDS = registerItem("mandrake_seeds", settings -> new BlockItem(ModBlocks.MANDRAKE_CROP, settings));

    // Totems
    public static final Item BLANK_TOKEN = registerItem("blank_token", Item::new);
    public static final Item MOON_TOKEN_1 = registerItem("moon_token_1", Item::new);
    public static final Item MOON_TOKEN_2 = registerItem("moon_token_2", Item::new);
    public static final Item MOON_TOKEN_3 = registerItem("moon_token_3", Item::new);
    public static final Item MOON_TOKEN_4 = registerItem("moon_token_4", Item::new);

    public static final Item CRESCENT_LEXICON = registerItem("crescent_lexicon",
            settings -> new CrescentLexiconItem(settings.maxCount(1)));

    private static Item registerItem(String name, Function<Item.Settings, Item> factory) {
        Identifier id = Identifier.of(VerdantArcanaMod.MOD_ID, name);
        Item.Settings settings = new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, id));
        return Registry.register(Registries.ITEM, id, factory.apply(settings));
    }

    private static Item registerItemBook(String name, Function<Item.Settings, Item> factory) {
        Identifier id = Identifier.of(VerdantArcanaMod.MOD_ID, name);
        return Registry.register(Registries.ITEM, id, factory.apply(new Item.Settings()));
    }

    public static void registerModItems() {
        VerdantArcanaMod.LOGGER.info("Registering Mod Items for " + VerdantArcanaMod.MOD_ID);

        // FOOD
        //ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries -> {
        //    entries.add(CHEESE);
        //});

        // INGREDIENTS
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(MOONSTONE);
            entries.add(RAW_MOONSTONE);
            entries.add(LAVENDER);
            entries.add(SAGE);
            entries.add(MUGWORT);
            entries.add(MANDRAKE);
            entries.add(PINK_SALT);
        });

        // NATURAL
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> {
            entries.add(LAVENDER_SEEDS);
            entries.add(SAGE_SEEDS);
            entries.add(MUGWORT_SEEDS);
            entries.add(MANDRAKE_SEEDS);
        });
    }
}
