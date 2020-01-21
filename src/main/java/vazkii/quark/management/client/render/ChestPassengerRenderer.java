package vazkii.quark.management.client.render;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import vazkii.quark.management.entity.ChestPassengerEntity;

/**
 * @author WireSegal
 * Created at 2:02 PM on 9/3/19.
 */
public class ChestPassengerRenderer extends EntityRenderer<ChestPassengerEntity> {

    public ChestPassengerRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }
    
    @Override
    	public void render(ChestPassengerEntity entity, float yaw, float partialTicks, MatrixStack matrix, IRenderTypeBuffer buffer, int light) {
        if(!entity.isPassenger())
            return;

        Entity riding = entity.getRidingEntity();
        if (riding == null)
            return;

        BoatEntity boat = (BoatEntity) riding;
        Vec3d pos = entity.getPositionVec();
//        double entityYaw = MathHelper.lerp(partialTicks, boat.prevRotationYaw, boat.rotationYaw);
//        
//        double dX = MathHelper.lerp(partialTicks, entity.lastTickPosX, pos.x);
//        double dY = MathHelper.lerp(partialTicks, entity.lastTickPosY, pos.y);
//        double dZ = MathHelper.lerp(partialTicks, entity.lastTickPosZ, pos.z);
        
//        double renderX = dX - x; TODO fix this later
//        double renderY = dY - y;
//        double renderZ = dZ - z;
//        x = MathHelper.lerp(partialTicks, boat.lastTickPosX, boat.posX) - renderX;
//        y = MathHelper.lerp(partialTicks, boat.lastTickPosY, boat.posY) - renderY;
//        z = MathHelper.lerp(partialTicks, boat.lastTickPosZ, boat.posZ) - renderZ;

        super.render(entity, yaw, partialTicks, matrix, buffer, light);
        
        float rot = 180F - yaw;

        ItemStack stack = entity.getChestType();

        matrix.push();
        matrix.translate(0, 0.375, 0);
        matrix.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(rot));
        float timeSinceHit = boat.getTimeSinceHit() - partialTicks;
        float damageTaken = boat.getDamageTaken() - partialTicks;

        if (damageTaken < 0.0F)
            damageTaken = 0.0F;

        if (timeSinceHit > 0.0F) {
        	double angle = MathHelper.sin(timeSinceHit) * timeSinceHit * damageTaken / 10.0F * boat.getForwardDirection();
            matrix.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion((float) angle));
        }

        float rock = boat.getRockingAngle(partialTicks);
        if (!MathHelper.epsilonEquals(rock, 0.0F)) {
        	 matrix.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(rock));
        }

        if (riding.getControllingPassenger() == null) {
            if (riding.getPassengers().size() > 1)
            	matrix.translate(0F, 0F, -0.9F);
            else
            	matrix.translate(0F, 0F, -0.45F);
        }

        matrix.translate(0F, 0.7F - 0.375F, 0.6F - 0.15F);

        matrix.scale(1.75F, 1.75F, 1.75F);

        Minecraft.getInstance().getItemRenderer().renderItem(stack, TransformType.FIXED, light, OverlayTexture.DEFAULT_UV, matrix, buffer);
        matrix.pop();
    }

    @Override
    public ResourceLocation getEntityTexture(@Nonnull ChestPassengerEntity entity) {
        return null;
    }

}
