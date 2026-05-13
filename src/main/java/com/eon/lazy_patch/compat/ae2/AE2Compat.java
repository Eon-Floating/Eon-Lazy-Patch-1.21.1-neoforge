package com.eon.lazy_patch.compat.ae2;

import appeng.api.storage.StorageCells;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

public class AE2Compat {
    public static void register(IEventBus eventBus) {
        AE2Items.register(eventBus);
    }

    public static void init(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> StorageCells.addCellHandler(EonInfinityFluidCellInventory.HANDLER));
    }

    public static void addCreativeTabItems(CreativeModeTab.Output output) {
        output.accept(AE2Items.INFINITY_LIQUID_SCULK_MATTER_CELL.get());
        output.accept(AE2Items.INFINITY_LIQUID_DRAGON_BREATH_CELL.get());
        output.accept(AE2Items.INFINITY_ETHER_GAS_CELL.get());
        output.accept(AE2Items.INFINITY_PINK_SLIME_CELL.get());
        output.accept(AE2Items.LIQUID_SCULK_MATTER_CALIBRATION_CORE.get());
        output.accept(AE2Items.LIQUID_DRAGON_BREATH_CALIBRATION_CORE.get());
        output.accept(AE2Items.ETHER_GAS_CALIBRATION_CORE.get());
        output.accept(AE2Items.PINK_SLIME_CALIBRATION_CORE.get());
    }
}
