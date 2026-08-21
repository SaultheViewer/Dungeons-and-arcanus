package com.voltaire.d_n_a.dungeons_and_arcanus.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.screenhandlers.PCMimicScreenHandler;

public class PCMimicScreen extends AbstractContainerScreen<PCMimicScreenHandler> {
   private static final ResourceLocation TEXTURE = ProbablyChests.id("textures/gui/pc_mimic_gui.png");
   private final int rows;

   public PCMimicScreen(PCMimicScreenHandler handler, Inventory inventory, Component title) {
      super(handler, inventory, title);
      this.rows = handler.getRows();
      this.imageHeight = 114 + this.rows * 18;
      this.inventoryLabelY = this.imageHeight - 94;
   }

   protected void init() {
      super.init();
      this.titleLabelX = 8;
      this.inventoryLabelY = this.imageHeight - 94;
   }

   protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
      int x = (this.width - this.imageWidth) / 2;
      int y = (this.height - this.imageHeight) / 2;
      guiGraphics.blit(TEXTURE, x, y, 0, 0, this.imageWidth, this.rows * 18 + 17);
      guiGraphics.blit(TEXTURE, x, y + this.rows * 18 + 17, 0, 126, this.imageWidth, 96);
   }

   public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
      this.renderBackground(guiGraphics);
      super.render(guiGraphics, mouseX, mouseY, partialTick);
      this.renderTooltip(guiGraphics, mouseX, mouseY);
   }
}
