package twilightforest.compat.neoforge.common.extensions;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BaseSpawner;

public interface IOwnedSpawner {

	BaseSpawner getSpawner();

	BlockPos getPos();
}
