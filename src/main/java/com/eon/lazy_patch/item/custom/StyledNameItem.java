package com.eon.lazy_patch.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class StyledNameItem extends Item {
    private final ChatFormatting[] formats;

    public StyledNameItem(Properties properties, ChatFormatting... formats) {
        super(properties);
        this.formats = formats;
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        return super.getName(stack).copy().withStyle(formats);
    }
}
