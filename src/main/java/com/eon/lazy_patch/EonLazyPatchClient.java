package com.eon.lazy_patch;

import com.eon.lazy_patch.client.gui.ExperienceInfuserScreen;
import com.eon.lazy_patch.client.gui.ConstantGeneratorScreen;
import com.eon.lazy_patch.compat.ae2.AE2ClientCompat;
import com.eon.lazy_patch.menu.ModMenuTypes;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = EonLazyPatch.MODID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = EonLazyPatch.MODID, value = Dist.CLIENT)
public class EonLazyPatchClient {
    public EonLazyPatchClient(ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        // Some client setup code
        EonLazyPatch.LOGGER.info("HELLO FROM CLIENT SETUP");
        EonLazyPatch.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        if (ModList.get().isLoaded("ae2")) {
            AE2ClientCompat.init(event);
        }
    }

    @SubscribeEvent
    static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.EXPERIENCE_INFUSER.get(), ExperienceInfuserScreen::new);
        event.register(ModMenuTypes.CONSTANT_GENERATOR.get(), ConstantGeneratorScreen::new);
    }
}
