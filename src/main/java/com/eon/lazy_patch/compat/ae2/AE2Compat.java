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
    }
}
