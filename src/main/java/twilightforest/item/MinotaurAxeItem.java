package twilightforest.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;

import java.util.List;

public class MinotaurAxeItem extends Item {

	public MinotaurAxeItem(ToolMaterial material, Properties properties) {
		super(properties.axe(material, 6.0F, -3.2F));
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, net.minecraft.world.item.component.TooltipDisplay tfDisplay, java.util.function.Consumer<net.minecraft.network.chat.Component> tooltip, TooltipFlag flags) {
		super.appendHoverText(stack, context, tfDisplay, tooltip, flags);
		tooltip.accept(Component.translatable("item.twilightforest.minotaur_axe.desc").withStyle(ChatFormatting.GRAY));
	}
}