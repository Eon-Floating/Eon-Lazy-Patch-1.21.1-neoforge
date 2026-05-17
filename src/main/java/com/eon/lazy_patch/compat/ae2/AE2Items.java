package com.eon.lazy_patch.compat.ae2;

import com.eon.lazy_patch.EonLazyPatch;
import com.eon.lazy_patch.item.custom.StyledNameItem;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AE2Items {
    public static final String AE2_MOD_ID = "ae2";
    public static final String INDUSTRIAL_FOREGOING_MOD_ID = "industrialforegoing";
    public static final String IFEU_MOD_ID = "ifeu";

    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(EonLazyPatch.MODID);

    public static DeferredItem<Item> INFINITY_LIQUID_SCULK_MATTER_CELL;
    public static DeferredItem<Item> INFINITY_LIQUID_DRAGON_BREATH_CELL;
    public static DeferredItem<Item> INFINITY_ETHER_GAS_CELL;
    public static DeferredItem<Item> INFINITY_PINK_SLIME_CELL;
    public static DeferredItem<Item> LIQUID_SCULK_MATTER_CALIBRATION_CORE;
    public static DeferredItem<Item> LIQUID_DRAGON_BREATH_CALIBRATION_CORE;
    public static DeferredItem<Item> ETHER_GAS_CALIBRATION_CORE;
    public static DeferredItem<Item> PINK_SLIME_CALIBRATION_CORE;

    public static void prepareRegistrations() {
        INFINITY_LIQUID_SCULK_MATTER_CELL = registerInfinityCell(
                IFEU_MOD_ID,
                "ae2/infinity_liquid_sculk_matter_cell",
                ResourceLocation.fromNamespaceAndPath(IFEU_MOD_ID, "liquid_sculk_matter"));
        INFINITY_LIQUID_DRAGON_BREATH_CELL = registerInfinityCell(
                IFEU_MOD_ID,
                "ae2/infinity_liquid_dragon_breath_cell",
                ResourceLocation.fromNamespaceAndPath(IFEU_MOD_ID, "liquid_dragon_breath"));
        INFINITY_ETHER_GAS_CELL = registerInfinityCell(
                INDUSTRIAL_FOREGOING_MOD_ID,
                "ae2/infinity_ether_gas_cell",
                ResourceLocation.fromNamespaceAndPath(INDUSTRIAL_FOREGOING_MOD_ID, "ether_gas"));
        INFINITY_PINK_SLIME_CELL = registerInfinityCell(
                INDUSTRIAL_FOREGOING_MOD_ID,
                "ae2/infinity_pink_slime_cell",
                ResourceLocation.fromNamespaceAndPath(INDUSTRIAL_FOREGOING_MOD_ID, "pink_slime"));
        LIQUID_SCULK_MATTER_CALIBRATION_CORE = registerCalibrationCore(
                IFEU_MOD_ID,
                "ae2/liquid_sculk_matter_calibration_core");
        LIQUID_DRAGON_BREATH_CALIBRATION_CORE = registerCalibrationCore(
                IFEU_MOD_ID,
                "ae2/liquid_dragon_breath_calibration_core");
        ETHER_GAS_CALIBRATION_CORE = registerCalibrationCore(
                INDUSTRIAL_FOREGOING_MOD_ID,
                "ae2/ether_gas_calibration_core");
        PINK_SLIME_CALIBRATION_CORE = registerCalibrationCore(
                INDUSTRIAL_FOREGOING_MOD_ID,
                "ae2/pink_slime_calibration_core");
    }

    private static DeferredItem<Item> registerInfinityCell(String requiredModId, String registryName, ResourceLocation fluidId) {
        return ModList.get().isLoaded(AE2_MOD_ID) && ModList.get().isLoaded(requiredModId)
                ? ITEMS.register(registryName, () -> new EonInfinityFluidCellItem(fluidId))
                : null;
    }

    private static DeferredItem<Item> registerCalibrationCore(String requiredModId, String registryName) {
        return ModList.get().isLoaded(AE2_MOD_ID) && ModList.get().isLoaded(requiredModId)
                ? ITEMS.register(registryName, () -> new StyledNameItem(new Item.Properties(), ChatFormatting.DARK_PURPLE))
                : null;
    }

    public static void register(IEventBus eventBus) {
        prepareRegistrations();
        ITEMS.register(eventBus);
    }
}
