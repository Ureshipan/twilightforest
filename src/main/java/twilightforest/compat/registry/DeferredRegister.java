package twilightforest.compat.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Fabric-backed drop-in replacement for NeoForge's DeferredRegister.
 *
 * Usage: create → call register() per entry → call {@link #initialize()} once from
 * the mod's onInitialize() (instead of IEventBus.register()).
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class DeferredRegister<T> {

	private final ResourceKey<Registry<T>> registryKey;
	private final String namespace;
	private final List<PendingEntry<T>> pending = new ArrayList<>();

	DeferredRegister(ResourceKey<Registry<T>> registryKey, String namespace) {
		this.registryKey = registryKey;
		this.namespace = namespace;
	}

	// ── Factory methods matching NeoForge API ──

	public static <T> DeferredRegister<T> create(ResourceKey<Registry<T>> registryKey, String namespace) {
		return new DeferredRegister<>(registryKey, namespace);
	}

	public static Blocks createBlocks(String namespace) {
		return new Blocks(namespace);
	}

	public static Items createItems(String namespace) {
		return new Items(namespace);
	}

	// ── Registration ──

	public <I extends T> DeferredHolder<T, I> register(String name, Supplier<I> supplier) {
		Identifier id = Identifier.fromNamespaceAndPath(namespace, name);
		ResourceKey<T> key = ResourceKey.create(registryKey, id);
		DeferredHolder<T, I> holder = new DeferredHolder<>(key);
		pending.add(new PendingEntry<>(id, (Supplier<T>) supplier, (DeferredHolder<T, T>) holder));
		return holder;
	}

	/**
	 * Performs all queued Registry.register() calls.
	 * Call once per register during mod onInitialize() in dependency order.
	 */
	public void initialize() {
		Registry<T> registry = (Registry<T>) BuiltInRegistries.REGISTRY.get(registryKey.location());
		if (registry == null) {
			throw new IllegalStateException("Registry not found: " + registryKey.location()
				+ " — use a custom initializer for non-built-in registries");
		}
		for (PendingEntry<T> entry : pending) {
			T value = Registry.register(registry, entry.id, entry.supplier.get());
			entry.holder.setValue(value);
		}
	}

	/** NeoForge IEventBus compatibility stub — registration happens in {@link #initialize()}. */
	public void register(Object eventBus) {}

	// ── NeoForge-style typed subclasses ──

	/** Typed register for Block — register() returns DeferredBlock. */
	public static class Blocks extends DeferredRegister<Block> {
		Blocks(String namespace) {
			super(Registries.BLOCK, namespace);
		}

		@Override
		@SuppressWarnings("unchecked")
		public <I extends Block> DeferredBlock<I> register(String name, Supplier<I> supplier) {
			Identifier id = Identifier.fromNamespaceAndPath(namespace(), name);
			ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, id);
			DeferredBlock<I> holder = new DeferredBlock<>(key);
			addPending(id, (Supplier<Block>) supplier, (DeferredHolder<Block, Block>) holder);
			return holder;
		}
	}

	/** Typed register for Item — register() returns DeferredItem. */
	public static class Items extends DeferredRegister<Item> {
		Items(String namespace) {
			super(Registries.ITEM, namespace);
		}

		@Override
		@SuppressWarnings("unchecked")
		public <I extends Item> DeferredItem<I> register(String name, Supplier<I> supplier) {
			Identifier id = Identifier.fromNamespaceAndPath(namespace(), name);
			ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, id);
			DeferredItem<I> holder = new DeferredItem<>(key);
			addPending(id, (Supplier<Item>) supplier, (DeferredHolder<Item, Item>) holder);
			return holder;
		}
	}

	// ── Internal helpers ──

	protected String namespace() {
		return namespace;
	}

	protected void addPending(Identifier id, Supplier<T> supplier, DeferredHolder<T, T> holder) {
		pending.add(new PendingEntry<>(id, supplier, holder));
	}

	private record PendingEntry<T>(
		Identifier id,
		Supplier<T> supplier,
		DeferredHolder<T, T> holder
	) {}
}
