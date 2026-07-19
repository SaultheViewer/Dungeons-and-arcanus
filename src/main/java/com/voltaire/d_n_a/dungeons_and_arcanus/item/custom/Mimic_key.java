package com.voltaire.d_n_a.dungeons_and_arcanus.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Mimic_key extends Item {
    public Mimic_key(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("item.d_n_a.mimic_key.tooltip"));
        pTooltipComponents.add(Component.translatable("item.d_n_a.mimic_key.tooltip2").withStyle(ChatFormatting.BOLD));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }


}

