package net.systemhideout.verdantarcana.herbs;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;

import java.util.HashSet;
import java.util.Set;

public class HerbDiscoveryComponent {

    private final Set<String> discoveredHerbs = new HashSet<>();

    public HerbDiscoveryComponent() {}

    public HerbDiscoveryComponent(Set<String> herbs) {
        this.discoveredHerbs.addAll(herbs);
    }

    public void discover(String herbId) {
        discoveredHerbs.add(herbId);
    }

    public boolean isDiscovered(String herbId) {
        return discoveredHerbs.contains(herbId);
    }

    public Set<String> getDiscoveredHerbs() {
        return discoveredHerbs;
    }

    public NbtCompound writeNbt() {
        NbtCompound nbt = new NbtCompound();
        NbtList list = new NbtList();
        for (String herb : discoveredHerbs) {
            list.add(NbtString.of(herb));
        }
        nbt.put("DiscoveredHerbs", list);
        return nbt;
    }

    public void readNbt(NbtCompound nbt) {
        discoveredHerbs.clear();
        if (nbt.contains("DiscoveredHerbs")) {
            NbtList list = (NbtList) nbt.get("DiscoveredHerbs");
            for (int i = 0; i < list.size(); i++) {
                list.getString(i).ifPresent(discoveredHerbs::add);
            }
        }
    }
}

