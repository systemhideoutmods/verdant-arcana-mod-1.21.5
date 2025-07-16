package net.systemhideout.verdantarcana.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.RecipeBookCategories;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

public record WitchAltarRecipe(Ingredient inputItem0, Ingredient inputItem1, Ingredient inputItem2, Ingredient inputItem3, ItemStack output) implements Recipe<WitchAltarRecipeInput> {
    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> list = DefaultedList.of();
        list.add(this.inputItem0);
        list.add(this.inputItem1);
        list.add(this.inputItem2);
        list.add(this.inputItem3);
        return list;
    }

    @Override
    public boolean matches(WitchAltarRecipeInput input, World world) {
        if(world.isClient()) {
            return false;
        }

        return inputItem0.test(input.getStackInSlot(0))
                && inputItem1.test(input.getStackInSlot(1))
                && inputItem2.test(input.getStackInSlot(2))
                && inputItem3.test(input.getStackInSlot(3));
    }


    @Override
    public ItemStack craft(WitchAltarRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        return output.copy();
    }

    @Override
    public RecipeSerializer<? extends Recipe<WitchAltarRecipeInput>> getSerializer() {
        return ModRecipes.WITCH_ALTAR_SERIALIZER;
    }

    @Override
    public RecipeType<? extends Recipe<WitchAltarRecipeInput>> getType() {
        return ModRecipes.WITCH_ALTAR_TYPE;
    }

    @Override
    public IngredientPlacement getIngredientPlacement() {
        return IngredientPlacement.forMultipleSlots(List.of(
                Optional.ofNullable(inputItem0),
                Optional.ofNullable(inputItem1),
                Optional.ofNullable(inputItem2),
                Optional.ofNullable(inputItem3)
        ));
    }

    @Override
    public RecipeBookCategory getRecipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    public static class Serializer implements RecipeSerializer<WitchAltarRecipe> {
        public static final MapCodec<WitchAltarRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.CODEC.fieldOf("inputItem0").forGetter(WitchAltarRecipe::inputItem0),
                Ingredient.CODEC.fieldOf("inputItem1").forGetter(WitchAltarRecipe::inputItem1),
                Ingredient.CODEC.fieldOf("inputItem2").forGetter(WitchAltarRecipe::inputItem2),
                Ingredient.CODEC.fieldOf("inputItem3").forGetter(WitchAltarRecipe::inputItem3),
                ItemStack.CODEC.fieldOf("result").forGetter(WitchAltarRecipe::output)
        ).apply(inst, WitchAltarRecipe::new));

        public static final PacketCodec<RegistryByteBuf, WitchAltarRecipe> STREAM_CODEC =
                PacketCodec.tuple(
                        Ingredient.PACKET_CODEC, WitchAltarRecipe::inputItem0,
                        Ingredient.PACKET_CODEC, WitchAltarRecipe::inputItem1,
                        Ingredient.PACKET_CODEC, WitchAltarRecipe::inputItem2,
                        Ingredient.PACKET_CODEC, WitchAltarRecipe::inputItem3,
                        ItemStack.PACKET_CODEC, WitchAltarRecipe::output,
                        WitchAltarRecipe::new);

        @Override
        public MapCodec<WitchAltarRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, WitchAltarRecipe> packetCodec() {
            return STREAM_CODEC;
        }
    }
}
