package twilightforest.compat.neoforge.common.world;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LightLayer;

public interface AuxiliaryLightManager {

	void setLightAt(BlockPos pos, int light);

	void removeLightAt(BlockPos pos);

	int getLightAt(BlockPos pos);
}
