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
        event.enqueueWork(() -> StorageCellModels.registerModel(
                AE2Items.INFINITY_LIQUID_DRAGON_BREATH_CELL.get(),
                ResourceLocation.fromNamespaceAndPath(EonLazyPatch.MODID, "block/drive/infinity_liquid_dragon_breath_cell")
        ));
        event.enqueueWork(() -> StorageCellModels.registerModel(
                AE2Items.INFINITY_ETHER_GAS_CELL.get(),
                ResourceLocation.fromNamespaceAndPath(EonLazyPatch.MODID, "block/drive/infinity_ether_gas_cell")
        ));
        event.enqueueWork(() -> StorageCellModels.registerModel(
                AE2Items.INFINITY_PINK_SLIME_CELL.get(),
                ResourceLocation.fromNamespaceAndPath(EonLazyPatch.MODID, "block/drive/infinity_pink_slime_cell")
        ));
    }
}
