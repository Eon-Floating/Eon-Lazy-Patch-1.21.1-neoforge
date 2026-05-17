package com.eon.lazy_patch.recipe;

import com.eon.lazy_patch.EonLazyPatch;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, EonLazyPatch.MODID);

    public static final DeferredHolder<RecipeSerializer<?>, SimpleCraftingRecipeSerializer<ChargedEnergyTabletConstantGeneratorRecipe>> CHARGED_ENERGY_TABLET_CONSTANT_GENERATOR =
            RECIPE_SERIALIZERS.register("charged_energy_tablet_constant_generator",
                    () -> new SimpleCraftingRecipeSerializer<>(ChargedEnergyTabletConstantGeneratorRecipe::new));

    public static final DeferredHolder<RecipeSerializer<?>, SimpleCraftingRecipeSerializer<CreativeEnergyCubeRecipe>> CREATIVE_ENERGY_CUBE =
            RECIPE_SERIALIZERS.register("creative_energy_cube",
                    () -> new SimpleCraftingRecipeSerializer<>(CreativeEnergyCubeRecipe::new));

    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZERS.register(eventBus);
    }
}
