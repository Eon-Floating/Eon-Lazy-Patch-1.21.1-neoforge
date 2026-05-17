package com.eon.lazy_patch.recipe;

import com.eon.lazy_patch.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

public class CreativeEnergyCubeRecipe implements CraftingRecipe {
    private static final ResourceLocation ULTIMATE_ENERGY_CUBE_ID = ResourceLocation.fromNamespaceAndPath("mekanism", "ultimate_energy_cube");
    private static final ResourceLocation CREATIVE_ENERGY_CUBE_ID = ResourceLocation.fromNamespaceAndPath("mekanism", "creative_energy_cube");

    private final CraftingBookCategory category;

    public CreativeEnergyCubeRecipe(CraftingBookCategory category) {
        this.category = category;
    }

    @Override
    public boolean matches(@NotNull CraftingInput input, @NotNull Level level) {
        if (input.width() != 3 || input.height() != 3) {
            return false;
        }

        return isNetheriteBlock(input.getItem(0, 0))
                && isNetherStar(input.getItem(1, 0))
                && isNetheriteBlock(input.getItem(2, 0))
                && isFullUltimateEnergyCube(input.getItem(0, 1))
                && isConstantGenerator(input.getItem(1, 1))
                && isFullUltimateEnergyCube(input.getItem(2, 1))
                && isNetheriteBlock(input.getItem(0, 2))
                && isNetherStar(input.getItem(1, 2))
                && isNetheriteBlock(input.getItem(2, 2));
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CraftingInput input, @NotNull HolderLookup.Provider registries) {
        return createCreativeEnergyCubeStack();
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull HolderLookup.Provider registries) {
        return createCreativeEnergyCubeStack();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= 3 && height >= 3;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.CREATIVE_ENERGY_CUBE.get();
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(Ingredient.EMPTY,
                Ingredient.of(Items.NETHERITE_BLOCK),
                Ingredient.of(Items.NETHER_STAR),
                Ingredient.of(Items.NETHERITE_BLOCK),
                Ingredient.of(createFullUltimateEnergyCubeStack()),
                Ingredient.of(ModItems.CONSTANT_GENERATOR.get()),
                Ingredient.of(createFullUltimateEnergyCubeStack()),
                Ingredient.of(Items.NETHERITE_BLOCK),
                Ingredient.of(Items.NETHER_STAR),
                Ingredient.of(Items.NETHERITE_BLOCK)
        );
    }

    @Override
    public @NotNull CraftingBookCategory category() {
        return category;
    }

    private static boolean isNetheriteBlock(ItemStack stack) {
        return stack.is(Items.NETHERITE_BLOCK);
    }

    private static boolean isNetherStar(ItemStack stack) {
        return stack.is(Items.NETHER_STAR);
    }

    private static boolean isConstantGenerator(ItemStack stack) {
        return stack.is(ModItems.CONSTANT_GENERATOR.get());
    }

    private static boolean isFullUltimateEnergyCube(ItemStack stack) {
        if (!BuiltInRegistries.ITEM.getKey(stack.getItem()).equals(ULTIMATE_ENERGY_CUBE_ID)) {
            return false;
        }

        IEnergyStorage energyStorage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
        return energyStorage != null
                && energyStorage.getMaxEnergyStored() > 0
                && energyStorage.getEnergyStored() >= energyStorage.getMaxEnergyStored();
    }

    private static ItemStack createFullUltimateEnergyCubeStack() {
        return createFullEnergyStack(ULTIMATE_ENERGY_CUBE_ID);
    }

    private static ItemStack createCreativeEnergyCubeStack() {
        return createFullEnergyStack(CREATIVE_ENERGY_CUBE_ID);
    }

    private static ItemStack createFullEnergyStack(ResourceLocation itemId) {
        ItemStack stack = new ItemStack(BuiltInRegistries.ITEM.get(itemId));
        ItemStack mekanismFilledStack = getMekanismFilledEnergyVariant(stack);
        if (!mekanismFilledStack.isEmpty() && BuiltInRegistries.ITEM.getKey(mekanismFilledStack.getItem()).equals(itemId)) {
            return mekanismFilledStack;
        }

        IEnergyStorage energyStorage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
        if (energyStorage != null && energyStorage.canReceive()) {
            int attempts = 0;
            while (energyStorage.getEnergyStored() < energyStorage.getMaxEnergyStored()) {
                int before = energyStorage.getEnergyStored();
                int remaining = energyStorage.getMaxEnergyStored() - energyStorage.getEnergyStored();
                int received = energyStorage.receiveEnergy(remaining, false);
                if (received <= 0 || energyStorage.getEnergyStored() <= before || ++attempts > 64) {
                    break;
                }
            }
        }
        return stack;
    }

    private static ItemStack getMekanismFilledEnergyVariant(ItemStack stack) {
        try {
            Class<?> storageUtils = Class.forName("mekanism.common.util.StorageUtils");
            Object filledStack = storageUtils.getMethod("getFilledEnergyVariant", ItemStack.class).invoke(null, stack);
            if (filledStack instanceof ItemStack itemStack) {
                return itemStack;
            }
        } catch (ReflectiveOperationException ignored) {
            // Mekanism is optional at class-load time; fall back to the generic energy capability.
        }
        return ItemStack.EMPTY;
    }
}
