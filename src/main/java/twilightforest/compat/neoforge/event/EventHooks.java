package twilightforest.compat.neoforge.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.SpawnData;
import org.jetbrains.annotations.Nullable;

public class EventHooks {

	public static boolean checkSpawnPositionSpawner(Mob mob, ServerLevel level, EntitySpawnReason reason,
													SpawnData spawnData, Object spawner) {
		return true;
	}

	@Nullable
	public static SpawnGroupData finalizeMobSpawnSpawner(Mob mob, ServerLevel level, DifficultyInstance difficulty,
														EntitySpawnReason reason, @Nullable SpawnGroupData spawnData,
														@Nullable Object spawnDataNbt, Object spawner, boolean fromSpawner) {
		return mob.finalizeSpawn(level, difficulty, reason, spawnData);
	}

	@Nullable
	public static SpawnGroupData onSpecialSpawn(Mob mob, ServerLevel level, float x, float y, float z,
												@Nullable Object spawner, EntitySpawnReason reason) {
		return null;
	}

	public static boolean checkSpawnPosition(Mob mob, ServerLevel level, EntitySpawnReason reason) {
		return true;
	}
}
