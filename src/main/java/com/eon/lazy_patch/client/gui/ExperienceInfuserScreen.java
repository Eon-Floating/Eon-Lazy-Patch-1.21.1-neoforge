package com.eon.lazy_patch.client.gui;

import com.eon.lazy_patch.EonLazyPatch;
import com.eon.lazy_patch.item.custom.ExperienceCrystalItem;
import com.eon.lazy_patch.menu.ExperienceInfuserMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ExperienceInfuserScreen extends AbstractContainerScreen<ExperienceInfuserMenu> {
    private static final ResourceLocation MEKANISM_BASE = ResourceLocation.fromNamespaceAndPath(
            EonLazyPatch.MODID,
            "textures/gui/vendor/mekanism/base.png"
    );
    private static final ResourceLocation MEKANISM_SLOT = ResourceLocation.fromNamespaceAndPath(
            EonLazyPatch.MODID,
            "textures/gui/vendor/mekanism/slot/normal.png"
    );
    private static final int TANK_BACK_COLOR = 0xFF8B8B8B;
    private static final int TANK_X = 34;
    private static final int TANK_Y = 32;
    private static final int TANK_WIDTH = 72;
    private static final int TANK_HEIGHT = 58;
    private static final int TANK_INSET = 1;
    private static final int TANK_TICK_COUNT = 7;
    private static final int TANK_TICK_LENGTH = 10;
    private static final int TANK_BORDER_DARK = 0xFF4D4D4D;
    private static final int TANK_BORDER_LIGHT = 0xFFFFFFFF;
    private static final int TANK_TICK_COLOR = 0xFF202020;

    public ExperienceInfuserScreen(ExperienceInfuserMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        imageWidth = 176;
        imageHeight = 190;
        inventoryLabelY = 96;
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);

        if (isHovering(TANK_X, TANK_Y, TANK_WIDTH, TANK_HEIGHT, mouseX, mouseY)) {
            guiGraphics.renderComponentTooltip(font, fluidTooltip(), mouseX, mouseY);
        }
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int left = leftPos;
        int top = topPos;
        drawBasePanel(guiGraphics, left, top);

        drawSlot(guiGraphics, left + 140, top + 50);
        drawPlayerInventorySlots(guiGraphics, left, top);
        drawFluidTank(guiGraphics, left + TANK_X, top + TANK_Y);
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(font, title, titleLabelX, titleLabelY, 0x404040, false);
        guiGraphics.drawString(font, playerInventoryTitle, inventoryLabelX, inventoryLabelY, 0x404040, false);
    }

    private void drawSlot(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.blit(MEKANISM_SLOT, x, y, 0, 0, 18, 18, 18, 18);
    }

    private void drawPlayerInventorySlots(GuiGraphics guiGraphics, int left, int top) {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                drawSlot(guiGraphics, left + 7 + column * 18, top + 107 + row * 18);
            }
        }

        for (int column = 0; column < 9; column++) {
            drawSlot(guiGraphics, left + 7 + column * 18, top + 165);
        }
    }

    private void drawFluidTank(GuiGraphics guiGraphics, int x, int y) {
        drawTankBackground(guiGraphics, x, y);

        int innerX = x + TANK_INSET;
        int innerY = y + TANK_INSET;
        int innerWidth = TANK_WIDTH - TANK_INSET * 2;
        int innerHeight = TANK_HEIGHT - TANK_INSET * 2;
        guiGraphics.fill(innerX, innerY, innerX + innerWidth, innerY + innerHeight, TANK_BACK_COLOR);

        int capacity = Math.max(1, menu.getFluidCapacity());
        int amount = Math.clamp(menu.getFluidAmount(), 0, capacity);
        int height = amount * innerHeight / capacity;
        if (height > 0) {
            drawFluid(guiGraphics, innerX, innerY + innerHeight - height, innerWidth, height);
        }
        drawTankOverlay(guiGraphics, x, y);
    }

    private void drawTankBackground(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.fill(x, y, x + TANK_WIDTH, y + TANK_HEIGHT, TANK_BACK_COLOR);
    }

    private void drawTankOverlay(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.fill(x, y, x + TANK_WIDTH, y + 1, TANK_BORDER_DARK);
        guiGraphics.fill(x, y, x + 1, y + TANK_HEIGHT, TANK_BORDER_DARK);
        guiGraphics.fill(x + 1, y + TANK_HEIGHT - 1, x + TANK_WIDTH, y + TANK_HEIGHT, TANK_BORDER_LIGHT);
        guiGraphics.fill(x + TANK_WIDTH - 1, y + 1, x + TANK_WIDTH, y + TANK_HEIGHT, TANK_BORDER_LIGHT);
        for (int tick = 1; tick <= TANK_TICK_COUNT; tick++) {
            int tickY = y + tick * TANK_HEIGHT / (TANK_TICK_COUNT + 1);
            guiGraphics.fill(x, tickY, x + TANK_TICK_LENGTH, tickY + 1, TANK_TICK_COLOR);
        }
    }

    private void drawFluid(GuiGraphics guiGraphics, int x, int y, int width, int height) {
        Fluid fluid = getFluid();
        FluidStack fluidStack = new FluidStack(fluid, Math.max(1, menu.getFluidAmount()));
        IClientFluidTypeExtensions fluidClient = IClientFluidTypeExtensions.of(fluid);
        ResourceLocation stillTexture = Objects.requireNonNull(fluidClient.getStillTexture(fluidStack));

        TextureAtlasSprite sprite = Minecraft.getInstance()
                .getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                .apply(stillTexture);
        int tint = fluidClient.getTintColor(fluidStack);
        float alpha = ((tint >>> 24) & 0xFF) / 255.0f;
        float red = ((tint >>> 16) & 0xFF) / 255.0f;
        float green = ((tint >>> 8) & 0xFF) / 255.0f;
        float blue = (tint & 0xFF) / 255.0f;

        for (int drawX = x; drawX < x + width; drawX += 16) {
            for (int drawY = y; drawY < y + height; drawY += 16) {
                int tileWidth = Math.min(16, x + width - drawX);
                int tileHeight = Math.min(16, y + height - drawY);
                guiGraphics.blit(drawX, drawY, 0, tileWidth, tileHeight, sprite, red, green, blue, alpha);
            }
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

        Fluid fluid = getFluid();
        return Component.translatable(fluid.getFluidType().getDescriptionId());
    }

    private Fluid getFluid() {
        return BuiltInRegistries.FLUID.byId(menu.getFluidId());
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
}
