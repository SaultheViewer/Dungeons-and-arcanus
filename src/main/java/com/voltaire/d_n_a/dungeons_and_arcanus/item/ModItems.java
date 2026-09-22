package com.voltaire.d_n_a.dungeons_and_arcanus.item;

import com.voltaire.d_n_a.dungeons_and_arcanus.Dungeons_and_arcanus;
import com.voltaire.d_n_a.dungeons_and_arcanus.item.custom.*;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Dungeons_and_arcanus.MOD_ID);
    public static final RegistryObject<Item> MITHRIL_INGOT = ITEMS.register("mithril_ingot",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SILVER_INGOT = ITEMS.register("silver_ingot",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ORICALCIUM_GEM = ITEMS.register("oricalcium_gem",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ORICALCIUM_PASTE = ITEMS.register("oricalcium_paste",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RAW_MITHRIL = ITEMS.register("raw_mithril",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RAW_SILVER = ITEMS.register("raw_silver",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RUBY = ITEMS.register("ruby",
            () -> new Item(new Item.Properties()));

    //PC ITEMS
    public static final RegistryObject<Item> GOLD_LOCK = ITEMS.register("gold_lock",
            () -> new Gold_Lock(new Item.Properties()));
    public static final RegistryObject<Item> GOLD_KEY = ITEMS.register("gold_key",
            () -> new Gold_key(new Item.Properties()));
    public static final RegistryObject<Item> IRON_KEY = ITEMS.register("iron_key",
            () -> new Iron_key(new Item.Properties()));
    public static final RegistryObject<Item> IRON_LOCK = ITEMS.register("iron_lock",
            () -> new Iron_Lock(new Item.Properties()));
    public static final RegistryObject<Item> MIMIC_CORE = ITEMS.register("mimic_core",
            () -> new Mimic_Core(new Item.Properties()));
    public static final RegistryObject<Item> MIMIC_HAND_BELL = ITEMS.register("mimic_hand_bell",
            () -> new Mimic_Hand_Bell(new Item.Properties()));
    public static final RegistryObject<Item> MIMIC_KEY = ITEMS.register("mimic_key",
            () -> new Mimic_key(new Item.Properties()));
    public static final RegistryObject<Item> VOID_KEY = ITEMS.register("void_key",
            () -> new Void_key(new Item.Properties()));
    public static final RegistryObject<Item> PET_MIMIC_KEY = ITEMS.register("pet_mimic_key",
            () -> new Pet_Mimic_key(new Item.Properties()));
    public static final RegistryObject<Item> VOID_LOCK = ITEMS.register("void_lock",
            () -> new Void_Lock(new Item.Properties()));
    public static final RegistryObject<Item> MIMIC_KEY_FRAGMENT = ITEMS.register("mimic_key_fragment",
            () -> new Item(new Item.Properties()));



    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
