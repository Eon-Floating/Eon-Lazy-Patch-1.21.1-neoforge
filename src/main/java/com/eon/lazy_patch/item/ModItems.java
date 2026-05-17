package com.eon.lazy_patch.item;

import com.eon.lazy_patch.EonLazyPatch;
import com.eon.lazy_patch.block.ModBlocks;
import com.eon.lazy_patch.item.custom.ExperienceCrystalItem;
import com.eon.lazy_patch.item.custom.StyledNameBlockItem;
import com.eon.lazy_patch.item.custom.StyledNameItem;
import net.minecraft.ChatFormatting;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(EonLazyPatch.MODID);
    public static final DeferredItem<Item> EON_STAR =
            ITEMS.register("misc/eon_star", () -> new StyledNameItem(new Item.Properties(), ChatFormatting.DARK_RED, ChatFormatting.BOLD));
    public static final DeferredItem<Item> EXPERIENCE_CRYSTAL =
            ITEMS.register("experience_crystal", ExperienceCrystalItem::new);
    public static final DeferredItem<BlockItem> EXPERIENCE_INFUSER =
            ITEMS.register("experience_infuser", () -> new StyledNameBlockItem(ModBlocks.EXPERIENCE_INFUSER.get(), new Item.Properties(), ChatFormatting.GREEN));
    public static final DeferredItem<BlockItem> CONSTANT_GENERATOR =
            ITEMS.register("constant_generator", () -> new StyledNameBlockItem(ModBlocks.CONSTANT_GENERATOR.get(), new Item.Properties(), ChatFormatting.DARK_PURPLE));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
