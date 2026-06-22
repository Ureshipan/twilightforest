package twilightforest.compat.neoforge.capabilities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.Nullable;

public class BlockCapabilityCache<R, C> {
    @Nullable
    public static <R> BlockCapabilityCache<R, Direction> create(BlockCapability<R, Direction> capability, ServerLevel level, BlockPos pos, Direction direction) {
        return new BlockCapabilityCache<>();
    }

    @Nullable
    public R getCapability() {
        return null;
    }
}
