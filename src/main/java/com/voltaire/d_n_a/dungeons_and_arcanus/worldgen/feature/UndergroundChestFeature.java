package com.voltaire.d_n_a.dungeons_and_arcanus.worldgen.feature;

import com.mojang.serialization.Codec;
import com.voltaire.d_n_a.dungeons_and_arcanus.blocks.ModBlocks;
import com.voltaire.d_n_a.dungeons_and_arcanus.blocks.entity.PC_BaseChestBlockEntity;
import com.voltaire.d_n_a.dungeons_and_arcanus.registry.PCProperties;
import com.voltaire.d_n_a.dungeons_and_arcanus.utils.PCLockedState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.Column;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.Optional;

public class UndergroundChestFeature extends Feature<NoneFeatureConfiguration> {

   public UndergroundChestFeature(Codec<NoneFeatureConfiguration> configCodec) {
      super(configCodec);
   }

   @Override
   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
      RandomSource random = context.random();
      WorldGenLevel level = context.level();
      BlockPos pos = context.origin().above();

      BlockState blockToBePlaced = null;
      boolean isWater = level.getBlockState(pos).is(Blocks.WATER);
      boolean isNether = level.dimensionType().ultraWarm();
      boolean hasGoldLock = false;
      PCLockedState lockedState = PCLockedState.UNLOCKED;

      if (isWater) {
         if (random.nextFloat() < 0.85F) {
            return false;
         }
         if (level.getBlockState(pos.above()).isRedstoneConductor(level, pos.above())) {
            return false;
         }

         // Fabric ConventionalBiomeTags.ICY / SNOWY -> Forge tags plus vanilla biome tags
         if (!level.getBiome(pos).is(Tags.Biomes.IS_SNOWY)
                 && !level.getBiome(pos).is(BiomeTags.MINESHAFT_BLOCKING) /* optional */
                 && !isSnowyOrIcy(level, pos)) {
            blockToBePlaced = ModBlocks.CORAL_CHEST.get().defaultBlockState();
         } else {
            blockToBePlaced = ModBlocks.ICE_CHEST.get().defaultBlockState();
         }
      } else {
         Optional<Column> optional = Column.scan(
                 level, pos, 64,
                 UndergroundChestFeature::canGenerate,
                 UndergroundChestFeature::canReplace
         );
         if (optional.isEmpty() || !(optional.get() instanceof Column.Range bounded)) {
            return false;
         }
         if (bounded.height() < 3) {
            return false;
         }
         if (bounded.floor() > pos.getY()) {
            return false;
         }

         if (isNether) {
            blockToBePlaced = ModBlocks.NETHER_CHEST.get().defaultBlockState();
         }

         BlockPos biomeCheckPos = pos.relative(Direction.UP, 5);
         if (random.nextFloat() < 0.85F) {
            if (level.getBiome(biomeCheckPos).is(Biomes.LUSH_CAVES)) {
               blockToBePlaced = ModBlocks.LUSH_CHEST.get().defaultBlockState();
            } else if (level.getBiome(biomeCheckPos).is(Biomes.DRIPSTONE_CAVES)) {
               blockToBePlaced = ModBlocks.AZURE_CHEST.get().defaultBlockState();
            }
         }

         if (blockToBePlaced == null) {
            if (pos.getY() <= 0) {
               if (random.nextFloat() < 0.25F) {
                  blockToBePlaced = ModBlocks.GOLD_CHEST.get().defaultBlockState();
                  hasGoldLock = true;
               } else {
                  blockToBePlaced = ModBlocks.STONE_CHEST.get().defaultBlockState();
               }
            } else if (!isSnowyOrIcy(level, biomeCheckPos)) {
               if (random.nextFloat() < 0.25F) {
                  blockToBePlaced = ModBlocks.LUSH_CHEST.get().defaultBlockState();
               } else {
                  blockToBePlaced = ModBlocks.NORMAL_CHEST.get().defaultBlockState();
               }
            } else {
               blockToBePlaced = ModBlocks.ICE_CHEST.get().defaultBlockState();
            }
         }
      }

      if (hasGoldLock) {
         lockedState = PCLockedState.LOCKED;
      }

      // FabricLoader.isDevelopmentEnvironment() → Forge
      if (!FMLEnvironment.production) {
         BlockPos debugPos = pos;
         for (int i = 0; i < 40; ++i) {
            debugPos = debugPos.above();
            level.setBlock(debugPos, Blocks.END_ROD.defaultBlockState(), 3);
         }
      }

      level.setBlock(
              pos,
              blockToBePlaced
                      .setValue(BlockStateProperties.WATERLOGGED, isWater)
                      .setValue(PCProperties.PC_LOCKED_STATE, lockedState),
              3
      );

      if (level.getBlockEntity(pos) instanceof PC_BaseChestBlockEntity chest) {
         chest.isNatural = true;
         chest.hasGoldLock = hasGoldLock;
         chest.isLocked = hasGoldLock;
      }

      return true;
   }

   /** Stand-in for ConventionalBiomeTags.SNOWY / ICY */
   private static boolean isSnowyOrIcy(WorldGenLevel level, BlockPos pos) {
      return level.getBiome(pos).is(Tags.Biomes.IS_SNOWY)
              || level.getBiome(pos).is(BiomeTags.HAS_IGLOO)
              || level.getBiome(pos).is(BiomeTags.SPAWNS_SNOW_FOXES)
              || level.getBiome(pos).is(BiomeTags.SPAWNS_WHITE_RABBITS);
      // Or use Tags.Biomes / your own tag if you want finer control
   }

   public static boolean canReplace(BlockState state) {
      return state.is(BlockTags.BASE_STONE_OVERWORLD)
              || state.is(BlockTags.DEEPSLATE_ORE_REPLACEABLES)
              || state.is(BlockTags.LUSH_GROUND_REPLACEABLE)
              || state.is(BlockTags.BASE_STONE_NETHER);
   }

   public static boolean canGenerate(BlockState state) {
      return state.isAir() || state.is(Blocks.WATER);
   }
}
