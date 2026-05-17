package com.eon.lazy_patch.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class StyledNameBlockItem extends BlockItem {
    private final ChatFormatting[] formats;

    public StyledNameBlockItem(Block block, Properties properties, ChatFormatting... formats) {
        super(block, properties);
        this.formats = formats;
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        return super.getName(stack).copy().withStyle(formats);
    }
}
