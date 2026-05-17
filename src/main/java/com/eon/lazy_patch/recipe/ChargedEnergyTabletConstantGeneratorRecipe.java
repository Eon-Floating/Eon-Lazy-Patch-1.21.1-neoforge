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

public class ChargedEnergyTabletConstantGeneratorRecipe implements CraftingRecipe {
    private static final ResourceLocation ENERGY_TABLET_ID = ResourceLocation.fromNamespaceAndPath("mekanism", "energy_tablet");

    private final CraftingBookCategory category;

    public ChargedEnergyTabletConstantGeneratorRecipe(CraftingBookCategory category) {
        this.category = category;
    }

    @Override
    public boolean matches(@NotNull CraftingInput input, @NotNull Level level) {
        if (input.width() != 3 || input.height() != 3) {
            return false;
        }

        return isDiamond(input.getItem(0, 0))
                && isRedstone(input.getItem(1, 0))
                && isDiamond(input.getItem(2, 0))
                && isRedstone(input.getItem(0, 1))
                && isFullEnergyTablet(input.getItem(1, 1))
                && isRedstone(input.getItem(2, 1))
                && isDiamond(input.getItem(0, 2))
                && isRedstone(input.getItem(1, 2))
                && isDiamond(input.getItem(2, 2));
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CraftingInput input, @NotNull HolderLookup.Provider registries) {
        return ModItems.CONSTANT_GENERATOR.get().getDefaultInstance();
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull HolderLookup.Provider registries) {
        return ModItems.CONSTANT_GENERATOR.get().getDefaultInstance();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= 3 && height >= 3;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.CHARGED_ENERGY_TABLET_CONSTANT_GENERATOR.get();
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(Ingredient.EMPTY,
                Ingredient.of(Items.DIAMOND),
                Ingredient.of(Items.REDSTONE),
                Ingredient.of(Items.DIAMOND),
                Ingredient.of(Items.REDSTONE),
                Ingredient.of(createFullEnergyTabletStack()),
                Ingredient.of(Items.REDSTONE),
                Ingredient.of(Items.DIAMOND),
                Ingredient.of(Items.REDSTONE),
                Ingredient.of(Items.DIAMOND)
        );
    }

    @Override
    public @NotNull CraftingBookCategory category() {
        return category;
    }

    private static boolean isDiamond(ItemStack stack) {
        return stack.is(Items.DIAMOND);
    }

    private static boolean isRedstone(ItemStack stack) {
        return stack.is(Items.REDSTONE);
    }

    private static boolean isFullEnergyTablet(ItemStack stack) {
        if (!BuiltInRegistries.ITEM.getKey(stack.getItem()).equals(ENERGY_TABLET_ID)) {
            return false;
        }

        IEnergyStorage energyStorage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
        return energyStorage != null
                && energyStorage.getMaxEnergyStored() > 0
                && energyStorage.getEnergyStored() >= energyStorage.getMaxEnergyStored();
    }

    private static ItemStack createFullEnergyTabletStack() {
        ItemStack stack = new ItemStack(BuiltInRegistries.ITEM.get(ENERGY_TABLET_ID));
        IEnergyStorage energyStorage = stack.getCapability(Capabilities.EnergyStorage.ITEM);
        if (energyStorage != null && energyStorage.canReceive()) {
            while (energyStorage.getEnergyStored() < energyStorage.getMaxEnergyStored()) {
                int remaining = energyStorage.getMaxEnergyStored() - energyStorage.getEnergyStored();
                int received = energyStorage.receiveEnergy(remaining, false);
                if (received <= 0) {
                    break;
                }
            }
        }
        return stack;
    }
}
