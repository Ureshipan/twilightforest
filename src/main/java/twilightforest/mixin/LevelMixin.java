package twilightforest.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.ASMHooks;

@Mixin(Level.class)
public abstract class LevelMixin {

	@Inject(method = "isRainingAt", at = @At("RETURN"), cancellable = true)
	private void injectCloudBlockRain(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		boolean result = ASMHooks.isRainingAt(cir.getReturnValue(), (Level) (Object) this, pos);
		if (result != cir.getReturnValue()) {
			cir.setReturnValue(result);
		}
	}
}
