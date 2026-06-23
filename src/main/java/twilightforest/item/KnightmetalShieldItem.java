package twilightforest.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.TooltipFlag;
import twilightforest.compat.neoforge.common.ItemAbilities;
import twilightforest.compat.neoforge.common.ItemAbility;

import java.util.List;

public class KnightmetalShieldItem extends ShieldItem {

	public KnightmetalShieldItem(Properties properties) {
		super(properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, net.minecraft.world.item.component.TooltipDisplay tfDisplay, java.util.function.Consumer<net.minecraft.network.chat.Component> tooltip, TooltipFlag flag) {
	}
}