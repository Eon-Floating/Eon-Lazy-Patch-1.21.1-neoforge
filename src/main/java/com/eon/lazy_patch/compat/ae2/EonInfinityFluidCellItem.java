package com.eon.lazy_patch.compat.ae2;

import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.GenericStack;
import appeng.items.storage.StorageCellTooltipComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class EonInfinityFluidCellItem extends Item {
    private final ResourceLocation fluidId;
    private AEKey cachedRecord;

    public EonInfinityFluidCellItem(ResourceLocation fluidId) {
        super(new Item.Properties().stacksTo(1));
        this.fluidId = fluidId;
    }

    public AEKey getRecord() {
        if (this.cachedRecord == null) {
            Fluid fluid = BuiltInRegistries.FLUID.get(this.fluidId);
            if (fluid == Fluids.EMPTY) {
                throw new IllegalStateException("Missing fluid for infinity cell: " + this.fluidId);
            }
            this.cachedRecord = AEFluidKey.of(fluid);
        }

        return this.cachedRecord;
    }

    public static long getDisplayedAmount(AEKey key) {
        return Integer.MAX_VALUE * (long) key.getAmountPerUnit();
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable("item.eon_lazy_patch.infinity_fluid_cell", this.getRecord().getDisplayName());
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.eon_lazy_patch.infinity_fluid_cell").withStyle(ChatFormatting.GREEN));
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        AEKey record = this.getRecord();
        List<GenericStack> content = Collections.singletonList(new GenericStack(record, getDisplayedAmount(record)));
        return Optional.of(new StorageCellTooltipComponent(List.of(), content, false, true));
    }
}
