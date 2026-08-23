package com.voltaire.d_n_a.dungeons_and_arcanus.registry;

import com.voltaire.d_n_a.dungeons_and_arcanus.Dungeons_and_arcanus;
import com.voltaire.d_n_a.dungeons_and_arcanus.Entity.ChestMimicPet;
import com.voltaire.d_n_a.dungeons_and_arcanus.Entity.PCChestMimic;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntitys {
   public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Dungeons_and_arcanus.MOD_ID);

   public static final RegistryObject<EntityType<PCChestMimic>> NORMAL_CHEST_MIMIC = ENTITY_TYPES.register(
           "normal_chest_mimic",
           () -> EntityType.Builder.of(PCChestMimic::new, MobCategory.MONSTER)
                   .sized(0.9F, 0.9F)
                   .build("d_n_a:normal_chest_mimic")
   );

   public static final RegistryObject<EntityType<PCChestMimic>> LUSH_CHEST_MIMIC = ENTITY_TYPES.register(
           "lush_chest_mimic",
           () -> EntityType.Builder.of(PCChestMimic::new, MobCategory.MONSTER)
                   .sized(0.9F, 0.9F)
                   .build("d_n_a:lush_chest_mimic")
   );

   public static final RegistryObject<EntityType<PCChestMimic>> AZURE_CHEST_MIMIC = ENTITY_TYPES.register(
           "azure_chest_mimic",
           () -> EntityType.Builder.of(PCChestMimic::new, MobCategory.MONSTER)
                   .sized(0.9F, 0.9F)
                   .build("d_n_a:azure_chest_mimic")
   );

   public static final RegistryObject<EntityType<PCChestMimic>> STONE_CHEST_MIMIC = ENTITY_TYPES.register(
           "stone_chest_mimic",
           () -> EntityType.Builder.of(PCChestMimic::new, MobCategory.MONSTER)
                   .sized(0.9F, 0.9F)
                   .build("d_n_a:stone_chest_mimic")
   );

   public static final RegistryObject<EntityType<PCChestMimic>> GOLD_CHEST_MIMIC = ENTITY_TYPES.register(
           "gold_chest_mimic",
           () -> EntityType.Builder.of(PCChestMimic::new, MobCategory.MONSTER)
                   .sized(0.9F, 0.9F)
                   .build("d_n_a:gold_chest_mimic")
   );

   public static final RegistryObject<EntityType<PCChestMimic>> NETHER_CHEST_MIMIC = ENTITY_TYPES.register(
           "nether_chest_mimic",
           () -> EntityType.Builder.of(PCChestMimic::new, MobCategory.MONSTER)
                   .sized(0.9F, 0.9F)
                   .build("d_n_a:nether_chest_mimic")
   );

   public static final RegistryObject<EntityType<PCChestMimic>> SHADOW_CHEST_MIMIC = ENTITY_TYPES.register(
           "shadow_chest_mimic",
           () -> EntityType.Builder.of(PCChestMimic::new, MobCategory.MONSTER)
                   .sized(0.9F, 0.9F)
                   .build("d_n_a:shadow_chest_mimic")
   );

   public static final RegistryObject<EntityType<PCChestMimic>> ICE_CHEST_MIMIC = ENTITY_TYPES.register(
           "ice_chest_mimic",
           () -> EntityType.Builder.of(PCChestMimic::new, MobCategory.MONSTER)
                   .sized(0.9F, 0.9F)
                   .build("d_n_a:ice_chest_mimic")
   );

   public static final RegistryObject<EntityType<PCChestMimic>> CORAL_CHEST_MIMIC = ENTITY_TYPES.register(
           "coral_chest_mimic",
           () -> EntityType.Builder.of(PCChestMimic::new, MobCategory.MONSTER)
                   .sized(0.9F, 0.9F)
                   .build("d_n_a:coral_chest_mimic")
   );

   public static final RegistryObject<EntityType<ChestMimicPet>> NORMAL_CHEST_MIMIC_PET = ENTITY_TYPES.register(
           "normal_chest_mimic_pet",
           () -> EntityType.Builder.of(ChestMimicPet::new, MobCategory.CREATURE)
                   .sized(0.9F, 0.9F)
                   .build("d_n_a:normal_chest_mimic_pet")
   );

   public static final RegistryObject<EntityType<ChestMimicPet>> LUSH_CHEST_MIMIC_PET = ENTITY_TYPES.register(
           "lush_chest_mimic_pet",
           () -> EntityType.Builder.of(ChestMimicPet::new, MobCategory.CREATURE)
                   .sized(0.9F, 0.9F)
                   .build("d_n_a:lush_chest_mimic_pet")
   );

   public static final RegistryObject<EntityType<ChestMimicPet>> AZURE_CHEST_MIMIC_PET = ENTITY_TYPES.register(
           "azure_chest_mimic_pet",
           () -> EntityType.Builder.of(ChestMimicPet::new, MobCategory.CREATURE)
                   .sized(0.9F, 0.9F)
                   .build("d_n_a:azure_chest_mimic_pet")
   );

   public static final RegistryObject<EntityType<ChestMimicPet>> STONE_CHEST_MIMIC_PET = ENTITY_TYPES.register(
           "stone_chest_mimic_pet",
           () -> EntityType.Builder.of(ChestMimicPet::new, MobCategory.CREATURE)
                   .sized(0.9F, 0.9F)
                   .build("d_n_a:stone_chest_mimic_pet")
   );

   public static final RegistryObject<EntityType<ChestMimicPet>> GOLD_CHEST_MIMIC_PET = ENTITY_TYPES.register(
           "gold_chest_mimic_pet",
           () -> EntityType.Builder.of(ChestMimicPet::new, MobCategory.CREATURE)
                   .sized(0.9F, 0.9F)
                   .build("d_n_a:gold_chest_mimic_pet")
   );

   public static final RegistryObject<EntityType<ChestMimicPet>> NETHER_CHEST_MIMIC_PET = ENTITY_TYPES.register(
           "nether_chest_mimic_pet",
           () -> EntityType.Builder.of(ChestMimicPet::new, MobCategory.CREATURE)
                   .sized(0.9F, 0.9F)
                   .build("d_n_a:nether_chest_mimic_pet")
   );

   public static final RegistryObject<EntityType<ChestMimicPet>> SHADOW_CHEST_MIMIC_PET = ENTITY_TYPES.register(
           "shadow_chest_mimic_pet",
           () -> EntityType.Builder.of(ChestMimicPet::new, MobCategory.CREATURE)
                   .sized(0.9F, 0.9F)
                   .build("d_n_a:shadow_chest_mimic_pet")
   );

   public static final RegistryObject<EntityType<ChestMimicPet>> ICE_CHEST_MIMIC_PET = ENTITY_TYPES.register(
           "ice_chest_mimic_pet",
           () -> EntityType.Builder.of(ChestMimicPet::new, MobCategory.CREATURE)
                   .sized(0.9F, 0.9F)
                   .build("d_n_a:ice_chest_mimic_pet")
   );

   public static final RegistryObject<EntityType<ChestMimicPet>> CORAL_CHEST_MIMIC_PET = ENTITY_TYPES.register(
           "coral_chest_mimic_pet",
           () -> EntityType.Builder.of(ChestMimicPet::new, MobCategory.CREATURE)
                   .sized(0.9F, 0.9F)
                   .build("d_n_a:coral_chest_mimic_pet")
   );

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

      event.put(NORMAL_CHEST_MIMIC_PET.get(), ChestMimicPet.createMobAttributes().build());
      event.put(LUSH_CHEST_MIMIC_PET.get(), ChestMimicPet.createMobAttributes().build());
      event.put(AZURE_CHEST_MIMIC_PET.get(), ChestMimicPet.createMobAttributes().build());
      event.put(STONE_CHEST_MIMIC_PET.get(), ChestMimicPet.createMobAttributes().build());
      event.put(GOLD_CHEST_MIMIC_PET.get(), ChestMimicPet.createMobAttributes().build());
      event.put(NETHER_CHEST_MIMIC_PET.get(), ChestMimicPet.createMobAttributes().build());
      event.put(SHADOW_CHEST_MIMIC_PET.get(), ChestMimicPet.createMobAttributes().build());
      event.put(ICE_CHEST_MIMIC_PET.get(), ChestMimicPet.createMobAttributes().build());
      event.put(CORAL_CHEST_MIMIC_PET.get(), ChestMimicPet.createMobAttributes().build());
   }

   public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
      event.register(NORMAL_CHEST_MIMIC.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, PCChestMimic::canSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
      event.register(LUSH_CHEST_MIMIC.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, PCChestMimic::canSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
      event.register(AZURE_CHEST_MIMIC.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, PCChestMimic::canSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
      event.register(STONE_CHEST_MIMIC.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, PCChestMimic::canSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
      event.register(GOLD_CHEST_MIMIC.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, PCChestMimic::canSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
      event.register(NETHER_CHEST_MIMIC.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, PCChestMimic::canSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
      event.register(SHADOW_CHEST_MIMIC.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, PCChestMimic::canSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
      event.register(ICE_CHEST_MIMIC.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, PCChestMimic::canSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
      event.register(CORAL_CHEST_MIMIC.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, PCChestMimic::canSpawn, SpawnPlacementRegisterEvent.Operation.REPLACE);
   }

}