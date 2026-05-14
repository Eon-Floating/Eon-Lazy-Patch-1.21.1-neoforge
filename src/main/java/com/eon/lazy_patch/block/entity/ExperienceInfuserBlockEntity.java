package com.eon.lazy_patch.block.entity;

import com.eon.lazy_patch.item.custom.ExperienceCrystalItem;
import com.eon.lazy_patch.menu.ExperienceInfuserMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
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
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExperienceInfuserBlockEntity extends BlockEntity implements MenuProvider {
    public static final int TANK_CAPACITY = 64_000;
    private static final String ITEMS_KEY = "Items";
    private static final String FLUID_KEY = "FluidTank";

    private final ItemStackHandler itemHandler = new ItemStackHandler(1) {
        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return stack.getItem() instanceof ExperienceCrystalItem;
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

    private final FluidTank fluidTank = new FluidTank(TANK_CAPACITY, fluid -> fluid.is(ExperienceCrystalItem.EXPERIENCE_FLUIDS)) {
        @Override
        protected void onContentsChanged() {
            setChangedAndUpdate();
        }
    };

    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> fluidTank.getFluidAmount();
                case 1 -> fluidTank.getCapacity();
                case 2 -> fluidTank.getFluid().isEmpty() ? -1 : BuiltInRegistries.FLUID.getId(fluidTank.getFluid().getFluid());
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
        }

        @Override
        public int getCount() {
            return 3;
        }
    };

    public ExperienceInfuserBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.EXPERIENCE_INFUSER.get(), pos, blockState);
    }

    @SuppressWarnings("unused")
    public static void serverTick(Level level, BlockPos pos, BlockState state, ExperienceInfuserBlockEntity blockEntity) {
        blockEntity.chargeCrystal();
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    public IFluidHandler getFluidHandler() {
        return fluidTank;
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
        return Component.translatable("block.eon_lazy_patch.experience_infuser");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, @NotNull Inventory playerInventory, @NotNull Player player) {
        return new ExperienceInfuserMenu(containerId, playerInventory, this, data);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put(ITEMS_KEY, itemHandler.serializeNBT(registries));
        tag.put(FLUID_KEY, fluidTank.writeToNBT(registries, new CompoundTag()));
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        itemHandler.deserializeNBT(registries, tag.getCompound(ITEMS_KEY));
        fluidTank.readFromNBT(registries, tag.getCompound(FLUID_KEY));
    }

    private void chargeCrystal() {
        ItemStack stack = itemHandler.getStackInSlot(0);
        int availableFluid = fluidTank.getFluidAmount();
        if (!(stack.getItem() instanceof ExperienceCrystalItem) || availableFluid < ExperienceCrystalItem.MILLIBUCKETS_PER_XP) {
            return;
        }

        int availableXp = availableFluid / ExperienceCrystalItem.MILLIBUCKETS_PER_XP;
        int movedXp = ExperienceCrystalItem.addStoredXp(stack, availableXp);
        if (movedXp <= 0) {
            return;
        }

        fluidTank.drain(movedXp * ExperienceCrystalItem.MILLIBUCKETS_PER_XP, IFluidHandler.FluidAction.EXECUTE);
        setChangedAndUpdate();
    }

    private void setChangedAndUpdate() {
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }
}
