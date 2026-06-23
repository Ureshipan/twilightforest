package twilightforest.compat.neoforge.common.world;

import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;

/**
 * Port compat for NeoForge's {@code net.neoforged.neoforge.common.world.PieceBeardifierModifier}.
 * <p>
 * Structure pieces implement this to drive terrain "beardifier" adaptation around them. On Fabric
 * the integration is provided by {@code twilightforest.mixin.BeardifierMixin}, which detects pieces
 * implementing this interface. Keeps the same method surface so the 22 TF structure pieces compile
 * unchanged.
 */
public interface PieceBeardifierModifier {

	BoundingBox getBeardifierBox();

	TerrainAdjustment getTerrainAdjustment();

	int getGroundLevelDelta();
}
