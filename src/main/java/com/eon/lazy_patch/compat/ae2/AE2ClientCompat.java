package com.eon.lazy_patch.compat.ae2;

import appeng.api.client.StorageCellModels;
import com.eon.lazy_patch.EonLazyPatch;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.registries.DeferredItem;

public class AE2ClientCompat {
    public static void init(FMLClientSetupEvent event) {
        registerModelIfPresent(event, AE2Items.INFINITY_LIQUID_SCULK_MATTER_CELL,
                "block/drive/infinity_liquid_sculk_matter_cell");
        registerModelIfPresent(event, AE2Items.INFINITY_LIQUID_DRAGON_BREATH_CELL,
                "block/drive/infinity_liquid_dragon_breath_cell");
        registerModelIfPresent(event, AE2Items.INFINITY_ETHER_GAS_CELL,
                "block/drive/infinity_ether_gas_cell");
        registerModelIfPresent(event, AE2Items.INFINITY_PINK_SLIME_CELL,
                "block/drive/infinity_pink_slime_cell");
    }

    private static void registerModelIfPresent(FMLClientSetupEvent event, DeferredItem<?> item, String modelPath) {
        if (item == null) {
            return;
        }

        event.enqueueWork(() -> StorageCellModels.registerModel(
                item.get(),
                ResourceLocation.fromNamespaceAndPath(EonLazyPatch.MODID, modelPath)
        ));
    }
}
