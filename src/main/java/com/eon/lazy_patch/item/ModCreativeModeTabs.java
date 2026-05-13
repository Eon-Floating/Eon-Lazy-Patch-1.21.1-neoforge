package com.eon.lazy_patch.item;

import com.eon.lazy_patch.EonLazyPatch;
import com.eon.lazy_patch.compat.ae2.AE2Compat;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, EonLazyPatch.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EON_LAZY_PATCH_TAB =
            CREATIVE_MODE_TABS.register("main", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.eon_lazy_patch"))
                    .withTabsBefore(CreativeModeTabs.INGREDIENTS)
                    .icon(() -> ModItems.EON_STAR.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.EON_STAR.get());
                        output.accept(ModItems.EXPERIENCE_CRYSTAL.get());
                        if (ModList.get().isLoaded("ae2")) {
                            AE2Compat.addCreativeTabItems(output);
                        }
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
