package twilightforest.compat.neoforge.common;

import net.minecraft.world.item.ItemStack;

public class ItemAbilities {

	public static final ItemAbility FIRESTARTER_LIGHT = ItemAbility.get("firestarter_light");
	public static final ItemAbility SHEARS_HARVEST = ItemAbility.get("shears_harvest");
	public static final ItemAbility SHEARS_DIG = ItemAbility.get("shears_dig");
	public static final ItemAbility AXE_WAX_OFF = ItemAbility.get("axe_wax_off");
	public static final ItemAbility AXE_STRIP = ItemAbility.get("axe_strip");
	public static final ItemAbility SHOVEL_DIG = ItemAbility.get("shovel_dig");
	public static final ItemAbility SHIELD_BLOCK = ItemAbility.get("shield_block");

	public static boolean canPerformAction(ItemStack stack, ItemAbility ability) {
		return false;
	}
}
