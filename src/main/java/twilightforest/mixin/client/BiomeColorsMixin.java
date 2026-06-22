package twilightforest.mixin.client;

import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.ASMHooks;

@Mixin(BiomeColors.class)
public abstract class BiomeColorsMixin {

	/**
	 * Intercepts the FOLIAGE_COLOR_RESOLVER lambda (static lambda$static$0) to apply
	 * TF biome-specific foliage colors (enchanted forest, dark forest, etc.).
	 */
	@Inject(
		method = "lambda$static$0(Lnet/minecraft/world/level/biome/Biome;DD)I",
		at = @At("RETURN"),
		cancellable = true,
		remap = false
	)
	private static void injectFoliageColor(Biome biome, double x, double z,
			CallbackInfoReturnable<Integer> cir) {
		int modified = ASMHooks.resolveFoliageColor(cir.getReturnValue(), biome, x, z);
		if (modified != cir.getReturnValue()) {
			cir.setReturnValue(modified);
		}
	}
}
