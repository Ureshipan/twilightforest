package twilightforest.mixin;

import net.minecraft.world.entity.PathfinderMob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.ASMHooks;

@Mixin(PathfinderMob.class)
public abstract class PathfinderMobMixin {

	/**
	 * Allows entities with LEASH_PATHFINDER_OVERRIDE attachment to ignore leash-holder proximity
	 * constraint (e.g. entities that need to path freely while leashed).
	 */
	@Inject(method = "shouldStayCloseToLeashHolder", at = @At("RETURN"), cancellable = true)
	private void injectLeashOverride(CallbackInfoReturnable<Boolean> cir) {
		boolean result = ASMHooks.overrideStayCloseToHolder(cir.getReturnValue(), (PathfinderMob) (Object) this);
		if (result != cir.getReturnValue()) {
			cir.setReturnValue(result);
		}
	}
}
