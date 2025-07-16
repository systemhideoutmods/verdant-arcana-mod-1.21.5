package net.systemhideout.verdantarcana.util;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.systemhideout.verdantarcana.VerdantArcanaMod;

public class ModTags {

    public static class Items {
        // Witchy brewing tag additions
        public static final TagKey<Item> WITCHY_CROPS = createTag("witchy_crops");
        public static final TagKey<Item> WITCHY_SEEDS = createTag("witchy_seeds");
        public static final TagKey<Item> WITCHY_FOOD = createTag("witchy_food");

        private static TagKey<Item> createTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, Identifier.of(VerdantArcanaMod.MOD_ID, name));
        }
    }
}
