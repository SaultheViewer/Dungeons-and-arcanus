package com.voltaire.d_n_a.dungeons_and_arcanus.registry;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.voltaire.d_n_a.dungeons_and_arcanus.Dungeons_and_arcanus;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntitySpawns {

   public static final DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS =
           DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, Dungeons_and_arcanus.MOD_ID);

   public static final RegistryObject<Codec<? extends BiomeModifier>> CONFIG_SPAWN =
           BIOME_MODIFIER_SERIALIZERS.register("config_spawn", () -> ConfigSpawnBiomeModifier.CODEC);

   public static void register(IEventBus bus) {
      BIOME_MODIFIER_SERIALIZERS.register(bus);
   }

   /** Adds a spawn only if config.mimicSettings.spawnNaturalMimics is true */
   public record ConfigSpawnBiomeModifier(
           HolderSet<Biome> biomes,
           MobCategory category,
           MobSpawnSettings.SpawnerData spawner
   ) implements BiomeModifier {

      public static final Codec<ConfigSpawnBiomeModifier> CODEC = RecordCodecBuilder.create(instance ->
              instance.group(
                      Biome.LIST_CODEC.fieldOf("biomes").forGetter(ConfigSpawnBiomeModifier::biomes),
                      MobCategory.CODEC.fieldOf("category").forGetter(ConfigSpawnBiomeModifier::category),
                      MobSpawnSettings.SpawnerData.CODEC.fieldOf("spawners").forGetter(ConfigSpawnBiomeModifier::spawner)
              ).apply(instance, ConfigSpawnBiomeModifier::new)
      );

      @Override
      public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
         if (phase != Phase.ADD) return;
         if (Dungeons_and_arcanus.loadedConfig == null
                 || !Dungeons_and_arcanus.loadedConfig.mimicSettings.spawnNaturalMimics) {
            return;
         }
         if (this.biomes.contains(biome)) {
            builder.getMobSpawnSettings().addSpawn(this.category, this.spawner);
         }
      }

      @Override
      public Codec<? extends BiomeModifier> codec() {
         return CODEC;
      }
   }
}