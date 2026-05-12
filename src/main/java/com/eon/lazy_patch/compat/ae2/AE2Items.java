package com.eon.lazy_patch.compat.ae2;

import com.eon.lazy_patch.EonLazyPatch;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AE2Items {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(EonLazyPatch.MODID);

    public static final DeferredItem<Item> INFINITY_LIQUID_SCULK_MATTER_CELL =
            ITEMS.register("ae2/infinity_liquid_sculk_matter_cell", () -> new EonInfinityFluidCellItem(
                    ResourceLocation.fromNamespaceAndPath("ifeu", "liquid_sculk_matter")));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
