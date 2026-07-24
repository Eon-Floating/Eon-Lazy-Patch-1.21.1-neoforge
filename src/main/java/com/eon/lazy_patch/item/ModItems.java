package com.eon.lazy_patch.item;

import com.eon.lazy_patch.EonLazyPatch;
import com.eon.lazy_patch.block.ModBlocks;
import com.eon.lazy_patch.item.custom.ExperienceCrystalItem;
import com.eon.lazy_patch.item.custom.StyledNameBlockItem;
import com.eon.lazy_patch.item.custom.StyledNameItem;
import com.eon.lazy_patch.item.material.ModArmorMaterials;
import com.eon.lazy_patch.item.material.ModToolTiers;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.Unbreakable;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(EonLazyPatch.MODID);
    public static final DeferredItem<Item> EON_STAR =
            ITEMS.register("misc/eon_star", () -> new StyledNameItem(new Item.Properties(), ChatFormatting.DARK_RED, ChatFormatting.BOLD));
    public static final DeferredItem<Item> EON_PEARL =
            ITEMS.register("eon_pearl", () -> new StyledNameItem(
                    new Item.Properties().rarity(Rarity.EPIC).fireResistant(),
                    ChatFormatting.DARK_RED,
                    ChatFormatting.BOLD
            ));
    public static final DeferredItem<ArmorItem> EON_HELMET =
            ITEMS.register("eon_helmet", () -> new ArmorItem(
                    ModArmorMaterials.EON,
                    ArmorItem.Type.HELMET,
                    terminalArmorProperties(ArmorItem.Type.HELMET)
            ));
    public static final DeferredItem<ArmorItem> EON_CHESTPLATE =
            ITEMS.register("eon_chestplate", () -> new ArmorItem(
                    ModArmorMaterials.EON,
                    ArmorItem.Type.CHESTPLATE,
                    terminalArmorProperties(ArmorItem.Type.CHESTPLATE)
            ));
    public static final DeferredItem<ArmorItem> EON_LEGGINGS =
            ITEMS.register("eon_leggings", () -> new ArmorItem(
                    ModArmorMaterials.EON,
                    ArmorItem.Type.LEGGINGS,
                    terminalArmorProperties(ArmorItem.Type.LEGGINGS)
            ));
    public static final DeferredItem<ArmorItem> EON_BOOTS =
            ITEMS.register("eon_boots", () -> new ArmorItem(
                    ModArmorMaterials.EON,
                    ArmorItem.Type.BOOTS,
                    terminalArmorProperties(ArmorItem.Type.BOOTS)
            ));
    public static final DeferredItem<SwordItem> EON_SWORD =
            ITEMS.register("eon_sword", () -> new SwordItem(
                    ModToolTiers.EON,
                    terminalProperties().attributes(SwordItem.createAttributes(ModToolTiers.EON, 3, 2.9F))
            ));
    public static final DeferredItem<PickaxeItem> EON_PICKAXE =
            ITEMS.register("eon_pickaxe", () -> new PickaxeItem(
                    ModToolTiers.EON,
                    terminalProperties().attributes(PickaxeItem.createAttributes(ModToolTiers.EON, 2.0F, 2.0F))
            ));
    public static final DeferredItem<AxeItem> EON_AXE =
            ITEMS.register("eon_axe", () -> new AxeItem(
                    ModToolTiers.EON,
                    terminalProperties().attributes(AxeItem.createAttributes(ModToolTiers.EON, 4.0F, 2.4F))
            ));
    public static final DeferredItem<ShovelItem> EON_SHOVEL =
            ITEMS.register("eon_shovel", () -> new ShovelItem(
                    ModToolTiers.EON,
                    terminalProperties().attributes(ShovelItem.createAttributes(ModToolTiers.EON, 1.0F, 1.9F))
            ));
    public static final DeferredItem<Item> EXPERIENCE_CRYSTAL =
            ITEMS.register("experience_crystal", ExperienceCrystalItem::new);
    public static final DeferredItem<BlockItem> EXPERIENCE_INFUSER =
            ITEMS.register("experience_infuser", () -> new StyledNameBlockItem(ModBlocks.EXPERIENCE_INFUSER.get(), new Item.Properties(), ChatFormatting.GREEN));
    public static final DeferredItem<BlockItem> CONSTANT_GENERATOR =
            ITEMS.register("constant_generator", () -> new StyledNameBlockItem(ModBlocks.CONSTANT_GENERATOR.get(), new Item.Properties(), ChatFormatting.DARK_PURPLE));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    private static Item.Properties terminalProperties() {
        return new Item.Properties()
                .rarity(Rarity.EPIC)
                .fireResistant()
                .component(DataComponents.UNBREAKABLE, new Unbreakable(true));
    }

    private static Item.Properties terminalArmorProperties(ArmorItem.Type type) {
        return terminalProperties().durability(type.getDurability(9001));
    }
}
