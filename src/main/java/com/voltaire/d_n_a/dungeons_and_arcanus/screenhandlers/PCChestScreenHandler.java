package com.voltaire.d_n_a.dungeons_and_arcanus.screenhandlers;

import com.voltaire.d_n_a.dungeons_and_arcanus.registry.PCScreenHandlerType;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;


public class PCChestScreenHandler extends AbstractContainerMenu {
   private static final int columns = 9;
   private final Container inventory;
   private final int rows = 4;

   public PCChestScreenHandler(int syncId, Inventory playerInventory) {
      this(PCScreenHandlerType.PC_CHEST.get(), syncId, playerInventory, new SimpleContainer(36));
   }

   public static PCChestScreenHandler createScreenHandler(int syncId, Inventory playerInventory, Container inventory) {
      return new PCChestScreenHandler(PCScreenHandlerType.PC_CHEST.get(), syncId, playerInventory, inventory);
   }

   public PCChestScreenHandler(MenuType<?> type, int syncId, Inventory playerInventory, Container inventory) {
      super(type, syncId);
      checkContainerSize(inventory, 36);
      this.inventory = inventory;
      inventory.startOpen(playerInventory.player);
      int i = (4 - 4) * 18;

      for (int j = 0; j < 4; j++) {
         for (int k = 0; k < 9; k++) {
            this.addSlot(new Slot(inventory, k + j * 9, 8 + k * 18, 18 + j * 18));
         }
      }

      for (int j = 0; j < 3; j++) {
         for (int k = 0; k < 9; k++) {
            this.addSlot(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, 103 + j * 18 + i));
         }
      }

      for (int j = 0; j < 9; j++) {
         this.addSlot(new Slot(playerInventory, j, 8 + j * 18, 161 + i));
      }
   }

   public ItemStack quickMoveStack(Player player, int index) {
      ItemStack itemStack = ItemStack.EMPTY;
      Slot slot = (Slot)this.slots.get(index);
      if (slot != null && slot.hasItem()) {
         ItemStack itemStack2 = slot.getItem();
         itemStack = itemStack2.copy();
         if (index < 4 * 9) {
            if (!this.moveItemStackTo(itemStack2, 4 * 9, this.slots.size(), true)) {
               return ItemStack.EMPTY;
            }
         } else if (!this.moveItemStackTo(itemStack2, 0, 4 * 9, false)) {
            return ItemStack.EMPTY;
         }

         if (itemStack2.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
         } else {
            slot.setChanged();
         }
      }

      return itemStack;
   }

   public boolean stillValid(Player player) {
      return this.inventory.stillValid(player);
   }

   public void removed(Player player) {
      super.removed(player);
      this.inventory.stopOpen(player);
   }

   public Container getInventory() {
      return this.inventory;
   }

   public int getRows() {
      return 4;
   }
}
