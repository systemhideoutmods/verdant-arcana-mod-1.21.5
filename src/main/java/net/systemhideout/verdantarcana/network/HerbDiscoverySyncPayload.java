package net.systemhideout.verdantarcana.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.List;

public record HerbDiscoverySyncPayload(List<String> discoveredHerbs) implements CustomPayload {

    public static final Id<HerbDiscoverySyncPayload> ID =
            new CustomPayload.Id<>(Identifier.of("verdant-arcana-mod", "herb_discovery_sync"));

    public static final PacketCodec<PacketByteBuf, HerbDiscoverySyncPayload> CODEC =
            PacketCodec.of(HerbDiscoverySyncPayload::write, HerbDiscoverySyncPayload::read);

    public static HerbDiscoverySyncPayload read(PacketByteBuf buf) {
        List<String> herbs = buf.readList(b -> b.readString(Short.MAX_VALUE));
        return new HerbDiscoverySyncPayload(herbs);
    }

    public void write(PacketByteBuf buf) {
        buf.writeCollection(discoveredHerbs, PacketByteBuf::writeString);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}