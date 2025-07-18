package net.systemhideout.verdantarcana;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.systemhideout.verdantarcana.block.ModBlocks;
import net.systemhideout.verdantarcana.block.entity.ModBlockEntities;
import net.systemhideout.verdantarcana.item.ModItemGroups;
import net.systemhideout.verdantarcana.item.ModItems;
import net.systemhideout.verdantarcana.recipe.ModRecipes;
import net.systemhideout.verdantarcana.screen.ModScreenHandlers;
import net.systemhideout.verdantarcana.util.ModLootTableModifiers;
import net.systemhideout.verdantarcana.util.ModVillagerTrades;
import net.systemhideout.verdantarcana.world.gen.ModWorldGeneration;
import net.systemhideout.verdantarcana.network.ModNetworking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VerdantArcanaMod implements ModInitializer {
	public static final String MOD_ID = "verdant-arcana-mod";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);


	@Override
	public void onInitialize() {
		ModNetworking.register();
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