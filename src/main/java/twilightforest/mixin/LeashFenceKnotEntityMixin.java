package twilightforest.mixin;

import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.ASMHooks;

@Mixin(LeashFenceKnotEntity.class)
public abstract class LeashFenceKnotEntityMixin {

	@Inject(method = "survives", at = @At("RETURN"), cancellable = true)
	private void injectWroughtIronFenceSurvives(CallbackInfoReturnable<Boolean> cir) {
		boolean result = ASMHooks.leashFenceKnotSurvives(cir.getReturnValue(), (LeashFenceKnotEntity) (Object) this);
		if (result != cir.getReturnValue()) {
			cir.setReturnValue(result);
		}
	}
}
