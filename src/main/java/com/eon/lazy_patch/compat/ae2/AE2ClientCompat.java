package com.eon.lazy_patch.compat.ae2;

import appeng.api.client.StorageCellModels;
import com.eon.lazy_patch.EonLazyPatch;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

public class AE2ClientCompat {
    public static void init(FMLClientSetupEvent event) {
        event.enqueueWork(() -> StorageCellModels.registerModel(
                AE2Items.INFINITY_LIQUID_SCULK_MATTER_CELL.get(),
                ResourceLocation.fromNamespaceAndPath(EonLazyPatch.MODID, "block/drive/infinity_liquid_sculk_matter_cell")
        ));
    }
}
