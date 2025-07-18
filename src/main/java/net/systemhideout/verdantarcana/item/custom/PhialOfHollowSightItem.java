package net.systemhideout.verdantarcana.item.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.UseAction;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;


public class PhialOfHollowSightItem extends Item {

    public PhialOfHollowSightItem(Settings settings) {
        super(settings);
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 32;
    }


    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!world.isClient && user instanceof PlayerEntity player) {
            // Apply status effects
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 15 * 20));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 90 * 20));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 4 * 20));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 10 * 20));

            player.playSound(SoundEvents.ITEM_HONEY_BOTTLE_DRINK.value(), 1.0F, 1.0F);

            // Consume item
            if (!player.getAbilities().creativeMode) {
                stack.decrement(1);
            }
        }

        return super.finishUsing(stack, world, user);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        user.setCurrentHand(hand);

        return ActionResult.SUCCESS; // <-- this must be SUCCESS, not CONSUME
    }

}


