package net.neoforged.neoforge.event.entity.player;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import java.util.List;
public class ItemTooltipEvent {
    private final ItemStack stack;
    private final List<Component> toolTip;
    public ItemTooltipEvent(ItemStack stack, List<Component> toolTip) { this.stack = stack; this.toolTip = toolTip; }
    public ItemStack getItemStack() { return stack; }
    public List<Component> getToolTip() { return toolTip; }
}