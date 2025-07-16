package net.systemhideout.verdantarcana.item.custom;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.function.Consumer;

public class CresentLexiconItem extends Item {
    public static Consumer<PlayerEntity> onClientUse = player -> {};

    public CresentLexiconItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient()) {
            onClientUse.accept(user);  // calls into client code ONLY if installed
        }

        return world.isClient ? ActionResult.SUCCESS : ActionResult.CONSUME;
    }
}
