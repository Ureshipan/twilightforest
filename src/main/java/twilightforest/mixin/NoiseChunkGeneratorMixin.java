package twilightforest.mixin;

import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.ASMHooks;
import twilightforest.mixin.util.TFBeardifierAccess;

@Mixin(NoiseBasedChunkGenerator.class)
public abstract class NoiseChunkGeneratorMixin {

	@Inject(
		method = "createNoiseChunk",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/levelgen/Beardifier;forStructuresInChunk(Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/world/level/ChunkPos;)Lnet/minecraft/world/level/levelgen/Beardifier;",
			shift = At.Shift.AFTER
		)
	)
	private void injectCustomBeardifierField(ChunkAccess chunk, StructureManager structures,
			Blender blender, RandomState randomState,
			CallbackInfoReturnable<?> cir) {
		// The beardifier is on the stack here; we'll retrieve it via a local capture workaround.
		// The actual field injection happens through LocalCapture or via a separate @ModifyVariable.
		// Delegating to ASMHooks — Beardifier is cast to our interface in the @ModifyReturnValue below.
	}

	// Simpler approach: redirect forStructuresInChunk return to initialize the custom field
	@org.spongepowered.asm.mixin.injection.Redirect(
		method = "createNoiseChunk",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/levelgen/Beardifier;forStructuresInChunk(Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/world/level/ChunkPos;)Lnet/minecraft/world/level/levelgen/Beardifier;"
		)
	)
	private Beardifier redirectForStructuresInChunk(StructureManager structures,
			net.minecraft.world.level.ChunkPos chunkPos) {
		Beardifier beardifier = Beardifier.forStructuresInChunk(structures, chunkPos);
		if (beardifier instanceof TFBeardifierAccess access) {
			access.twilightforest_setCustomStructureDensities(ASMHooks.gatherCustomTerrain(structures, chunkPos));
		}
		return beardifier;
	}
}
