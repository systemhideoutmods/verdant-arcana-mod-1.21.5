package net.systemhideout.verdantarcana;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.systemhideout.verdantarcana.block.ModBlocks;
import net.systemhideout.verdantarcana.block.entity.ModBlockEntities;
import net.systemhideout.verdantarcana.herbs.HerbDiscoveryComponent;
import net.systemhideout.verdantarcana.item.ModItemGroups;
import net.systemhideout.verdantarcana.item.ModItems;
import net.systemhideout.verdantarcana.network.ModNetworking;
import net.systemhideout.verdantarcana.recipe.ModRecipes;
import net.systemhideout.verdantarcana.screen.ModScreenHandlers;
import net.systemhideout.verdantarcana.util.ModLootTableModifiers;
import net.systemhideout.verdantarcana.util.ModVillagerTrades;
import net.systemhideout.verdantarcana.world.gen.ModWorldGeneration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class VerdantArcanaMod implements ModInitializer {
	public static final String MOD_ID = "verdant-arcana-mod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final Map<UUID, HerbDiscoveryComponent> HERB_TRACKER = new HashMap<>();

	public static HerbDiscoveryComponent getHerbData(ServerPlayerEntity player) {
		return HERB_TRACKER.computeIfAbsent(player.getUuid(), uuid -> new HerbDiscoveryComponent());
	}

	@Environment(EnvType.CLIENT)
	public static HerbDiscoveryComponent CLIENT_HERB_DATA = new HerbDiscoveryComponent();


	@Override
	public void onInitialize() {
		ModNetworking.register();
		ModNetworking.registerEvents();
		ModItemGroups.registerItemGroups();
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModWorldGeneration.generateModWorldGen();
		ModBlockEntities.registerBlockEntities();
		ModScreenHandlers.registerScreenHandlers();
		ModRecipes.registerRecipes();
		ModLootTableModifiers.modifyLootTables();
		ModVillagerTrades.registerTrades();
		ModEvents.registerEvents();

		CompostingChanceRegistry.INSTANCE.add(ModItems.LAVENDER_SEEDS, 0.3f);
		CompostingChanceRegistry.INSTANCE.add(ModItems.LAVENDER, 0.5f);
		CompostingChanceRegistry.INSTANCE.add(ModItems.SAGE_SEEDS, 0.3f);
		CompostingChanceRegistry.INSTANCE.add(ModItems.SAGE, 0.5f);
		CompostingChanceRegistry.INSTANCE.add(ModItems.MUGWORT_SEEDS, 0.3f);
		CompostingChanceRegistry.INSTANCE.add(ModItems.MUGWORT, 0.5f);
		CompostingChanceRegistry.INSTANCE.add(ModItems.MANDRAKE_SEEDS, 0.3f);
		CompostingChanceRegistry.INSTANCE.add(ModItems.MANDRAKE, 0.5f);
	}
}
