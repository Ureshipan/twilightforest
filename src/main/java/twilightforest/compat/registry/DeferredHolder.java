package twilightforest.compat.registry;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;

import java.util.function.Supplier;

/**
 * Fabric-backed drop-in replacement for NeoForge's DeferredHolder.
 * Value is populated by DeferredRegister.initialize() during mod init.
 */
public class DeferredHolder<R, T extends R> implements Supplier<T> {

	private final ResourceKey<R> key;
	private T value;

	DeferredHolder(ResourceKey<R> key) {
		this.key = key;
	}

	@Override
	public T get() {
		return value;
	}

	public T value() {
		return value;
	}

	public ResourceKey<R> getKey() {
		return key;
	}

	public Identifier getId() {
		return key.location();
	}

	void setValue(T value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "DeferredHolder[" + key.location() + "]";
	}
}
