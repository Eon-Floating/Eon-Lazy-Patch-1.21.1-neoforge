package com.eon.lazy_patch.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = "com.blakebr0.mysticalagriculture.block.MysticalCropBlock", remap = false)
public abstract class MysticalCropBlockMixin {
    @Inject(method = "isBonemealSuccess", at = @At("HEAD"), cancellable = true, remap = false)
    private void eonLazyPatch$allowBonemeal(
            Level level,
            RandomSource random,
            BlockPos pos,
            BlockState state,
            CallbackInfoReturnable<Boolean> callback
    ) {
        callback.setReturnValue(true);
    }
}
