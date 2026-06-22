package twilightforest.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.init.TFDataComponents;

@Mixin(HumanoidArmorLayer.class)
public abstract class HumanoidArmorLayerMixin {

	/**
	 * Skip rendering when the piece has the EMPERORS_CLOTH component — it provides protection
	 * stats but is intentionally invisible.
	 */
	@Inject(method = "renderArmorPiece", at = @At("HEAD"), cancellable = true)
	private void cancelEmperorsClothRendering(PoseStack poseStack, MultiBufferSource bufferSource,
			ItemStack stack, EquipmentSlot slot, int packedLight, HumanoidModel<?> model,
			CallbackInfo ci) {
		if (stack.has(TFDataComponents.EMPERORS_CLOTH)) {
			ci.cancel();
		}
	}
}
