package net.systemhideout.verdantarcana.block.entity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.systemhideout.verdantarcana.VerdantArcanaMod;
import net.systemhideout.verdantarcana.block.ModBlocks;
import net.systemhideout.verdantarcana.block.entity.custom.WitchAltarBlockEntity;

public class ModBlockEntities {

    public static final BlockEntityType<WitchAltarBlockEntity> WITCH_ALTAR =
            Registry.register(Registries.BLOCK_ENTITY_TYPE,
                    Identifier.of(VerdantArcanaMod.MOD_ID, "witch_altar"),
                    FabricBlockEntityTypeBuilder.create(WitchAltarBlockEntity::new, ModBlocks.WITCH_ALTAR).build());

    public static void registerBlockEntities() {
        VerdantArcanaMod.LOGGER.info("Registering Block Entities for " + VerdantArcanaMod.MOD_ID);
    }
}
