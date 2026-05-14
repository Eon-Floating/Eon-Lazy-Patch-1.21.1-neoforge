package com.eon.lazy_patch.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetExperiencePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ExperienceCrystalItem extends Item {
    private static final String STORED_XP_KEY = "StoredXp";
    public static final int MAX_STORED_XP = 1_000_000;
    private static final int MAX_LEVEL_SPAN = 30;
    public static final int MILLIBUCKETS_PER_XP = 20;
    public static final TagKey<Fluid> EXPERIENCE_FLUIDS = TagKey.create(
            Registries.FLUID,
            ResourceLocation.fromNamespaceAndPath("c", "experience")
    );

    public ExperienceCrystalItem() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(
            @NotNull Level level,
            @NotNull Player player,
            @NotNull InteractionHand usedHand
    ) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (level.isClientSide) {
            return InteractionResultHolder.sidedSuccess(stack, true);
        }

        int moved = player.isShiftKeyDown() ? storeExperience(stack, player) : withdrawExperience(stack, player);
        if (moved <= 0) {
            return InteractionResultHolder.fail(stack);
        }

        level.playSound(null, player.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.4f, 1.0f);
        return InteractionResultHolder.success(stack);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        Level level = context.getLevel();
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        ItemStack stack = context.getItemInHand();
        int moved = fillFromExperienceFluid(stack, context);
        if (moved <= 0) {
            return InteractionResult.FAIL;
        }

        level.playSound(null, context.getClickedPos(), SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 0.7f, 1.0f);
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(
            @NotNull ItemStack stack,
            @NotNull TooltipContext context,
            @NotNull List<Component> tooltip,
            @NotNull TooltipFlag flag
    ) {
        int storedXp = getStoredXp(stack);
        tooltip.add(Component.translatable("tooltip.eon_lazy_patch.experience_crystal.stored", storedXp, MAX_STORED_XP)
                .withStyle(ChatFormatting.GREEN));
        tooltip.add(Component.translatable("tooltip.eon_lazy_patch.experience_crystal.use")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("tooltip.eon_lazy_patch.experience_crystal.sneak_use")
                .withStyle(ChatFormatting.GRAY));
    }

    private static int storeExperience(ItemStack stack, Player player) {
        int storedXp = getStoredXp(stack);
        int freeSpace = MAX_STORED_XP - storedXp;
        int currentXp = getPlayerTotalXp(player);
        int targetXp = getXpAfterLosingLevels(player);
        int transferable = Math.min(Math.min(currentXp - targetXp, freeSpace), currentXp);
        if (transferable <= 0) {
            return 0;
        }

        setPlayerTotalXp(player, currentXp - transferable);
        setStoredXp(stack, storedXp + transferable);
        return transferable;
    }

    private static int withdrawExperience(ItemStack stack, Player player) {
        int storedXp = getStoredXp(stack);
        int currentXp = getPlayerTotalXp(player);
        int targetXp = getXpAfterGainingLevels(player);
        int transferLimit = targetXp - currentXp;
        int transferable = Math.min(storedXp, transferLimit);
        if (transferable <= 0) {
            return 0;
        }

        setPlayerTotalXp(player, currentXp + transferable);
        setStoredXp(stack, storedXp - transferable);
        return transferable;
    }

    private static int fillFromExperienceFluid(ItemStack stack, UseOnContext context) {
        int storedXp = getStoredXp(stack);
        int freeSpace = MAX_STORED_XP - storedXp;
        if (freeSpace <= 0) {
            return 0;
        }

        Level level = context.getLevel();
        Direction side = context.getClickedFace();
        BlockState blockState = level.getBlockState(context.getClickedPos());
        BlockEntity blockEntity = level.getBlockEntity(context.getClickedPos());
        IFluidHandler fluidHandler = level.getCapability(Capabilities.FluidHandler.BLOCK, context.getClickedPos(), blockState, blockEntity, side);
        if (fluidHandler == null) {
            return 0;
        }

        int maxFluidToDrain = freeSpace * MILLIBUCKETS_PER_XP;
        FluidStack simulatedDrain = drainExperienceFluid(fluidHandler, maxFluidToDrain, IFluidHandler.FluidAction.SIMULATE);
        int transferableXp = simulatedDrain.getAmount() / MILLIBUCKETS_PER_XP;
        if (transferableXp <= 0) {
            return 0;
        }

        int fluidToDrain = transferableXp * MILLIBUCKETS_PER_XP;
        FluidStack drained = drainExperienceFluid(fluidHandler, fluidToDrain, IFluidHandler.FluidAction.EXECUTE);
        int movedXp = Math.min(transferableXp, drained.getAmount() / MILLIBUCKETS_PER_XP);
        if (movedXp <= 0) {
            return 0;
        }

        setStoredXp(stack, storedXp + movedXp);
        return movedXp;
    }

    private static FluidStack drainExperienceFluid(IFluidHandler fluidHandler, int maxAmount, IFluidHandler.FluidAction action) {
        for (int tank = 0; tank < fluidHandler.getTanks(); tank++) {
            FluidStack fluid = fluidHandler.getFluidInTank(tank);
            if (fluid.isEmpty() || !fluid.is(EXPERIENCE_FLUIDS)) {
                continue;
            }

            FluidStack request = fluid.copy();
            request.setAmount(Math.min(maxAmount, fluid.getAmount()));
            return fluidHandler.drain(request, action);
        }

        return FluidStack.EMPTY;
    }

    public static int getStoredXp(ItemStack stack) {
        CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        return Math.clamp(customData.copyTag().getInt(STORED_XP_KEY), 0, MAX_STORED_XP);
    }

    public static int addStoredXp(ItemStack stack, int amount) {
        int storedXp = getStoredXp(stack);
        int transferable = Math.min(Math.max(amount, 0), MAX_STORED_XP - storedXp);
        if (transferable <= 0) {
            return 0;
        }

        setStoredXp(stack, storedXp + transferable);
        return transferable;
    }

    private static void setStoredXp(ItemStack stack, int storedXp) {
        int clampedXp = Math.clamp(storedXp, 0, MAX_STORED_XP);
        stack.update(DataComponents.CUSTOM_DATA, CustomData.EMPTY, data -> {
            CompoundTag tag = data.copyTag();
            if (clampedXp <= 0) {
                tag.remove(STORED_XP_KEY);
            } else {
                tag.putInt(STORED_XP_KEY, clampedXp);
            }
            return CustomData.of(tag);
        });
    }

    private static int getPlayerTotalXp(Player player) {
        return getTotalXpForLevel(player.experienceLevel)
                + Math.round(player.experienceProgress * getXpNeededForNextLevel(player.experienceLevel));
    }

    private static int getXpAfterGainingLevels(Player player) {
        int targetLevel = player.experienceLevel + MAX_LEVEL_SPAN;
        return getTotalXpForLevel(targetLevel)
                + Math.round(player.experienceProgress * getXpNeededForNextLevel(targetLevel));
    }

    private static int getXpAfterLosingLevels(Player player) {
        if (player.experienceLevel <= MAX_LEVEL_SPAN) {
            return 0;
        }

        int targetLevel = player.experienceLevel - MAX_LEVEL_SPAN;
        return getTotalXpForLevel(targetLevel)
                + Math.round(player.experienceProgress * getXpNeededForNextLevel(targetLevel));
    }

    private static void setPlayerTotalXp(Player player, int totalXp) {
        int clampedXp = Math.max(0, totalXp);
        int level = getLevelForTotalXp(clampedXp);
        int levelBaseXp = getTotalXpForLevel(level);
        int xpIntoLevel = clampedXp - levelBaseXp;
        int xpNeeded = getXpNeededForNextLevel(level);

        player.totalExperience = clampedXp;
        player.experienceLevel = level;
        player.experienceProgress = xpNeeded == 0 ? 0 : xpIntoLevel / (float) xpNeeded;

        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.connection.send(new ClientboundSetExperiencePacket(
                    player.experienceProgress,
                    player.totalExperience,
                    player.experienceLevel
            ));
        }
    }

    private static int getLevelForTotalXp(int totalXp) {
        int level = 0;
        while (getTotalXpForLevel(level + 1) <= totalXp) {
            level++;
        }
        return level;
    }

    private static int getTotalXpForLevel(int level) {
        if (level <= 0) {
            return 0;
        }
        int total = 0;
        for (int currentLevel = 0; currentLevel < level; currentLevel++) {
            total += getXpNeededForNextLevel(currentLevel);
        }
        return total;
    }

    private static int getXpNeededForNextLevel(int level) {
        if (level >= 30) {
            return 112 + (level - 30) * 9;
        }
        if (level >= 15) {
            return 37 + (level - 15) * 5;
        }
        return 7 + level * 2;
    }
}
