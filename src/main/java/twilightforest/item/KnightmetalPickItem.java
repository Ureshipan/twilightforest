package twilightforest.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class KnightmetalPickItem extends PickaxeItem {

	public KnightmetalPickItem(ToolMaterial material, Properties properties) {
		super(material, 1.0F, -2.8F, properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, net.minecraft.world.item.component.TooltipDisplay tfDisplay, java.util.function.Consumer<net.minecraft.network.chat.Component> tooltips, TooltipFlag flags) {
		super.appendHoverText(stack, context, tfDisplay, tooltips, flags);
		tooltips.accept(Component.translatable(this.getDescriptionId() + ".desc").withStyle(ChatFormatting.GRAY));
	}
}