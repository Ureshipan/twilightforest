package twilightforest.mixin;

import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.status.ChunkStatusTasks;
import net.minecraft.world.level.levelgen.RandomState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import twilightforest.ASMHooks;

@Mixin(ChunkStatusTasks.class)
public abstract class ChunkStatusTasksMixin {

	/**
	 * After {@code ChunkGenerator.buildSurface()}, run TF chunk blanket processors.
	 * Using @Redirect to capture both ChunkAccess and WorldGenRegion from the callsite.
	 */
	@Redirect(
		method = "generateSurface",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/chunk/ChunkGenerator;buildSurface(Lnet/minecraft/server/level/WorldGenRegion;Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/world/level/levelgen/RandomState;Lnet/minecraft/world/level/chunk/ChunkAccess;)V"
		)
	)
	private static void redirectBuildSurface(ChunkGenerator generator, WorldGenRegion region,
			StructureManager structures, RandomState randomState, ChunkAccess chunk) {
		generator.buildSurface(region, structures, randomState, chunk);
		ASMHooks.chunkBlanketing(chunk, region);
	}
}
