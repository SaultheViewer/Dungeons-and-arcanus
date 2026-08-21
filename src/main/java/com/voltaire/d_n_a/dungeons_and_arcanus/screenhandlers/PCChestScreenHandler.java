package com.voltaire.d_n_a.dungeons_and_arcanus.screenhandlers;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.cloudwarp.probablychests.registry.PCScreenHandlerType;

import java.util.Objects;

public class PCChestScreenHandler extends AbstractContainerMenu {
   private static final int columns = 9;
   private final Container inventory;
   private final int rows;

   public PCChestScreenHandler(int syncId, Inventory playerInventory) {
      this(PCScreenHandlerType.PC_CHEST, syncId, playerInventory, new SimpleContainer(36));
   }

   public static PCChestScreenHandler createScreenHandler(int syncId, Inventory playerInventory, Container inventory) {
      return new PCChestScreenHandler(PCScreenHandlerType.PC_CHEST, syncId, playerInventory, inventory);
   }

   public PCChestScreenHandler(MenuType<?> type, int syncId, Inventory playerInventory, Container inventory) {
      super(type, syncId);
      this.rows = 4;
      checkContainerSize(inventory, 36);
      this.inventory = inventory;
      inventory.startOpen(playerInventory.player);
      Objects.requireNonNull(this);
      int i = (4 - 4) * 18;
      int j = 0;

      while(true) {
         Objects.requireNonNull(this);
         if (j >= 4) {
            for(int j = 0; j < 3; ++j) {
               for(int k = 0; k < 9; ++k) {
                  this.addSlot(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, 103 + j * 18 + i));
               }
            }

            for(int j = 0; j < 9; ++j) {
               this.addSlot(new Slot(playerInventory, j, 8 + j * 18, 161 + i));
            }

            return;
         }

         for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(inventory, k + j * 9, 8 + k * 18, 18 + j * 18));
         }

         ++j;
      }
   }

   public ItemStack quickMoveStack(Player player, int index) {
      ItemStack itemStack = ItemStack.EMPTY;
      Slot slot = (Slot)this.slots.get(index);
      if (slot != null && slot.hasItem()) {
         ItemStack itemStack2 = slot.getItem();
         itemStack = itemStack2.copy();
         Objects.requireNonNull(this);
         if (index < 4 * 9) {
            Objects.requireNonNull(this);
            if (!this.moveItemStackTo(itemStack2, 4 * 9, this.slots.size(), true)) {
               return ItemStack.EMPTY;
            }
         } else {
            Objects.requireNonNull(this);
            if (!this.moveItemStackTo(itemStack2, 0, 4 * 9, false)) {
               return ItemStack.EMPTY;
            }
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
      Objects.requireNonNull(this);
      return 4;
   }
}
