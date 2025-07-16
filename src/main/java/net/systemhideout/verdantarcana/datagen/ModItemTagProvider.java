package net.systemhideout.verdantarcana.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.systemhideout.verdantarcana.item.ModItems;
import net.systemhideout.verdantarcana.util.ModTags;


import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {

        // Custom tag for crops
        getOrCreateTagBuilder(ModTags.Items.WITCHY_CROPS)
                .add(ModItems.LAVENDER)
                .add(ModItems.SAGE)
                .add(ModItems.MUGWORT)
                .add(ModItems.MANDRAKE);

        // Custom tag for witchy food
        getOrCreateTagBuilder(ModTags.Items.WITCHY_FOOD)
        ;

    }
}
