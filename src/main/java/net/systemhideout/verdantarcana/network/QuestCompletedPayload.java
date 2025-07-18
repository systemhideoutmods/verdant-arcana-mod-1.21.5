package net.systemhideout.verdantarcana.network;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record QuestCompletedPayload() implements CustomPayload {
    public static final Id<QuestCompletedPayload> ID = new Id<>(Identifier.of("verdant-arcana", "quest_completed"));

    public static final PacketCodec<RegistryByteBuf, QuestCompletedPayload> CODEC = new PacketCodec<>() {
        @Override
        public QuestCompletedPayload decode(RegistryByteBuf buf) {
            return new QuestCompletedPayload();
        }

        @Override
        public void encode(RegistryByteBuf buf, QuestCompletedPayload payload) {
            // no data needed
        }
    };

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
