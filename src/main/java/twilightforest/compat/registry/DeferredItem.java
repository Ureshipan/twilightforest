package twilightforest.compat.registry;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Fabric-backed drop-in replacement for NeoForge's DeferredItem.
 * Adds Item-specific convenience methods delegating to get().
 */
public class DeferredItem<T extends Item> extends DeferredHolder<Item, T> {

	DeferredItem(ResourceKey<Item> key) {
		super(key);
	}

	public ItemStack toStack() {
		return get().getDefaultInstance();
	}

	public ItemStack toStack(int count) {
		return new ItemStack(get(), count);
	}
}
