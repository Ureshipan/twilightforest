package twilightforest.compat.registry;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Fabric-backed drop-in replacement for NeoForge's DeferredBlock.
 * Adds Block-specific convenience methods delegating to get().
 */
public class DeferredBlock<T extends Block> extends DeferredHolder<Block, T> {

	DeferredBlock(ResourceKey<Block> key) {
		super(key);
	}

	public Item asItem() {
		return get().asItem();
	}

	public BlockState defaultBlockState() {
		return get().defaultBlockState();
	}
}
