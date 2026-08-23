package com.voltaire.d_n_a.dungeons_and_arcanus.blocks;

import com.voltaire.d_n_a.dungeons_and_arcanus.Dungeons_and_arcanus;
import com.voltaire.d_n_a.dungeons_and_arcanus.blocks.custom.PCChestTypes;
import com.voltaire.d_n_a.dungeons_and_arcanus.blocks.custom.PC_ChestBlock;
import com.voltaire.d_n_a.dungeons_and_arcanus.blocks.custom.PC_PotTypes;
import com.voltaire.d_n_a.dungeons_and_arcanus.blocks.custom.PotBlock;
import com.voltaire.d_n_a.dungeons_and_arcanus.item.ModItems;
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
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Dungeons_and_arcanus.MOD_ID);
// Remove both example blocks B4 publishing
    // add new blocks here
    public static final  RegistryObject<Block> MITHRIL_BLOCK = registerBlock( "mithril_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DIAMOND_BLOCK)));
    public static final  RegistryObject<Block> ORICALCIUM_BLOCK = registerBlock( "oricalcium_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.EMERALD_BLOCK)));
    public static final  RegistryObject<Block> RAW_SILVER_BLOCK = registerBlock( "raw_silver_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.RAW_COPPER_BLOCK)));
    public static final  RegistryObject<Block> SILVER_BLOCK = registerBlock( "silver_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.RAW_COPPER_BLOCK)));
    public static final  RegistryObject<Block> RUBY_BLOCK = registerBlock( "ruby_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.RAW_COPPER_BLOCK)));


// ore blocks here
public static final RegistryObject<Block> DEEPSLATE_MITHRIL_ORE = registerBlock("deepslate_mithril_ore",
        () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE)
                .strength(3.5f), UniformInt.of(3,6)));
public static final RegistryObject<Block> DEEPSLATE_SILVER_ORE = registerBlock("deepslate_silver_ore",
        () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE)
                .strength(3.5f), UniformInt.of(3,6)));
public static final RegistryObject<Block> MITHRIL_ORE = registerBlock("mithril_ore",
        () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.STONE)
                .strength(2f), UniformInt.of(3,6)));
public static final RegistryObject<Block> ORICALCIUM_ORE = registerBlock("oricalcium_ore",
        () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.BLACKSTONE)
                , UniformInt.of(3,6)));
public static final RegistryObject<Block> SILVER_ORE = registerBlock("silver_ore",
        () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.STONE)
                , UniformInt.of(3,6)));
public static final RegistryObject<Block> RUBY_ORE = registerBlock("ruby_ore",
        () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.STONE)
                , UniformInt.of(3,6)));
//pots
public static final  RegistryObject<Block> LUSH_POT = registerBlock( "lush_pot",
        () -> new PotBlock(PC_PotTypes.LUSH.setting()));
public static final  RegistryObject<Block> NORMAL_POT = registerBlock( "normal_pot",
        () -> new PotBlock(PC_PotTypes.NORMAL.setting()));
public static final  RegistryObject<Block> NETHER_POT = registerBlock( "nether_pot",
        () -> new PotBlock(PC_PotTypes.NETHER.setting()));
public static final  RegistryObject<Block> ROCKY_POT = registerBlock( "rocky_pot",
        () -> new PotBlock(PC_PotTypes.ROCKY.setting()));

//Chests
public static final  RegistryObject<Block> LUSH_CHEST = registerBlock( "lush_chest",
        () -> new PC_ChestBlock(PCChestTypes.LUSH.setting(), PCChestTypes.LUSH));
public static final  RegistryObject<Block> NORMAL_CHEST = registerBlock( "normal_chest",
        () -> new PC_ChestBlock(PCChestTypes.NORMAL.setting(), PCChestTypes.NORMAL));
public static final  RegistryObject<Block> NETHER_CHEST = registerBlock( "nether_chest",
        () -> new PC_ChestBlock(PCChestTypes.NETHER.setting(), PCChestTypes.NETHER));
public static final  RegistryObject<Block> CORAL_CHEST = registerBlock( "coral_chest",
        () -> new PC_ChestBlock(PCChestTypes.CORAL.setting(), PCChestTypes.CORAL));
public static final  RegistryObject<Block> SHADOW_CHEST = registerBlock( "shadow_chest",
        () -> new PC_ChestBlock(PCChestTypes.SHADOW.setting(), PCChestTypes.SHADOW));
public static final  RegistryObject<Block> GOLD_CHEST = registerBlock( "gold_chest",
        () -> new PC_ChestBlock(PCChestTypes.GOLD.setting(), PCChestTypes.GOLD));
public static final  RegistryObject<Block> AZURE_CHEST = registerBlock( "azure_chest",
        () -> new PC_ChestBlock(PCChestTypes.AZURE.setting(), PCChestTypes.AZURE));
public static final  RegistryObject<Block> STONE_CHEST = registerBlock( "stone_chest",
        () -> new PC_ChestBlock(PCChestTypes.STONE.setting(), PCChestTypes.STONE));
public static final  RegistryObject<Block> ICE_CHEST = registerBlock( "ice_chest",
        () -> new PC_ChestBlock(PCChestTypes.ICE.setting(), PCChestTypes.ICE));




     private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block){
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
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
