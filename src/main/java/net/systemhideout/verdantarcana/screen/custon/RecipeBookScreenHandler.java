package net.systemhideout.verdantarcana.screen.custon;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.item.ItemStack;
import net.systemhideout.verdantarcana.screen.ModScreenHandlers;

public class RecipeBookScreenHandler extends ScreenHandler {

    public RecipeBookScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(ModScreenHandlers.RECIPE_BOOK_SCREEN_HANDLER, syncId);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true; // No interaction needed; view-only
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        return ItemStack.EMPTY;
    }
}
