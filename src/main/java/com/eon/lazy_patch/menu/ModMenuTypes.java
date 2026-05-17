package com.eon.lazy_patch.menu;

import com.eon.lazy_patch.EonLazyPatch;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(Registries.MENU, EonLazyPatch.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<ExperienceInfuserMenu>> EXPERIENCE_INFUSER =
            MENU_TYPES.register("experience_infuser", () -> new MenuType<>(
                    (IContainerFactory<ExperienceInfuserMenu>) ExperienceInfuserMenu::new,
                    FeatureFlags.VANILLA_SET
            ));
    public static final DeferredHolder<MenuType<?>, MenuType<ConstantGeneratorMenu>> CONSTANT_GENERATOR =
            MENU_TYPES.register("constant_generator", () -> new MenuType<>(
                    (IContainerFactory<ConstantGeneratorMenu>) ConstantGeneratorMenu::new,
                    FeatureFlags.VANILLA_SET
            ));

    public static void register(IEventBus eventBus) {
        MENU_TYPES.register(eventBus);
    }
}
