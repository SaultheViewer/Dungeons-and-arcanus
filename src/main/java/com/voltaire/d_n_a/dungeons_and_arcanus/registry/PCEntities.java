package com.voltaire.d_n_a.dungeons_and_arcanus.registry;

import com.voltaire.d_n_a.dungeons_and_arcanus.Dungeons_and_arcanus;
import com.voltaire.d_n_a.dungeons_and_arcanus.entity.PCChestMimic;
import com.voltaire.d_n_a.dungeons_and_arcanus.entity.PCChestMimicPet;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = Dungeons_and_arcanus.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PCEntities {

   public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
           DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Dungeons_and_arcanus.MOD_ID);

   // ---------------- Hostile mimics ----------------
   public static final RegistryObject<EntityType<PCChestMimic>> NORMAL_CHEST_MIMIC =
           registerHostile("normal_chest_mimic");
   public static final RegistryObject<EntityType<PCChestMimic>> LUSH_CHEST_MIMIC =
           registerHostile("lush_chest_mimic");
   public static final RegistryObject<EntityType<PCChestMimic>> AZURE_CHEST_MIMIC =
           registerHostile("azure_chest_mimic");
   public static final RegistryObject<EntityType<PCChestMimic>> STONE_CHEST_MIMIC =
           registerHostile("stone_chest_mimic");
   public static final RegistryObject<EntityType<PCChestMimic>> GOLD_CHEST_MIMIC =
           registerHostile("gold_chest_mimic");
   public static final RegistryObject<EntityType<PCChestMimic>> NETHER_CHEST_MIMIC =
           registerHostile("nether_chest_mimic");
   public static final RegistryObject<EntityType<PCChestMimic>> SHADOW_CHEST_MIMIC =
           registerHostile("shadow_chest_mimic");
   public static final RegistryObject<EntityType<PCChestMimic>> ICE_CHEST_MIMIC =
           registerHostile("ice_chest_mimic");
   public static final RegistryObject<EntityType<PCChestMimic>> CORAL_CHEST_MIMIC =
           registerHostile("coral_chest_mimic");

   // ---------------- Pet mimics ----------------
   public static final RegistryObject<EntityType<PCChestMimicPet>> NORMAL_CHEST_MIMIC_PET =
           registerPet("normal_chest_mimic_pet");
   public static final RegistryObject<EntityType<PCChestMimicPet>> LUSH_CHEST_MIMIC_PET =
           registerPet("lush_chest_mimic_pet");
   public static final RegistryObject<EntityType<PCChestMimicPet>> AZURE_CHEST_MIMIC_PET =
           registerPet("azure_chest_mimic_pet");
   public static final RegistryObject<EntityType<PCChestMimicPet>> STONE_CHEST_MIMIC_PET =
           registerPet("stone_chest_mimic_pet");
   public static final RegistryObject<EntityType<PCChestMimicPet>> GOLD_CHEST_MIMIC_PET =
           registerPet("gold_chest_mimic_pet");
   public static final RegistryObject<EntityType<PCChestMimicPet>> NETHER_CHEST_MIMIC_PET =
           registerPet("nether_chest_mimic_pet");
   public static final RegistryObject<EntityType<PCChestMimicPet>> SHADOW_CHEST_MIMIC_PET =
           registerPet("shadow_chest_mimic_pet");
   public static final RegistryObject<EntityType<PCChestMimicPet>> ICE_CHEST_MIMIC_PET =
           registerPet("ice_chest_mimic_pet");
   public static final RegistryObject<EntityType<PCChestMimicPet>> CORAL_CHEST_MIMIC_PET =
           registerPet("coral_chest_mimic_pet");

   private static RegistryObject<EntityType<PCChestMimic>> registerHostile(String name) {
      return ENTITY_TYPES.register(name, () ->
              EntityType.Builder.of(PCChestMimic::new, MobCategory.MONSTER)
                      .sized(0.9F, 0.9F)
                      .clientTrackingRange(8)
                      .build(name));
   }

   private static RegistryObject<EntityType<PCChestMimicPet>> registerPet(String name) {
      return ENTITY_TYPES.register(name, () ->
              EntityType.Builder.of(PCChestMimicPet::new, MobCategory.CREATURE)
                      .sized(0.9F, 0.9F)
                      .clientTrackingRange(8)
                      .build(name));
   }

   public static void register(IEventBus bus) {
      ENTITY_TYPES.register(bus);
   }

   /** Attributes — Forge replacement for FabricDefaultAttributeRegistry */
   @SubscribeEvent
   public static void registerAttributes(EntityAttributeCreationEvent event) {
      event.put(NORMAL_CHEST_MIMIC.get(), PCChestMimic.createMobAttributes().build());
      event.put(LUSH_CHEST_MIMIC.get(), PCChestMimic.createMobAttributes().build());
      event.put(AZURE_CHEST_MIMIC.get(), PCChestMimic.createMobAttributes().build());
      event.put(STONE_CHEST_MIMIC.get(), PCChestMimic.createMobAttributes().build());
      event.put(GOLD_CHEST_MIMIC.get(), PCChestMimic.createMobAttributes().build());
      event.put(NETHER_CHEST_MIMIC.get(), PCChestMimic.createMobAttributes().build());
      event.put(SHADOW_CHEST_MIMIC.get(), PCChestMimic.createMobAttributes().build());
      event.put(ICE_CHEST_MIMIC.get(), PCChestMimic.createMobAttributes().build());
      event.put(CORAL_CHEST_MIMIC.get(), PCChestMimic.createMobAttributes().build());

      event.put(NORMAL_CHEST_MIMIC_PET.get(), PCChestMimicPet.createMobAttributes().build());
      event.put(LUSH_CHEST_MIMIC_PET.get(), PCChestMimicPet.createMobAttributes().build());
      event.put(AZURE_CHEST_MIMIC_PET.get(), PCChestMimicPet.createMobAttributes().build());
      event.put(STONE_CHEST_MIMIC_PET.get(), PCChestMimicPet.createMobAttributes().build());
      event.put(GOLD_CHEST_MIMIC_PET.get(), PCChestMimicPet.createMobAttributes().build());
      event.put(NETHER_CHEST_MIMIC_PET.get(), PCChestMimicPet.createMobAttributes().build());
      event.put(SHADOW_CHEST_MIMIC_PET.get(), PCChestMimicPet.createMobAttributes().build());
      event.put(ICE_CHEST_MIMIC_PET.get(), PCChestMimicPet.createMobAttributes().build());
      event.put(CORAL_CHEST_MIMIC_PET.get(), PCChestMimicPet.createMobAttributes().build());
   }

   /** Spawn restrictions — call from commonSetup enqueueWork */
   public static void registerSpawnPlacements() {
      SpawnPlacements.register(
              NORMAL_CHEST_MIMIC.get(),
              SpawnPlacements.Type.ON_GROUND,
              Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
              PCChestMimic::canSpawn);
      SpawnPlacements.register(
              LUSH_CHEST_MIMIC.get(),
              SpawnPlacements.Type.ON_GROUND,
              Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
              PCChestMimic::canSpawn);
      SpawnPlacements.register(
              AZURE_CHEST_MIMIC.get(),
              SpawnPlacements.Type.ON_GROUND,
              Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
              PCChestMimic::canSpawn);
      SpawnPlacements.register(
              STONE_CHEST_MIMIC.get(),
              SpawnPlacements.Type.ON_GROUND,
              Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
              PCChestMimic::canSpawn);
      SpawnPlacements.register(
              GOLD_CHEST_MIMIC.get(),
              SpawnPlacements.Type.ON_GROUND,
              Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
              PCChestMimic::canSpawn);
      SpawnPlacements.register(
              NETHER_CHEST_MIMIC.get(),
              SpawnPlacements.Type.ON_GROUND,
              Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
              PCChestMimic::canSpawn);
      SpawnPlacements.register(
              SHADOW_CHEST_MIMIC.get(),
              SpawnPlacements.Type.ON_GROUND,
              Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
              PCChestMimic::canSpawn);
      SpawnPlacements.register(
              ICE_CHEST_MIMIC.get(),
              SpawnPlacements.Type.ON_GROUND,
              Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
              PCChestMimic::canSpawn);
      SpawnPlacements.register(
              CORAL_CHEST_MIMIC.get(),
              SpawnPlacements.Type.ON_GROUND,
              Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
              PCChestMimic::canSpawn);
   }
}