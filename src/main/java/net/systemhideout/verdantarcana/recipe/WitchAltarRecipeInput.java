package net.systemhideout.verdantarcana.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.input.RecipeInput;
import net.minecraft.util.collection.DefaultedList;

public record WitchAltarRecipeInput(DefaultedList<ItemStack> inputs) implements RecipeInput {

    @Override
    public ItemStack getStackInSlot(int slot) {
        return inputs.get(slot);
    }

    @Override
    public int size() {
        return inputs.size();
    }

    public DefaultedList<ItemStack> getStacks() {
        return inputs;
    }

}
