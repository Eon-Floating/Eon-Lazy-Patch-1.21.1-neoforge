package com.eon.lazy_patch.item.material;

import com.eon.lazy_patch.EonLazyPatch;
import com.eon.lazy_patch.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.Map;

public final class ModArmorMaterials {
    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS =
            DeferredRegister.create(Registries.ARMOR_MATERIAL, EonLazyPatch.MODID);

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> EON =
            ARMOR_MATERIALS.register("eon", () -> new ArmorMaterial(
                    Map.of(
                            ArmorItem.Type.HELMET, 8,
                            ArmorItem.Type.CHESTPLATE, 11,
                            ArmorItem.Type.LEGGINGS, 13,
                            ArmorItem.Type.BOOTS, 8,
                            ArmorItem.Type.BODY, 12
                    ),
                    125,
                    SoundEvents.ARMOR_EQUIP_NETHERITE,
                    () -> Ingredient.of(ModItems.EON_PEARL.get()),
                    List.of(new ArmorMaterial.Layer(
                            ResourceLocation.fromNamespaceAndPath(EonLazyPatch.MODID, "eon")
                    )),
                    15.0F,
                    1.0F
            ));

    private ModArmorMaterials() {
    }

    public static void register(IEventBus eventBus) {
        ARMOR_MATERIALS.register(eventBus);
    }
}
