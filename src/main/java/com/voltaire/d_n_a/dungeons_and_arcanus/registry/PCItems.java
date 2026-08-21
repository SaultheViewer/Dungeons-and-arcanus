package com.voltaire.d_n_a.dungeons_and_arcanus.registry;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.cloudwarp.probablychests.ProbablyChests;
import org.cloudwarp.probablychests.item.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class PCItems {
   private static final Map<Item, ResourceLocation> ITEMS = new LinkedHashMap();
   public static final Item MIMIC_KEY = create(new MimicKeyItem((new FabricItemSettings()).maxCount(16)), "mimic_key");
   public static final Item MIMIC_KEY_FRAGMENT = create(new Item(new FabricItemSettings()), "mimic_key_fragment");
   public static final Item MIMIC_CORE = create(new MimicCoreItem((new FabricItemSettings()).maxCount(1)), "mimic_core");
   public static final Item PET_MIMIC_KEY = create(new PetMimicKeyItem((new FabricItemSettings()).maxCount(16)), "pet_mimic_key");
   public static final Item MIMIC_HAND_BELL = create(new MimicHandBellItem((new FabricItemSettings()).maxCount(1)), "mimic_hand_bell");
   public static final Item IRON_KEY = create(new IronKeyItem((new FabricItemSettings()).maxCount(16)), "iron_key");
   public static final Item IRON_LOCK = create(new IronLockItem((new FabricItemSettings()).maxCount(16)), "iron_lock");
   public static final Item GOLD_KEY = create(new GoldKeyItem((new FabricItemSettings()).maxCount(16)), "gold_key");
   public static final Item GOLD_LOCK = create(new GoldLockItem((new FabricItemSettings()).maxCount(16)), "gold_lock");
   public static final Item VOID_KEY = create(new VoidKeyItem((new FabricItemSettings()).maxCount(16)), "void_key");
   public static final Item VOID_LOCK = create(new VoidLockItem((new FabricItemSettings()).maxCount(16)), "void_lock");

   public static void init() {
      ITEMS.keySet().forEach((item) -> Registry.register(BuiltInRegistries.ITEM, (ResourceLocation)ITEMS.get(item), item));
      ItemGroupEvents.modifyEntriesEvent(ProbablyChests.PROBABLY_CHESTS_GROUP).register((ItemGroupEvents.ModifyEntries)(content) -> {
         Set var10000 = ITEMS.keySet();
         Objects.requireNonNull(content);
         var10000.forEach(content::accept);
      });
   }

   public static <T extends Item> T create(T item, String name) {
      ITEMS.put(item, ProbablyChests.id(name));
      return item;
   }
}
