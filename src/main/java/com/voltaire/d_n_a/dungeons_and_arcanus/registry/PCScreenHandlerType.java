package com.voltaire.d_n_a.dungeons_and_arcanus.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.MenuType;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.screenhandlers.PCChestScreenHandler;
import org.cloudwarp.probablychests.screenhandlers.PCMimicScreenHandler;

public class PCScreenHandlerType {
   public static MenuType<PCChestScreenHandler> PC_CHEST;
   public static MenuType<PCMimicScreenHandler> PC_CHEST_MIMIC;

   public static void registerScreenHandlers() {
      PC_CHEST = (MenuType)Registry.register(BuiltInRegistries.MENU, ProbablyChests.id("pc_chest_screen_handler"), new MenuType(PCChestScreenHandler::new, FeatureFlagSet.of()));
      PC_CHEST_MIMIC = (MenuType)Registry.register(BuiltInRegistries.MENU, ProbablyChests.id("pc_chest_mimic_screen_handler"), new MenuType(PCMimicScreenHandler::new, FeatureFlagSet.of()));
   }
}
