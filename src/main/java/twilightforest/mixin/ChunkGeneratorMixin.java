package twilightforest.mixin;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.jetbrains.annotations.Nullable;
import twilightforest.ASMHooks;

@Mixin(ChunkGenerator.class)
public abstract class ChunkGeneratorMixin {

	@Inject(method = "findNearestMapStructure", at = @At("RETURN"), cancellable = true)
	private void injectFindNearestNonRandomSpread(ServerLevel level,
			HolderSet<Structure> structures, BlockPos pos,
			int searchRadius, boolean skipKnownStructures,
			CallbackInfoReturnable<@Nullable Pair<BlockPos, Holder<Structure>>> cir) {
		Pair<BlockPos, Holder<Structure>> resolved =
			ASMHooks.resolveNearestNonRandomSpreadMapStructure(
				cir.getReturnValue(), level, structures, pos, searchRadius, skipKnownStructures);
		if (resolved != cir.getReturnValue()) {
			cir.setReturnValue(resolved);
		}
	}
}
