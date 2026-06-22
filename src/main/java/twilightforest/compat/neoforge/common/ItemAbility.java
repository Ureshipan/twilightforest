package twilightforest.compat.neoforge.common;

import net.minecraft.world.item.ItemStack;

public record ItemAbility(String name) {

	public static ItemAbility get(String name) {
		return new ItemAbility(name);
	}

	public static boolean canPerform(ItemStack stack, ItemAbility ability) {
		return false;
	}
}
