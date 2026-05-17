package com.eon.lazy_patch.client.gui;

import com.eon.lazy_patch.EonLazyPatch;
import com.eon.lazy_patch.menu.ConstantGeneratorMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ConstantGeneratorScreen extends AbstractContainerScreen<ConstantGeneratorMenu> {
    private static final ResourceLocation MEKANISM_BASE = ResourceLocation.fromNamespaceAndPath(
            EonLazyPatch.MODID,
            "textures/gui/vendor/mekanism/base.png"
    );
    private static final ResourceLocation MEKANISM_SLOT = ResourceLocation.fromNamespaceAndPath(
            EonLazyPatch.MODID,
            "textures/gui/vendor/mekanism/slot/normal.png"
    );
    private static final ResourceLocation MEKANISM_POWER_SLOT = ResourceLocation.fromNamespaceAndPath(
            EonLazyPatch.MODID,
            "textures/gui/vendor/mekanism/slot/power.png"
    );
    private static final ResourceLocation MEKANISM_ENERGY_ICON = ResourceLocation.fromNamespaceAndPath(
            EonLazyPatch.MODID,
            "textures/gui/vendor/mekanism/energy.png"
    );
    private static final ResourceLocation MEKANISM_INNER = ResourceLocation.fromNamespaceAndPath(
            EonLazyPatch.MODID,
            "textures/gui/vendor/mekanism/inner_screen.png"
    );
    private static final ResourceLocation MEKANISM_VERTICAL_POWER = ResourceLocation.fromNamespaceAndPath(
            EonLazyPatch.MODID,
            "textures/gui/vendor/mekanism/bar/vertical_power.png"
    );

    private static final int ENERGY_SLOT_X = 128;
    private static final int ENERGY_SLOT_Y = 35;
    private static final int INFO_SCREEN_X = 36;
    private static final int INFO_SCREEN_Y = 21;
    private static final int INFO_SCREEN_WIDTH = 80;
    private static final int INFO_SCREEN_HEIGHT = 44;
    private static final int POWER_BAR_X = 160;
    private static final int POWER_BAR_Y = 15;
    private static final int POWER_BAR_WIDTH = 4;
    private static final int POWER_BAR_HEIGHT = 52;
    private static final int PANEL_TEXT_COLOR = 0xFF4A4A4A;
    private static final int INFO_TEXT_COLOR = 0xFF34FF9A;
    private static final float INFO_TEXT_SCALE = 0.8F;

    public ConstantGeneratorScreen(ConstantGeneratorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        imageWidth = 176;
        imageHeight = 166;
        inventoryLabelY = 74;
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);

        if (isHovering(INFO_SCREEN_X, INFO_SCREEN_Y, INFO_SCREEN_WIDTH, INFO_SCREEN_HEIGHT, mouseX, mouseY)) {
            guiGraphics.renderComponentTooltip(font, energyTooltip(), mouseX, mouseY);
        }
        if (isHovering(POWER_BAR_X - 2, POWER_BAR_Y - 2, POWER_BAR_WIDTH + 4, POWER_BAR_HEIGHT + 4, mouseX, mouseY)) {
            guiGraphics.renderComponentTooltip(font, energyTooltip(), mouseX, mouseY);
        }
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int left = leftPos;
        int top = topPos;
        drawBasePanel(guiGraphics, left, top);

        drawEnergyScreen(guiGraphics, left + INFO_SCREEN_X, top + INFO_SCREEN_Y);
        drawPowerSlot(guiGraphics, left + ENERGY_SLOT_X, top + ENERGY_SLOT_Y);
        drawPowerBar(guiGraphics, left + POWER_BAR_X, top + POWER_BAR_Y);
        drawPlayerInventorySlots(guiGraphics, left, top);
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(font, title, (imageWidth - font.width(title)) / 2, titleLabelY, PANEL_TEXT_COLOR, false);
        guiGraphics.drawString(font, playerInventoryTitle, inventoryLabelX, inventoryLabelY, PANEL_TEXT_COLOR, false);
    }

    private void drawBasePanel(GuiGraphics guiGraphics, int x, int y) {
        int border = 4;
        int innerWidth = imageWidth - border * 2;
        int innerHeight = imageHeight - border * 2;
        int sourceEdge = 256 - border;

        guiGraphics.blit(MEKANISM_BASE, x, y, 0, 0, border, border, 256, 256);
        guiGraphics.blit(MEKANISM_BASE, x + border, y, border, 0, innerWidth, border, 256, 256);
        guiGraphics.blit(MEKANISM_BASE, x + imageWidth - border, y, sourceEdge, 0, border, border, 256, 256);

        guiGraphics.blit(MEKANISM_BASE, x, y + border, 0, border, border, innerHeight, 256, 256);
        guiGraphics.blit(MEKANISM_BASE, x + border, y + border, border, border, innerWidth, innerHeight, 256, 256);
        guiGraphics.blit(MEKANISM_BASE, x + imageWidth - border, y + border, sourceEdge, border, border, innerHeight, 256, 256);

        guiGraphics.blit(MEKANISM_BASE, x, y + imageHeight - border, 0, sourceEdge, border, border, 256, 256);
        guiGraphics.blit(MEKANISM_BASE, x + border, y + imageHeight - border, border, sourceEdge, innerWidth, border, 256, 256);
        guiGraphics.blit(MEKANISM_BASE, x + imageWidth - border, y + imageHeight - border, sourceEdge, sourceEdge, border, border, 256, 256);
    }

    private void drawSlot(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.blit(MEKANISM_SLOT, x, y, 0, 0, 18, 18, 18, 18);
    }

    private void drawPowerSlot(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.blit(MEKANISM_POWER_SLOT, x, y, 0, 0, 18, 18, 18, 18);
        guiGraphics.blit(MEKANISM_ENERGY_ICON, x, y, 0, 0, 18, 18, 18, 18);
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

    private void drawEnergyScreen(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.blit(MEKANISM_INNER, x, y, 0, 0, INFO_SCREEN_WIDTH, INFO_SCREEN_HEIGHT, 256, 256);
        drawScaledInfoLines(guiGraphics, List.of(
                Component.literal(formatEnergy(menu.getEnergyStored()) + " / " + formatEnergy(menu.getEnergyCapacity()) + " FE"),
                Component.literal("\u53d1\u7535: " + formatEnergy(menu.getGenerationRate()) + " FE/t"),
                Component.literal("\u8f93\u51fa: " + formatEnergy(menu.getOutputRate()) + " FE/t")
        ), x + 5, y + 7);
    }

    private void drawScaledInfoLines(GuiGraphics guiGraphics, List<Component> lines, int x, int y) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(INFO_TEXT_SCALE, INFO_TEXT_SCALE, 1.0F);
        int scaledX = Math.round(x / INFO_TEXT_SCALE);
        int scaledY = Math.round(y / INFO_TEXT_SCALE);
        for (int line = 0; line < lines.size(); line++) {
            guiGraphics.drawString(font, lines.get(line), scaledX, scaledY + line * 14, INFO_TEXT_COLOR, false);
        }
        guiGraphics.pose().popPose();
    }

    private void drawPowerBar(GuiGraphics guiGraphics, int x, int y) {
        int capacity = Math.max(1, menu.getEnergyCapacity());
        int energy = Math.clamp(menu.getEnergyStored(), 0, capacity);

        guiGraphics.fill(x - 2, y - 2, x + POWER_BAR_WIDTH + 2, y + POWER_BAR_HEIGHT + 2, 0xFF373737);
        guiGraphics.fill(x + POWER_BAR_WIDTH + 1, y - 1, x + POWER_BAR_WIDTH + 2, y + POWER_BAR_HEIGHT + 2, 0xFFFFFFFF);
        guiGraphics.fill(x - 1, y + POWER_BAR_HEIGHT + 1, x + POWER_BAR_WIDTH + 2, y + POWER_BAR_HEIGHT + 2, 0xFFFFFFFF);
        guiGraphics.fill(x - 1, y - 1, x + POWER_BAR_WIDTH + 1, y + POWER_BAR_HEIGHT + 1, 0xFF5B5B5B);
        guiGraphics.fill(x, y, x + POWER_BAR_WIDTH, y + POWER_BAR_HEIGHT, 0xFF404040);

        int fillHeight = energy * POWER_BAR_HEIGHT / capacity;
        if (fillHeight > 0) {
            guiGraphics.blit(MEKANISM_VERTICAL_POWER, x, y + POWER_BAR_HEIGHT - fillHeight, 0, POWER_BAR_HEIGHT - fillHeight, POWER_BAR_WIDTH, fillHeight, POWER_BAR_WIDTH, POWER_BAR_HEIGHT);
        }
    }

    private List<Component> energyTooltip() {
        List<Component> tooltip = new ArrayList<>();
        tooltip.add(Component.translatable("tooltip.eon_lazy_patch.constant_generator.energy").withStyle(ChatFormatting.YELLOW));
        tooltip.add(Component.literal("\u53d1\u7535: " + formatEnergy(menu.getGenerationRate()) + " FE/t").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("\u8f93\u51fa: " + formatEnergy(menu.getOutputRate()) + " FE/t").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal(String.format(Locale.ROOT, "%,d / %,d FE", menu.getEnergyStored(), menu.getEnergyCapacity()))
                .withStyle(ChatFormatting.GRAY));
        return tooltip;
    }

    private static String formatEnergy(int energy) {
        if (energy >= 1_000_000_000) {
            return String.format(Locale.ROOT, "%.2fB", energy / 1_000_000_000.0);
        }
        if (energy >= 1_000_000) {
            return String.format(Locale.ROOT, "%.1fM", energy / 1_000_000.0);
        }
        if (energy >= 1_000) {
            return String.format(Locale.ROOT, "%.1fk", energy / 1_000.0);
        }
        return Integer.toString(energy);
    }
}
