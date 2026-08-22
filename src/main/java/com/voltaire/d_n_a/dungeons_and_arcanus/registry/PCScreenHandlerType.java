package com.voltaire.d_n_a.dungeons_and_arcanus.registry;

import com.voltaire.d_n_a.dungeons_and_arcanus.Dungeons_and_arcanus;
import com.voltaire.d_n_a.dungeons_and_arcanus.screenhandlers.PCChestScreenHandler;
import com.voltaire.d_n_a.dungeons_and_arcanus.screenhandlers.PCMimicScreenHandler;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.MenuType;


public class PCScreenHandlerType {
   public static MenuType<PCChestScreenHandler> PC_CHEST;
   public static MenuType<PCMimicScreenHandler> PC_CHEST_MIMIC;

   public static void registerScreenHandlers() {
      PC_CHEST = (MenuType)Registry.register(BuiltInRegistries.MENU, Dungeons_and_arcanus.id("pc_chest_screen_handler"), new MenuType(PCChestScreenHandler::new, FeatureFlagSet.of()));
      PC_CHEST_MIMIC = (MenuType)Registry.register(BuiltInRegistries.MENU, Dungeons_and_arcanus.id("pc_chest_mimic_screen_handler"), new MenuType(PCMimicScreenHandler::new, FeatureFlagSet.of()));
   }
}
