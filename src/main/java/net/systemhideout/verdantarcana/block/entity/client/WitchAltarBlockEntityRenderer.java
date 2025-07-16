package net.systemhideout.verdantarcana.block.entity.client;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.systemhideout.verdantarcana.block.entity.client.model.WitchAltarBookModel;
import net.systemhideout.verdantarcana.block.entity.custom.WitchAltarBlockEntity;

public class WitchAltarBlockEntityRenderer implements BlockEntityRenderer<WitchAltarBlockEntity>{

    private final WitchAltarBookModel bookModel;

    public WitchAltarBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.bookModel = new WitchAltarBookModel(ctx.getLayerModelPart(WitchAltarBookModel.LAYER));
    }

    @Override
    public void render(WitchAltarBlockEntity entity, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay, Vec3d cameraOffset) {

        float time = entity.getWorld() != null ? entity.getWorld().getTime() + tickDelta : 0;

        matrices.push();

        // 1. Move to center of block for rotation pivot
        matrices.translate(0.5, 1.0, 0.5);

        // 2. Rotate around the center
        matrices.multiply(RotationAxis.POSITIVE_Y.rotation(time * 0.02f));

        // 3. Raise book up slightly after rotating (so it floats in place)
        matrices.translate(0.0, 0.25 + Math.sin(time * 0.01f) * 0.02f, 0.0);

        // 4. Scale book down a little
        matrices.scale(0.85f, 0.85f, 0.85f);

        // 5. Render the book
        bookModel.render(matrices, vertexConsumers, light, overlay, tickDelta, time);

        matrices.pop();
    }

    @Override
    public boolean rendersOutsideBoundingBox(WitchAltarBlockEntity blockEntity) {
        return true;
    }
}
