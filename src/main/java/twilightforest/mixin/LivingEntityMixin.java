package twilightforest.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.ASMHooks;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

	/**
	 * Applies TF shroud armor visibility reduction so shrouded armor makes the wearer harder
	 * to see (affects mob targeting range).
	 */
	@Inject(method = "getVisibilityPercent", at = @At("RETURN"), cancellable = true)
	private void injectArmorVisibility(Entity lookingEntity, CallbackInfoReturnable<Double> cir) {
		float modified = ASMHooks.modifyArmorVisibility(cir.getReturnValue().floatValue(),
			(LivingEntity) (Object) this);
		cir.setReturnValue((double) modified);
	}
}
