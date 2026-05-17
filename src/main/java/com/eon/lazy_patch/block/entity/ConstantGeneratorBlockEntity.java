package com.eon.lazy_patch.block.entity;

import com.eon.lazy_patch.menu.ConstantGeneratorMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConstantGeneratorBlockEntity extends BlockEntity implements MenuProvider {
    public static final int GENERATION_RATE = 256;
    public static final int OUTPUT_RATE = 1_024;
    public static final int ENERGY_CAPACITY = 1_000_000;
    private static final String ITEMS_KEY = "Items";
    private static final String ENERGY_KEY = "Energy";

    private final ItemStackHandler itemHandler = new ItemStackHandler(1) {
        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            IEnergyStorage energyStorage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
            return energyStorage != null && energyStorage.canReceive();
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

        @Override
        protected void onContentsChanged(int slot) {
            setChangedAndUpdate();
        }
    };

    private final BufferEnergyStorage energyStorage = new BufferEnergyStorage(ENERGY_CAPACITY, OUTPUT_RATE);

    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> energyStorage.getEnergyStored();
                case 1 -> energyStorage.getMaxEnergyStored();
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    public ConstantGeneratorBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.CONSTANT_GENERATOR.get(), pos, blockState);
    }

    @SuppressWarnings("unused")
    public static void serverTick(Level level, BlockPos pos, BlockState state, ConstantGeneratorBlockEntity blockEntity) {
        boolean changed = false;
        changed |= blockEntity.energyStorage.addEnergy(GENERATION_RATE) > 0;
        changed |= blockEntity.chargeStoredItem();
        if (changed) {
            blockEntity.setChangedAndUpdate();
        }
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    public IEnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    public void dropContents() {
        if (level == null) {
            return;
        }

        ItemStack stack = itemHandler.getStackInSlot(0);
        if (!stack.isEmpty()) {
            Block.popResource(level, worldPosition, stack);
            itemHandler.setStackInSlot(0, ItemStack.EMPTY);
        }
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.eon_lazy_patch.constant_generator");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, @NotNull Inventory playerInventory, @NotNull Player player) {
        return new ConstantGeneratorMenu(containerId, playerInventory, this, data);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put(ITEMS_KEY, itemHandler.serializeNBT(registries));
        tag.put(ENERGY_KEY, energyStorage.serializeNBT(registries));
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        itemHandler.deserializeNBT(registries, tag.getCompound(ITEMS_KEY));
        var energyTag = tag.get(ENERGY_KEY);
        if (energyTag != null) {
            energyStorage.deserializeNBT(registries, energyTag);
        }
    }

    private boolean chargeStoredItem() {
        ItemStack stack = itemHandler.getStackInSlot(0);
        if (stack.isEmpty() || energyStorage.getEnergyStored() <= 0) {
            return false;
        }

        IEnergyStorage itemEnergy = stack.getCapability(Capabilities.EnergyStorage.ITEM);
        if (itemEnergy == null || !itemEnergy.canReceive()) {
            return false;
        }

        int transfer = Math.min(OUTPUT_RATE, energyStorage.getEnergyStored());
        transfer = Math.min(transfer, itemEnergy.getMaxEnergyStored() - itemEnergy.getEnergyStored());
        if (transfer <= 0) {
            return false;
        }

        int received = itemEnergy.receiveEnergy(transfer, false);
        if (received <= 0) {
            return false;
        }

        energyStorage.extractEnergy(received, false);
        return true;
    }

    private void setChangedAndUpdate() {
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    private static class BufferEnergyStorage extends EnergyStorage {
        public BufferEnergyStorage(int capacity, int maxExtract) {
            super(capacity, 0, maxExtract);
        }

        public int addEnergy(int amount) {
            if (amount <= 0) {
                return 0;
            }

            int accepted = Math.max(0, Math.min(amount, this.capacity - this.energy));
            if (accepted > 0) {
                this.energy += accepted;
            }
            return accepted;
        }
    }
}
