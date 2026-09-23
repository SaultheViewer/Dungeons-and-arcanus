package com.voltaire.d_n_a.dungeons_and_arcanus.world.feature;

import com.mojang.serialization.Codec;
import com.voltaire.d_n_a.dungeons_and_arcanus.blocks.ModBlocks;
import com.voltaire.d_n_a.dungeons_and_arcanus.blocks.entity.PCBaseChestBlockEntity;
import com.voltaire.d_n_a.dungeons_and_arcanus.registry.PCProperties;
import com.voltaire.d_n_a.dungeons_and_arcanus.utils.PCLockedState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.Column;
import net.minecraft.world.level.levelgen.Column.Range;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.loading.FMLLoader;

import java.util.Optional;


public class UndergroundChestFeature extends Feature<NoneFeatureConfiguration> {
   public UndergroundChestFeature(Codec<NoneFeatureConfiguration> configCodec) {
      super(configCodec);
   }

   public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
      RandomSource random = context.random();
      WorldGenLevel structureWorldAccess = context.level();
      BlockPos pos = context.origin().above();
      NoneFeatureConfiguration config = context.config();
      BlockState blockToBePlaced = null;
      boolean isWater = structureWorldAccess.getBlockState(pos).is(Blocks.WATER);
      boolean isNether = structureWorldAccess.dimensionType().ultraWarm();
      boolean hasGoldLock = false;
      PCLockedState lockedState = PCLockedState.UNLOCKED;
      if (isWater) {
         if (random.nextFloat() < 0.85F) {
            return false;
         }

         if (structureWorldAccess.getBlockState(pos.above()).isRedstoneConductor(structureWorldAccess, pos.above())) {
            return false;
         }

         if (!structureWorldAccess.getBiome(pos).is(Tags.Biomes.IS_SNOWY) && !structureWorldAccess.getBiome(pos).is(Tags.Biomes.IS_COLD_OVERWORLD)) {
            blockToBePlaced = ModBlocks.CORAL_CHEST.get().defaultBlockState();
         } else {
            blockToBePlaced = ModBlocks.ICE_CHEST.get().defaultBlockState();
         }
      } else {
         Optional<Column> optional = Column.scan(structureWorldAccess, pos, 64, UndergroundChestFeature::canGenerate, UndergroundChestFeature::canReplace);
         if (!optional.isPresent() || !(optional.get() instanceof Range)) {
            return false;
         }

         Range bounded = (Range)optional.get();
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
            if (structureWorldAccess.getBiome(biomeCheckPos).is(Biomes.LUSH_CAVES)) {
               blockToBePlaced = ModBlocks.LUSH_CHEST.get().defaultBlockState();
            } else if (structureWorldAccess.getBiome(biomeCheckPos).is(Biomes.DRIPSTONE_CAVES)) {
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
            } else if (structureWorldAccess.getBiome(biomeCheckPos).is(Tags.Biomes.IS_SNOWY)
               || structureWorldAccess.getBiome(biomeCheckPos).is(Tags.Biomes.IS_COLD_OVERWORLD)) {
               blockToBePlaced = ModBlocks.ICE_CHEST.get().defaultBlockState();
            } else if (random.nextFloat() < 0.25F) {
               blockToBePlaced = ModBlocks.LUSH_CHEST.get().defaultBlockState();
            } else {
               blockToBePlaced = ModBlocks.NORMAL_CHEST.get().defaultBlockState();
            }
         }
      }

      if (hasGoldLock) {
         lockedState = PCLockedState.LOCKED;
      }

      if (!FMLLoader.isProduction()) {
         BlockPos debugPos = pos;

         for (int i = 0; i < 40; i++) {
            structureWorldAccess.setBlock(debugPos = debugPos.above(), Blocks.END_ROD.defaultBlockState(), 3);
         }
      }

      structureWorldAccess.setBlock(
         pos,
         (BlockState)((BlockState)blockToBePlaced.setValue(BlockStateProperties.WATERLOGGED, isWater)).setValue(PCProperties.PC_LOCKED_STATE, lockedState),
         3
      );
      PCBaseChestBlockEntity chest = (PCBaseChestBlockEntity)structureWorldAccess.getBlockEntity(pos);
      if (chest != null) {
         chest.isNatural = true;
         chest.hasGoldLock = hasGoldLock;
         chest.isLocked = hasGoldLock;
      }

      return true;
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
