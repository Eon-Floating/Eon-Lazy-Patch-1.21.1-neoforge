package com.eon.lazy_patch.menu;

import com.eon.lazy_patch.block.ModBlocks;
import com.eon.lazy_patch.block.entity.ConstantGeneratorBlockEntity;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ConstantGeneratorMenu extends AbstractContainerMenu {
    public static final int ENERGY_SLOT = 0;
    private static final int MACHINE_SLOT_COUNT = 1;
    private static final int PLAYER_INVENTORY_START = MACHINE_SLOT_COUNT;
    private static final int PLAYER_INVENTORY_END = PLAYER_INVENTORY_START + 27;
    private static final int PLAYER_HOTBAR_END = PLAYER_INVENTORY_END + 9;

    private final ContainerLevelAccess access;
    private final ContainerData data;

    public ConstantGeneratorMenu(int containerId, Inventory playerInventory, RegistryFriendlyByteBuf extraData) {
        this(containerId, playerInventory, getItemHandler(playerInventory, extraData), new SimpleContainerData(2), ContainerLevelAccess.NULL);
    }

    public ConstantGeneratorMenu(int containerId, Inventory playerInventory, ConstantGeneratorBlockEntity blockEntity, ContainerData data) {
        this(containerId, playerInventory, blockEntity.getItemHandler(), data, ContainerLevelAccess.create(
                Objects.requireNonNull(blockEntity.getLevel()),
                blockEntity.getBlockPos()
        ));
    }

    private ConstantGeneratorMenu(
            int containerId,
            Inventory playerInventory,
            ItemStackHandler itemHandler,
            ContainerData data,
            ContainerLevelAccess access
    ) {
        super(ModMenuTypes.CONSTANT_GENERATOR.get(), containerId);
        this.access = access;
        this.data = data;

        addSlot(new SlotItemHandler(itemHandler, ENERGY_SLOT, 129, 36));
        addPlayerInventory(playerInventory);
        addDataSlots(data);
    }

    public int getEnergyStored() {
        return data.get(0);
    }

    public int getEnergyCapacity() {
        return data.get(1);
    }

    public int getGenerationRate() {
        return ConstantGeneratorBlockEntity.GENERATION_RATE;
    }

    public int getOutputRate() {
        return ConstantGeneratorBlockEntity.OUTPUT_RATE;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (!slot.hasItem()) {
            return result;
        }

        ItemStack stack = slot.getItem();
        result = stack.copy();

        if (index < MACHINE_SLOT_COUNT) {
            if (!moveItemStackTo(stack, PLAYER_INVENTORY_START, PLAYER_HOTBAR_END, true)) {
                return ItemStack.EMPTY;
            }
        } else if (isChargeable(stack)) {
            if (!moveItemStackTo(stack, ENERGY_SLOT, ENERGY_SLOT + 1, false)) {
                return ItemStack.EMPTY;
            }
        } else if (index < PLAYER_INVENTORY_END) {
            if (!moveItemStackTo(stack, PLAYER_INVENTORY_END, PLAYER_HOTBAR_END, false)) {
                return ItemStack.EMPTY;
            }
        } else if (!moveItemStackTo(stack, PLAYER_INVENTORY_START, PLAYER_INVENTORY_END, false)) {
            return ItemStack.EMPTY;
        }

        if (stack.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        return result;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(access, player, ModBlocks.CONSTANT_GENERATOR.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(playerInventory, column + row * 9 + 9, 8 + column * 18, 84 + row * 18));
            }
        }

        for (int column = 0; column < 9; column++) {
            addSlot(new Slot(playerInventory, column, 8 + column * 18, 142));
        }
    }

    @SuppressWarnings("resource")
    private static ItemStackHandler getItemHandler(Inventory playerInventory, RegistryFriendlyByteBuf extraData) {
        if (extraData == null) {
            return new ItemStackHandler(1);
        }
        if (playerInventory.player.level().getBlockEntity(extraData.readBlockPos()) instanceof ConstantGeneratorBlockEntity generator) {
            return generator.getItemHandler();
        }
        return new ItemStackHandler(1);
    }

    private static boolean isChargeable(ItemStack stack) {
        IEnergyStorage energyStorage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
        return energyStorage != null && energyStorage.canReceive();
    }
}
