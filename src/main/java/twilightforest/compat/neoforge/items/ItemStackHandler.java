package twilightforest.compat.neoforge.items;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

public class ItemStackHandler extends SimpleContainer {

	public ItemStackHandler(int size) {
		super(size);
	}

	public ItemStack getStackInSlot(int slot) {
		return this.getItem(slot);
	}

	public void setStackInSlot(int slot, ItemStack stack) {
		this.setItem(slot, stack);
	}

	public int getSlots() {
		return this.getContainerSize();
	}

	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		if (stack.isEmpty()) return ItemStack.EMPTY;
		ItemStack existing = this.getItem(slot);
		if (!existing.isEmpty()) return stack;
		if (!simulate) this.setItem(slot, stack.copy());
		return ItemStack.EMPTY;
	}

	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		ItemStack existing = this.getItem(slot);
		if (existing.isEmpty()) return ItemStack.EMPTY;
		int toExtract = Math.min(amount, existing.getCount());
		ItemStack result = existing.copyWithCount(toExtract);
		if (!simulate) {
			if (toExtract >= existing.getCount()) {
				this.setItem(slot, ItemStack.EMPTY);
			} else {
				existing.shrink(toExtract);
			}
		}
		return result;
	}

	public int getSlotLimit(int slot) {
		return 64;
	}

	public boolean isItemValid(int slot, ItemStack stack) {
		return true;
	}
}
