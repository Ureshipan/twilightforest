package twilightforest.mixin;

import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.DensityFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.ASMHooks;
import twilightforest.mixin.util.TFBeardifierAccess;

@Mixin(Beardifier.class)
public abstract class BeardifierMixin implements TFBeardifierAccess {

	@Unique
	private ObjectListIterator<DensityFunction> twilightforest_customStructureDensities;

	@Override
	public ObjectListIterator<DensityFunction> twilightforest_getCustomStructureDensities() {
		return twilightforest_customStructureDensities;
	}

	@Override
	public void twilightforest_setCustomStructureDensities(ObjectListIterator<DensityFunction> iter) {
		this.twilightforest_customStructureDensities = iter;
	}

	@Inject(method = "compute", at = @At("RETURN"), cancellable = true)
	private void injectCustomDensity(DensityFunction.FunctionContext context,
			CallbackInfoReturnable<Double> cir) {
		double modified = ASMHooks.getCustomDensity(cir.getReturnValue(), context, twilightforest_customStructureDensities);
		if (modified != cir.getReturnValue()) {
			cir.setReturnValue(modified);
		}
	}
}
