package net.neoforged.neoforge.client.event;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
public class RegisterColorHandlersEvent {
    public static class Block extends RegisterColorHandlersEvent {
        private final BlockColors blockColors;
        public Block(BlockColors blockColors) { this.blockColors = blockColors; }
        public BlockColors getBlockColors() { return blockColors; }
        public void register(BlockColor color, net.minecraft.world.level.block.Block... blocks) {}
    }
    public static class ItemTintSources extends RegisterColorHandlersEvent {
        public void register(Object factory, net.minecraft.world.item.Item... items) {}
        public void register(Identifier id, Object factory) {}
    }
}