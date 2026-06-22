package twilightforest.compat.registry;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;

/**
 * Fabric-backed drop-in replacement for NeoForge's deferred data-component holder.
 * <p>
 * Implements {@link DataComponentType} by delegating to the registered value, so the
 * holder itself can be passed straight to {@code ItemStack.get(...)} / {@code getOrDefault(...)}
 * / {@code has(...)} exactly like NeoForge allows.
 */
public class DeferredComponent<T> extends DeferredHolder<DataComponentType<?>, DataComponentType<T>>
	implements DataComponentType<T> {

	DeferredComponent(ResourceKey<DataComponentType<?>> key) {
		super(key);
	}

	@Override
	public Codec<T> codec() {
		return get().codec();
	}

	@Override
	public StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() {
		return get().streamCodec();
	}

	@Override
	public boolean ignoreSwapAnimation() {
		return get().ignoreSwapAnimation();
	}
}
