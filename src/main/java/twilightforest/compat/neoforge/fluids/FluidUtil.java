package twilightforest.compat.neoforge.fluids;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.FluidState;

import java.util.Optional;

public class FluidUtil {

	public static Optional<FluidState> getFluidContained(ItemStack stack) {
		return Optional.empty();
	}

	public static FluidActionResult tryEmptyContainer(ItemStack container, Object fluidDest, int maxAmount,
													   Player player, boolean doFill) {
		return FluidActionResult.FAILURE;
	}

	public record FluidActionResult(ItemStack result, boolean success) {
		public static final FluidActionResult FAILURE = new FluidActionResult(ItemStack.EMPTY, false);

		public boolean isSuccess() {
			return success;
		}
	}
}
