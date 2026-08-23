package com.voltaire.d_n_a.dungeons_and_arcanus.registry;

import com.voltaire.d_n_a.dungeons_and_arcanus.Dungeons_and_arcanus;
import com.voltaire.d_n_a.dungeons_and_arcanus.screenhandlers.PCChestScreenHandler;
import com.voltaire.d_n_a.dungeons_and_arcanus.screenhandlers.PCMimicScreenHandler;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class PCScreenHandlerType {
   public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Dungeons_and_arcanus.MOD_ID);

   public static final RegistryObject<MenuType<PCChestScreenHandler>> PC_CHEST = MENUS.register(
           "pc_chest_screen_handler", () -> new MenuType<>(PCChestScreenHandler::new, FeatureFlagSet.of())
   );

   public static final RegistryObject<MenuType<PCMimicScreenHandler>> PC_CHEST_MIMIC = MENUS.register(
           "pc_chest_mimic_screen_handler", () -> IForgeMenuType.create(PCMimicScreenHandler::createScreenHandler)
   );
}
