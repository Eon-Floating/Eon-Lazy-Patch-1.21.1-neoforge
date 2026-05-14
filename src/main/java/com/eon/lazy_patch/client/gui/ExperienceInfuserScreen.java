package com.eon.lazy_patch.client.gui;

import com.eon.lazy_patch.item.custom.ExperienceCrystalItem;
import com.eon.lazy_patch.menu.ExperienceInfuserMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ExperienceInfuserScreen extends AbstractContainerScreen<ExperienceInfuserMenu> {
    private static final int PANEL_COLOR = 0xFFBFC5D1;
    private static final int BORDER_COLOR = 0xFF30343A;
    private static final int SLOT_COLOR = 0xFF8E97A5;
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
        guiGraphics.fill(left, top, left + imageWidth, top + imageHeight, PANEL_COLOR);
        guiGraphics.fill(left, top, left + imageWidth, top + 1, BORDER_COLOR);
        guiGraphics.fill(left, top + imageHeight - 1, left + imageWidth, top + imageHeight, BORDER_COLOR);
        guiGraphics.fill(left, top, left + 1, top + imageHeight, BORDER_COLOR);
        guiGraphics.fill(left + imageWidth - 1, top, left + imageWidth, top + imageHeight, BORDER_COLOR);

        drawSlot(guiGraphics, left + 79, top + 34);
        drawFluidTank(guiGraphics, left + 26, top + 20);
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(font, title, titleLabelX, titleLabelY, 0x30343A, false);
        guiGraphics.drawString(font, playerInventoryTitle, inventoryLabelX, inventoryLabelY, 0x30343A, false);

        ItemStack crystal = menu.getCrystalStack();
        if (crystal.getItem() instanceof ExperienceCrystalItem) {
            int storedXp = ExperienceCrystalItem.getStoredXp(crystal);
            String text = storedXp + " / " + ExperienceCrystalItem.MAX_STORED_XP + " XP";
            guiGraphics.drawString(font, text, 104 - font.width(text) / 2, 38, 0x30343A, false);
        }
    }

    private void drawSlot(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.fill(x, y, x + 18, y + 18, BORDER_COLOR);
        guiGraphics.fill(x + 1, y + 1, x + 17, y + 17, SLOT_COLOR);
    }

    private void drawFluidTank(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.fill(x - 1, y - 1, x + 15, y + 49, BORDER_COLOR);
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
        tooltip.add(Component.translatable("tooltip.eon_lazy_patch.experience_infuser.fluid")
                .withStyle(ChatFormatting.GREEN));
        tooltip.add(Component.literal(menu.getFluidAmount() + " / " + menu.getFluidCapacity() + " mB")
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal((menu.getFluidAmount() / ExperienceCrystalItem.MILLIBUCKETS_PER_XP) + " XP")
                .withStyle(ChatFormatting.GRAY));
        return tooltip;
    }
}
