package com.voltaire.dugeons.dungeons_and_arcanus.item;

import com.voltaire.dugeons.dungeons_and_arcanus.Dungeons_and_arcanus;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;



public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Dungeons_and_arcanus.MOD_ID);
    //example item register
    //public static final RegistryObject<Item> CCC = ITEMS.register("ccc",
            //() -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
