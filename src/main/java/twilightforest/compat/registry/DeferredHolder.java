package twilightforest.compat.registry;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Fabric-backed drop-in replacement for NeoForge's DeferredHolder.
 * <p>
 * Implements {@link Holder} by delegating to the underlying {@link Holder.Reference}
 * obtained from the registry during {@link DeferredRegister#initialize()}, so that
 * instances can be used anywhere a {@code Holder<R>} is expected.
 */
public class DeferredHolder<R, T extends R> implements Holder<R>, Supplier<T> {

	private final ResourceKey<R> key;
	private T value;
	private Holder.Reference<R> holder;

	DeferredHolder(ResourceKey<R> key) {
		this.key = key;
	}

	@Override
	public T get() {
		return value;
	}

	@Override
	@SuppressWarnings("unchecked")
	public T value() {
		return value != null ? value : (T) holder.value();
	}

	public ResourceKey<R> getKey() {
		return key;
	}

	public Identifier getId() {
		return key.identifier();
	}

	/** Convenience matching NeoForge: a stack of this entry (works for blocks and items via ItemLike). */
	public net.minecraft.world.item.ItemStack toStack() {
		return new net.minecraft.world.item.ItemStack((net.minecraft.world.level.ItemLike) value());
	}

	public net.minecraft.world.item.ItemStack toStack(int count) {
		return new net.minecraft.world.item.ItemStack((net.minecraft.world.level.ItemLike) value(), count);
	}

	void setValue(T value) {
		this.value = value;
	}

	void setHolder(Holder.Reference<R> holder) {
		this.holder = holder;
	}

	private Holder<R> delegate() {
		return holder;
	}

	// ── Holder<R> delegation ──

	@Override
	public boolean isBound() {
		return holder != null && holder.isBound();
	}

	@Override
	public boolean is(Identifier id) {
		return delegate().is(id);
	}

	@Override
	public boolean is(ResourceKey<R> key) {
		return this.key.equals(key);
	}

	@Override
	public boolean is(Predicate<ResourceKey<R>> predicate) {
		return delegate().is(predicate);
	}

	@Override
	public boolean is(TagKey<R> tag) {
		return delegate().is(tag);
	}

	@Override
	public boolean is(Holder<R> holder) {
		return delegate().is(holder);
	}

	@Override
	public Stream<TagKey<R>> tags() {
		return delegate().tags();
	}

	@Override
	public Either<ResourceKey<R>, R> unwrap() {
		return Either.left(key);
	}

	@Override
	public Optional<ResourceKey<R>> unwrapKey() {
		return Optional.of(key);
	}

	@Override
	public Holder.Kind kind() {
		return Holder.Kind.REFERENCE;
	}

	@Override
	public boolean canSerializeIn(HolderOwner<R> owner) {
		return delegate().canSerializeIn(owner);
	}

	@Override
	public String toString() {
		return "DeferredHolder[" + key.identifier() + "]";
	}
}
