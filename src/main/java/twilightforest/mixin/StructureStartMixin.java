package twilightforest.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.ASMHooks;

@Mixin(StructureStart.class)
public abstract class StructureStartMixin {

	@Inject(method = "loadStaticStart", at = @At("RETURN"), cancellable = true)
	private static void injectLoadStaticStart(StructurePieceSerializationContext ctx,
			CompoundTag nbt, long seed,
			CallbackInfoReturnable<StructureStart> cir) {
		StructureStart result = cir.getReturnValue();
		if (result != null) {
			StructureStart modified = ASMHooks.loadStaticStart(result, result.getPieces(), nbt);
			if (modified != result) {
				cir.setReturnValue(modified);
			}
		}
	}
}
