package com.eon.lazy_patch.block.entity;

import com.eon.lazy_patch.EonLazyPatch;
import com.eon.lazy_patch.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, EonLazyPatch.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ExperienceInfuserBlockEntity>> EXPERIENCE_INFUSER =
            BLOCK_ENTITIES.register("experience_infuser", () -> BlockEntityType.Builder.of(
                    ExperienceInfuserBlockEntity::new,
                    ModBlocks.EXPERIENCE_INFUSER.get()
            ).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                EXPERIENCE_INFUSER.get(),
                (blockEntity, side) -> blockEntity.getFluidHandler()
        );
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                EXPERIENCE_INFUSER.get(),
                (blockEntity, side) -> blockEntity.getItemHandler()
        );
    }
}
