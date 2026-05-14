package com.eon.lazy_patch.client.gui;

import com.eon.lazy_patch.item.custom.ExperienceCrystalItem;
import com.eon.lazy_patch.menu.ExperienceInfuserMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ExperienceInfuserScreen extends AbstractContainerScreen<ExperienceInfuserMenu> {
    private static final int PANEL_COLOR = 0xFFC6C6C6;
    private static final int PANEL_LIGHT = 0xFFFFFFFF;
    private static final int PANEL_SHADOW = 0xFF555555;
    private static final int PANEL_DARK = 0xFF373737;
    private static final int SLOT_INNER = 0xFF8B8B8B;
    private static final int FLUID_COLOR = 0xFF47D46A;
    private static final int FLUID_BACK_COLOR = 0xFF26322B;

    public ExperienceInfuserScreen(ExperienceInfuserMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        imageWidth = 176;
        imageHeight = 166;
        inventoryLabelY = 72;
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);

        if (isHovering(26, 20, 14, 48, mouseX, mouseY)) {
            guiGraphics.renderComponentTooltip(font, fluidTooltip(), mouseX, mouseY);
        }
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int left = leftPos;
        int top = topPos;
        drawPanel(guiGraphics, left, top, imageWidth, imageHeight);

        drawSlot(guiGraphics, left + 79, top + 34);
        drawPlayerInventorySlots(guiGraphics, left, top);
        drawFluidTank(guiGraphics, left + 26, top + 20);
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(font, title, titleLabelX, titleLabelY, 0x404040, false);
        guiGraphics.drawString(font, playerInventoryTitle, inventoryLabelX, inventoryLabelY, 0x404040, false);

        ItemStack crystal = menu.getCrystalStack();
        if (crystal.getItem() instanceof ExperienceCrystalItem) {
            int storedXp = ExperienceCrystalItem.getStoredXp(crystal);
            String text = storedXp + " / " + ExperienceCrystalItem.MAX_STORED_XP + " XP";
            guiGraphics.drawString(font, text, 104 - font.width(text) / 2, 38, 0x404040, false);
        }
    }

    private void drawPanel(GuiGraphics guiGraphics, int x, int y, int width, int height) {
        guiGraphics.fill(x, y, x + width, y + height, PANEL_COLOR);
        guiGraphics.fill(x, y, x + width - 1, y + 1, PANEL_LIGHT);
        guiGraphics.fill(x, y, x + 1, y + height - 1, PANEL_LIGHT);
        guiGraphics.fill(x + 1, y + height - 1, x + width, y + height, PANEL_DARK);
        guiGraphics.fill(x + width - 1, y + 1, x + width, y + height, PANEL_DARK);
        guiGraphics.fill(x + 1, y + 1, x + width - 1, y + 2, 0xFFE0E0E0);
        guiGraphics.fill(x + 1, y + 1, x + 2, y + height - 1, 0xFFE0E0E0);
        guiGraphics.fill(x + 2, y + height - 2, x + width - 1, y + height - 1, PANEL_SHADOW);
        guiGraphics.fill(x + width - 2, y + 2, x + width - 1, y + height - 1, PANEL_SHADOW);
    }

    private void drawSlot(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.fill(x, y, x + 18, y + 18, PANEL_DARK);
        guiGraphics.fill(x + 1, y + 1, x + 17, y + 17, SLOT_INNER);
        guiGraphics.fill(x + 1, y + 1, x + 17, y + 2, PANEL_SHADOW);
        guiGraphics.fill(x + 1, y + 1, x + 2, y + 17, PANEL_SHADOW);
        guiGraphics.fill(x + 2, y + 16, x + 17, y + 17, PANEL_LIGHT);
        guiGraphics.fill(x + 16, y + 2, x + 17, y + 17, PANEL_LIGHT);
    }

    private void drawPlayerInventorySlots(GuiGraphics guiGraphics, int left, int top) {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                drawSlot(guiGraphics, left + 7 + column * 18, top + 83 + row * 18);
            }
        }

        for (int column = 0; column < 9; column++) {
            drawSlot(guiGraphics, left + 7 + column * 18, top + 141);
        }
    }

    private void drawFluidTank(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.fill(x - 2, y - 2, x + 16, y + 50, PANEL_DARK);
        guiGraphics.fill(x - 1, y - 1, x + 15, y + 49, PANEL_SHADOW);
        guiGraphics.fill(x + 15, y - 2, x + 16, y + 50, PANEL_LIGHT);
        guiGraphics.fill(x - 2, y + 49, x + 16, y + 50, PANEL_LIGHT);
        guiGraphics.fill(x, y, x + 14, y + 48, FLUID_BACK_COLOR);

        int capacity = Math.max(1, menu.getFluidCapacity());
        int amount = Math.clamp(menu.getFluidAmount(), 0, capacity);
        int height = amount * 48 / capacity;
        if (height > 0) {
            guiGraphics.fill(x, y + 48 - height, x + 14, y + 48, FLUID_COLOR);
        }
    }

    private List<Component> fluidTooltip() {
        List<Component> tooltip = new ArrayList<>();
        tooltip.add(getFluidName().copy()
                .withStyle(ChatFormatting.GREEN));
        tooltip.add(Component.literal(menu.getFluidAmount() + " / " + menu.getFluidCapacity() + " mB")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal((menu.getFluidAmount() / ExperienceCrystalItem.MILLIBUCKETS_PER_XP) + " XP")
                .withStyle(ChatFormatting.GRAY));
        return tooltip;
    }

    private Component getFluidName() {
        int fluidId = menu.getFluidId();
        if (fluidId < 0) {
            return Component.translatable("tooltip.eon_lazy_patch.experience_infuser.empty");
        }

        Fluid fluid = BuiltInRegistries.FLUID.byId(fluidId);
        return Component.translatable(fluid.getFluidType().getDescriptionId());
    }
}
