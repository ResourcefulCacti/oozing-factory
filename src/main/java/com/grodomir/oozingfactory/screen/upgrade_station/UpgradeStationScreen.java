package com.grodomir.oozingfactory.screen.upgrade_station;

import com.grodomir.oozingfactory.OozingFactoryMod;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class UpgradeStationScreen extends AbstractContainerScreen<UpgradeStationMenu> {
    private static final ResourceLocation GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(OozingFactoryMod.MODID, "textures/gui/upgrade_station/upgrade_station_gui.png");
    private static final ResourceLocation ARROW_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(OozingFactoryMod.MODID, "textures/gui/basic_sieve/arrow_progress.png");

    private static final ResourceLocation OVERWORLD_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(OozingFactoryMod.MODID, "textures/gui/upgrade_station/overworld.png");
    private static final ResourceLocation NETHER_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(OozingFactoryMod.MODID, "textures/gui/upgrade_station/nether.png");
    private static final ResourceLocation END_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(OozingFactoryMod.MODID, "textures/gui/upgrade_station/end.png");


    public UpgradeStationScreen(UpgradeStationMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        String status = menu.blockEntity.getOverworld_energy() + "/" + "1000";
        String text = "Ov. En.:";

        guiGraphics.blit(GUI_TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
        guiGraphics.drawString(this.font, text, x+5, y+50, 0x404040, false);
        guiGraphics.drawString(this.font, status, x+5, y+60, 0x404040, false);
        guiGraphics.drawString(this.font, "Test", x+130, y+60, 0x404040, false);
        guiGraphics.drawString(this.font, menu.blockEntity.feEnergyStorage.getEnergyStored() +" FE", x+130, y+70, 0x404040, false);
        renderProgressArror(guiGraphics, x, y);
        renderOverworldEnergy(guiGraphics, x, y);
    }

    private void renderProgressArror(GuiGraphics guiGraphics, int x, int y) {
        if(menu.isCrafting()){
            guiGraphics.blit(ARROW_TEXTURE, x + 73, y + 35, 0, 0, menu.getScaledArrowProgres(), 16, 24, 16);
        }
    }

    private void renderOverworldEnergy(GuiGraphics guiGraphics, int x, int y){
            int fillHeight = menu.getOverworldProgress();
            //System.out.println("Height: " + fillHeight);
            guiGraphics.blit(OVERWORLD_TEXTURE, x + 56, y + 37, 0, 0, 2, fillHeight, 2, 35);

    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
