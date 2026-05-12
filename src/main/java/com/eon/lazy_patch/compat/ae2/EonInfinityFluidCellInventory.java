package com.eon.lazy_patch.compat.ae2;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.KeyCounter;
import appeng.api.storage.cells.CellState;
import appeng.api.storage.cells.ICellHandler;
import appeng.api.storage.cells.ISaveProvider;
import appeng.api.storage.cells.StorageCell;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class EonInfinityFluidCellInventory implements StorageCell {
    public static final ICellHandler HANDLER = new Handler();

    private final ItemStack stack;
    private final AEKey record;

    public EonInfinityFluidCellInventory(ItemStack stack) {
        if (!(stack.getItem() instanceof EonInfinityFluidCellItem cellItem)) {
            throw new IllegalArgumentException("Cell is not an Eon infinity fluid cell.");
        }

        this.stack = stack;
        this.record = cellItem.getRecord();
    }

    @Override
    public CellState getStatus() {
        return CellState.NOT_EMPTY;
    }

    @Override
    public double getIdleDrain() {
        return 1.0;
    }

    @Override
    public long insert(AEKey what, long amount, Actionable mode, IActionSource source) {
        return this.record.equals(what) ? amount : 0;
    }

    @Override
    public long extract(AEKey what, long amount, Actionable mode, IActionSource source) {
        return this.record.equals(what) ? amount : 0;
    }

    @Override
    public void persist() {
    }

    @Override
    public Component getDescription() {
        return this.stack.getHoverName();
    }

    @Override
    public void getAvailableStacks(KeyCounter out) {
        out.add(this.record, EonInfinityFluidCellItem.getDisplayedAmount(this.record));
    }

    @Override
    public boolean isPreferredStorageFor(AEKey what, IActionSource source) {
        return this.record.equals(what);
    }

    private static class Handler implements ICellHandler {
        @Override
        public boolean isCell(ItemStack stack) {
            return stack != null && stack.getItem() instanceof EonInfinityFluidCellItem;
        }

        @Override
        public StorageCell getCellInventory(ItemStack stack, ISaveProvider host) {
            return this.isCell(stack) ? new EonInfinityFluidCellInventory(stack) : null;
        }
    }
}
