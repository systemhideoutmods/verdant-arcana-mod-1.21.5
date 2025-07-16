package net.systemhideout.verdantarcana.block.entity.client.model;

import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class WitchAltarBookModel {

    private final ModelPart root;
    private final ModelPart leftPage;
    private final ModelPart rightPage;
    private final ModelPart bookCover;

    public static final Identifier TEXTURE = Identifier.of("system_hideout", "textures/entity/witch_altar_book.png");
    public static final EntityModelLayer LAYER = new EntityModelLayer(Identifier.of("system_hideout", "witch_altar_book"), "main");

    public WitchAltarBookModel(ModelPart root) {
        this.root = root;
        this.bookCover = root.getChild("bookCover");
        this.leftPage = root.getChild("leftPage");
        this.rightPage = root.getChild("rightPage");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData root = modelData.getRoot();

        // Book Cover (full book base)
        root.addChild("bookCover",
                ModelPartBuilder.create()
                        .uv(0, 0) // Top-left of texture
                        .cuboid(-4.0F, 0.0F, -4.0F, 8, 1, 8),
                ModelTransform.of(0.0F, 0.0F, 0.0F, 0, 0, 0)
        );

        root.addChild("leftPage",
                ModelPartBuilder.create()
                        .uv(0, 16)
                        .cuboid(0.0F, -0.5F, -3.0F, 4, 1, 6),
                ModelTransform.of(0.0F, 2.0F, 0.0F, 0, 0, 0) // ⬆️ Raise by 2.0F
        );

        root.addChild("rightPage",
                ModelPartBuilder.create()
                        .uv(24, 16)
                        .cuboid(-4.0F, -0.5F, -3.0F, 4, 1, 6),
                ModelTransform.of(0.0F, 2.0F, 0.0F, 0, 0, 0) // ⬆️ Raise by 2.0F
        );


        return TexturedModelData.of(modelData, 64, 32);
    }


    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, float tickDelta, float age) {
        float time = age + tickDelta; // Blend for smoother animation
        float floatHeight = MathHelper.sin(time * 0.01f) * 0.02f;
        float rotation = (time * 0.02f) % (2 * (float)Math.PI);

        matrices.push();
        matrices.translate(0.0, floatHeight, 0.0);
        matrices.scale(0.85f, 0.85f, 0.85f); // slightly smaller
        //leftPage.yaw = MathHelper.sin(time * 0.15f) * 0.2f;
        //rightPage.yaw = -MathHelper.sin(time * 0.15f) * 0.2f;
        this.root.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutout(TEXTURE)), light, overlay);
        matrices.pop();
    }

    public ModelPart getRoot() {
        return root;
    }
}
