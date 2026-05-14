package com.eon.lazy_patch.menu;

import com.eon.lazy_patch.block.ModBlocks;
import com.eon.lazy_patch.block.entity.ExperienceInfuserBlockEntity;
import com.eon.lazy_patch.item.custom.ExperienceCrystalItem;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ExperienceInfuserMenu extends AbstractContainerMenu {
    public static final int CRYSTAL_SLOT = 0;
    private static final int MACHINE_SLOT_COUNT = 1;
    private static final int PLAYER_INVENTORY_START = MACHINE_SLOT_COUNT;
    private static final int PLAYER_INVENTORY_END = PLAYER_INVENTORY_START + 27;
    private static final int PLAYER_HOTBAR_END = PLAYER_INVENTORY_END + 9;

    private final ContainerLevelAccess access;
    private final ContainerData data;

    public ExperienceInfuserMenu(int containerId, Inventory playerInventory, RegistryFriendlyByteBuf extraData) {
        this(containerId, playerInventory, getItemHandler(playerInventory, extraData), new SimpleContainerData(3), ContainerLevelAccess.NULL);
    }

    public ExperienceInfuserMenu(int containerId, Inventory playerInventory, ExperienceInfuserBlockEntity blockEntity, ContainerData data) {
        this(containerId, playerInventory, blockEntity.getItemHandler(), data, ContainerLevelAccess.create(
                Objects.requireNonNull(blockEntity.getLevel()),
                blockEntity.getBlockPos()
        ));
    }

    private ExperienceInfuserMenu(
            int containerId,
            Inventory playerInventory,
            ItemStackHandler itemHandler,
            ContainerData data,
            ContainerLevelAccess access
    ) {
        super(ModMenuTypes.EXPERIENCE_INFUSER.get(), containerId);
        this.access = access;
        this.data = data;

        addSlot(new SlotItemHandler(itemHandler, CRYSTAL_SLOT, 80, 35));
        addPlayerInventory(playerInventory);
        addDataSlots(data);
    }

    public int getFluidAmount() {
        return data.get(0);
    }

    public int getFluidCapacity() {
        return data.get(1);
    }

    public int getFluidId() {
        return data.get(2);
    }

    public ItemStack getCrystalStack() {
        return getSlot(CRYSTAL_SLOT).getItem();
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
        } else if (stack.getItem() instanceof ExperienceCrystalItem) {
            if (!moveItemStackTo(stack, CRYSTAL_SLOT, CRYSTAL_SLOT + 1, false)) {
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
        return stillValid(access, player, ModBlocks.EXPERIENCE_INFUSER.get());
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
        if (playerInventory.player.level().getBlockEntity(extraData.readBlockPos()) instanceof ExperienceInfuserBlockEntity infuser) {
            return infuser.getItemHandler();
        }
        return new ItemStackHandler(1);
    }
}
