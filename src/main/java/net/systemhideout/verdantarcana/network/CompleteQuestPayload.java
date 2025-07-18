package net.systemhideout.verdantarcana.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record CompleteQuestPayload() implements CustomPayload {
    public static final Id<CompleteQuestPayload> ID = new Id<>(Identifier.of("verdant-arcana", "complete_quest"));

    public static final PacketCodec<RegistryByteBuf, CompleteQuestPayload> CODEC = new PacketCodec<>() {
        @Override
        public CompleteQuestPayload decode(RegistryByteBuf buf) {
            return new CompleteQuestPayload(); // no data, just marker
        }

        @Override
        public void encode(RegistryByteBuf buf, CompleteQuestPayload payload) {
            // no-op: this payload carries no data
        }
    };

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}

