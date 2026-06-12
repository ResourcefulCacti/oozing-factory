package com.grodomir.oozingfactory.screen;

import com.grodomir.oozingfactory.OozingFactoryMod;
import com.grodomir.oozingfactory.common.block.custom.BasicSieve;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class BasicSieveScreen extends AbstractContainerScreen<BasicSieveMenu> {
    private static final ResourceLocation GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(OozingFactoryMod.MODID, "textures/gui/basic_sieve/basic_sieve_gui.png");
    private static final ResourceLocation ARROW_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(OozingFactoryMod.MODID, "textures/gui/basic_sieve/arrow_progress.png");

    public BasicSieveScreen(BasicSieveMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    public ItemStack getItemAbove(){
        Level level = menu.blockEntity.getLevel();
        BlockPos pos = menu.blockEntity.getBlockPos();

        return new ItemStack(BasicSieve.getBlockAbove(level, pos).getBlock().asItem());
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(GUI_TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
        guiGraphics.renderItem(getItemAbove(), leftPos + 54, topPos + 34);

        renderProgressArror(guiGraphics, x, y);
    }

    private void renderProgressArror(GuiGraphics guiGraphics, int x, int y) {
        if(menu.isCrafting()){
            guiGraphics.blit(ARROW_TEXTURE, x + 73, y + 35, 0, 0, menu.getScaledArrowProgres(), 16, 24, 16);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);

        int x = leftPos + 54;
        int y = topPos + 34;
        if(mouseX >= x && mouseX < x + 16 && mouseY >= y && mouseY < y + 16){
            guiGraphics.renderTooltip(this.font, getItemAbove(), mouseX, mouseY);
        }
    }
}
