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
    public static final DeferredItem<Item> INFINITY_LIQUID_DRAGON_BREATH_CELL =
            ITEMS.register("ae2/infinity_liquid_dragon_breath_cell", () -> new EonInfinityFluidCellItem(
                    ResourceLocation.fromNamespaceAndPath("ifeu", "liquid_dragon_breath")));
    public static final DeferredItem<Item> INFINITY_ETHER_GAS_CELL =
            ITEMS.register("ae2/infinity_ether_gas_cell", () -> new EonInfinityFluidCellItem(
                    ResourceLocation.fromNamespaceAndPath("industrialforegoing", "ether_gas")));
    public static final DeferredItem<Item> INFINITY_PINK_SLIME_CELL =
            ITEMS.register("ae2/infinity_pink_slime_cell", () -> new EonInfinityFluidCellItem(
                    ResourceLocation.fromNamespaceAndPath("industrialforegoing", "pink_slime")));
    public static final DeferredItem<Item> LIQUID_SCULK_MATTER_CALIBRATION_CORE =
            ITEMS.register("ae2/liquid_sculk_matter_calibration_core", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> LIQUID_DRAGON_BREATH_CALIBRATION_CORE =
            ITEMS.register("ae2/liquid_dragon_breath_calibration_core", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> ETHER_GAS_CALIBRATION_CORE =
            ITEMS.register("ae2/ether_gas_calibration_core", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PINK_SLIME_CALIBRATION_CORE =
            ITEMS.register("ae2/pink_slime_calibration_core", () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
