package com.eon.lazy_patch.block;

import com.eon.lazy_patch.EonLazyPatch;
import com.eon.lazy_patch.block.custom.ConstantGeneratorBlock;
import com.eon.lazy_patch.block.custom.ExperienceInfuserBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(EonLazyPatch.MODID);

    public static final DeferredBlock<ExperienceInfuserBlock> EXPERIENCE_INFUSER = BLOCKS.registerBlock(
            "experience_infuser",
            ExperienceInfuserBlock::new,
            BlockBehaviour.Properties.of()
                    .strength(3.5f)
                    .sound(SoundType.METAL)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredBlock<ConstantGeneratorBlock> CONSTANT_GENERATOR = BLOCKS.registerBlock(
            "constant_generator",
            ConstantGeneratorBlock::new,
            BlockBehaviour.Properties.of()
                    .strength(3.5f)
                    .sound(SoundType.METAL)
                    .requiresCorrectToolForDrops()
    );

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
