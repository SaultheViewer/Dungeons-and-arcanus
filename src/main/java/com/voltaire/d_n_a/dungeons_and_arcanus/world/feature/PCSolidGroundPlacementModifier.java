package com.voltaire.d_n_a.dungeons_and_arcanus.world.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

import java.util.stream.Stream;

public class PCSolidGroundPlacementModifier extends PlacementModifier {
   public static final Codec<PCSolidGroundPlacementModifier> MODIFIER_CODEC = RecordCodecBuilder.create(
      instance -> instance.group(
            BlockPredicate.CODEC.fieldOf("target_condition").forGetter(PCSolidGroundPlacementModifier -> PCSolidGroundPlacementModifier.targetPredicate)
         )
         .apply(instance, PCSolidGroundPlacementModifier::new)
   );
   private final BlockPredicate targetPredicate;

   private PCSolidGroundPlacementModifier(BlockPredicate targetPredicate) {
      this.targetPredicate = targetPredicate;
   }

   public static PCSolidGroundPlacementModifier of(BlockPredicate targetPredicate) {
      return new PCSolidGroundPlacementModifier(targetPredicate);
   }

   public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos pos) {
      MutableBlockPos mutableTarget = pos.mutable();
      mutableTarget.move(Direction.DOWN);
      WorldGenLevel structureWorldAccess = context.getLevel();
      return this.targetPredicate.test(structureWorldAccess, mutableTarget) ? Stream.of(pos) : Stream.of();
   }

   public PlacementModifierType<?> type() {
      return PCPlacementModifierType.SOLID_CHECK.get();
   }
}
