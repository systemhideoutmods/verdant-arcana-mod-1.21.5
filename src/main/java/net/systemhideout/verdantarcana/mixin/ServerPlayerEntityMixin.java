package net.systemhideout.verdantarcana.mixin;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.systemhideout.verdantarcana.herbs.HerbDiscoveryComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
    @Unique
    private final HerbDiscoveryComponent herbComponent = new HerbDiscoveryComponent();

    @Inject(method = "readCustomDataFromNbt", at = @At("RETURN"))
    private void loadHerbData(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.get("herb_discovery") instanceof NbtCompound herbNbt) {
            herbComponent.readNbt(herbNbt);
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("RETURN"))
    private void saveHerbData(NbtCompound nbt, CallbackInfo ci) {
        nbt.put("herb_discovery", herbComponent.writeNbt());
    }

    @Unique
    public HerbDiscoveryComponent getHerbDiscoveryComponent() {
        return herbComponent;
    }
}

