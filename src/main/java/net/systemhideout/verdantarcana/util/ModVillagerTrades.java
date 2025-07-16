package net.systemhideout.verdantarcana.util;

import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.village.VillagerProfession;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradedItem;
import net.systemhideout.verdantarcana.item.ModItems;

public class ModVillagerTrades {
    public static void registerTrades() {
        // Level 1 Farmer trades
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.FARMER, 1, factories -> {
            factories.add((entity, random) -> new TradeOffer(
                    new TradedItem(ModItems.LAVENDER, 8),
                    new ItemStack(Items.EMERALD, 1), 7, 2, 0.04f));

            factories.add((entity, random) -> new TradeOffer(
                    new TradedItem(ModItems.SAGE, 8),
                    new ItemStack(Items.EMERALD, 1), 7, 2, 0.04f));

            factories.add((entity, random) -> new TradeOffer(
                    new TradedItem(ModItems.MUGWORT, 8),
                    new ItemStack(Items.EMERALD, 1), 7, 2, 0.04f));

            factories.add((entity, random) -> new TradeOffer(
                    new TradedItem(ModItems.MANDRAKE, 8),
                    new ItemStack(Items.EMERALD, 1), 7, 2, 0.04f));
        });

        TradeOfferHelper.registerWanderingTraderOffers(factories -> {
            factories.addAll(Identifier.of("verdantarcana", "blank_token_trade"), (entity, random) -> {
                if (random.nextFloat() < 0.05f) { // 5% chance to appear
                    return new TradeOffer(
                            new TradedItem(Items.EMERALD, 20),
                            new ItemStack(ModItems.BLANK_TOKEN, 1),1,15,0.2f);
                }
                return null; // No trade added most of the time
            });
        });
    }
}
