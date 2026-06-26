package com.voltaire.dugeons.dungeons_and_arcanus.blocks;

import com.voltaire.dugeons.dungeons_and_arcanus.Dungeons_and_arcanus;
import com.voltaire.dugeons.dungeons_and_arcanus.item.ModItems;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Dungeons_and_arcanus.MOD_ID);
// Remove both example blocks B4 publishing
    // add new blocks here
    public static final  RegistryObject<Block> SUGAR_BLOCK = registerBlock( "test_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.SAND)));
// ore blocks here
public static final RegistryObject<Block> TEST_ORE = registerBlock("test_ore",
        () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.STONE)
                .strength(2f), UniformInt.of(3,6)));

     private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block){
        RegistryObject<T> toReturn =BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
     }

         //registers item associated with block
     private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block){
       return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
     }


    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);


    }
}
