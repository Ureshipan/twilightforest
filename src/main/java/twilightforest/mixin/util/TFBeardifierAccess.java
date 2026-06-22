package twilightforest.mixin.util;

import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.world.level.levelgen.DensityFunction;

public interface TFBeardifierAccess {
	ObjectListIterator<DensityFunction> twilightforest_getCustomStructureDensities();
	void twilightforest_setCustomStructureDensities(ObjectListIterator<DensityFunction> iter);
}
