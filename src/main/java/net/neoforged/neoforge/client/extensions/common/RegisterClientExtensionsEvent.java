package net.neoforged.neoforge.client.extensions.common;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
public class RegisterClientExtensionsEvent {
    public void registerBlock(IClientBlockExtensions extensions, Block... blocks) {}
    public void registerItem(Object extensions, Item... items) {}
}