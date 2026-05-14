package com.eon.lazy_patch.item;

import com.eon.lazy_patch.EonLazyPatch;
import com.eon.lazy_patch.block.ModBlocks;
import com.eon.lazy_patch.item.custom.ExperienceCrystalItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(EonLazyPatch.MODID);
    public static final DeferredItem<Item> EON_STAR =
            ITEMS.register("misc/eon_star", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> EXPERIENCE_CRYSTAL =
            ITEMS.register("experience_crystal", ExperienceCrystalItem::new);
    public static final DeferredItem<BlockItem> EXPERIENCE_INFUSER =
            ITEMS.registerSimpleBlockItem(ModBlocks.EXPERIENCE_INFUSER);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
