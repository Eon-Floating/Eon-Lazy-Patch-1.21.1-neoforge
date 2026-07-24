package com.eon.lazy_patch.item.material;

import com.eon.lazy_patch.EonLazyPatch;
import com.eon.lazy_patch.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.SimpleTier;

public final class ModToolTiers {
    public static final TagKey<Block> INCORRECT_FOR_EON_TOOL = TagKey.create(
            Registries.BLOCK,
            ResourceLocation.fromNamespaceAndPath(EonLazyPatch.MODID, "incorrect_for_eon_tool")
    );

    public static final SimpleTier EON = new SimpleTier(
            INCORRECT_FOR_EON_TOOL,
            9001,
            35.0F,
            65.0F,
            200,
            () -> net.minecraft.world.item.crafting.Ingredient.of(ModItems.EON_PEARL.get())
    );

    private ModToolTiers() {
    }
}
