package net.systemhideout.verdantarcana.mixin;

import net.minecraft.nbt.NbtCompound;

public interface HerbDiscoveryComponentSaver {
    void writeHerbDiscoveryData(NbtCompound nbt);
    void readHerbDiscoveryData(NbtCompound nbt);
}

