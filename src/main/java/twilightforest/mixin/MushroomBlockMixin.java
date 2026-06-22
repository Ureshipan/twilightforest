package twilightforest.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.MushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.ASMHooks;

@Mixin(MushroomBlock.class)
public abstract class MushroomBlockMixin {

	/**
	 * Allows mushrooms to grow on TF Twilight Portal block — matching the NeoForge logic
	 * that used {@code canSustainPlant(TriState)} on surrounding blocks.
	 */
	@Inject(method = "canSurvive", at = @At("RETURN"), cancellable = true)
	private void injectMushroomPortalSoil(BlockState state, LevelReader level,
			BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		if (!cir.getReturnValue() && ASMHooks.mushroomCanSurviveOnPortal(level, pos)) {
			cir.setReturnValue(true);
		}
	}
}
