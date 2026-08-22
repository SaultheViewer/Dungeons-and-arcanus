package com.voltaire.d_n_a.dungeons_and_arcanus.item.custom;

import com.voltaire.d_n_a.dungeons_and_arcanus.interfaces.PlayerEntityAccess;
import com.voltaire.d_n_a.dungeons_and_arcanus.registry.PCSounds;
import com.voltaire.d_n_a.dungeons_and_arcanus.registry.PCStatistics;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Mimic_Hand_Bell extends Item {
    public Mimic_Hand_Bell(Item.Properties settings) {
        super(settings);
    }

    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        BlockPlaceContext itemPlacementContext = new BlockPlaceContext(context);
        BlockPos blockPos = itemPlacementContext.getClickedPos();
        if (world instanceof ServerLevel serverWorld) {
            Player var7 = context.getPlayer();
            if (var7 instanceof ServerPlayer player) {
                blockPos = blockPos.relative(context.getClickedFace().getOpposite());
                if (serverWorld.getBlockState(blockPos).is(Blocks.AMETHYST_CLUSTER)) {
                    int amount = ((PlayerEntityAccess)player).abandonMimics();
                    if (amount > 0) {
                        player.awardStat(PCStatistics.ABANDONED_MIMICS, amount);
                        CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(player, blockPos, context.getItemInHand());
                    }

                    playSound(world, blockPos, PCSounds.BELL_HIT_1);
                }
            }
        }

        return InteractionResult.sidedSuccess(world.isClientSide);
    }
    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if(Screen.hasShiftDown()){
            pTooltipComponents.add(Component.translatable("item.d_n_a.mimicHandBell.tooltip.shift"));
            pTooltipComponents.add(Component.translatable("item.d_n_a.mimicHandBell.tooltip.shift2"));
            pTooltipComponents.add(Component.translatable("item.d_n_a.mimicHandBell.tooltip.shift3"));
            pTooltipComponents.add(Component.translatable("item.d_n_a.temptooltip"));
        } else {
            pTooltipComponents.add(Component.translatable("item.d_n_a.shift.tooltip"));

        }

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
    private static void playSound(Level world, BlockPos pos, SoundEvent soundEvent) {
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.5;
        double z = pos.getZ() + 0.5;

        world.playSound(
                null,                    // player (null = everyone hears it)
                x, y, z,
                soundEvent,
                SoundSource.BLOCKS,      // usually BLOCKS for block sounds
                0.8F,                    // volume
                world.getRandom().nextFloat() * 0.1F + 0.9F   // pitch
        );
    }
}

