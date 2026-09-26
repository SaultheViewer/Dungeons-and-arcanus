package com.voltaire.d_n_a.dungeons_and_arcanus;

import com.mojang.logging.LogUtils;
import com.voltaire.d_n_a.dungeons_and_arcanus.blocks.ModBlocks;
import com.voltaire.d_n_a.dungeons_and_arcanus.blocks.entity.ModBlockEntitys;
import com.voltaire.d_n_a.dungeons_and_arcanus.client.PCClient;
import com.voltaire.d_n_a.dungeons_and_arcanus.item.ModCreativeModeTabs;
import com.voltaire.d_n_a.dungeons_and_arcanus.item.ModItems;
import com.voltaire.d_n_a.dungeons_and_arcanus.registry.*;
import com.voltaire.d_n_a.dungeons_and_arcanus.utils.MimicDifficulty;
import com.voltaire.d_n_a.dungeons_and_arcanus.utils.PCConfig;
import com.voltaire.d_n_a.dungeons_and_arcanus.utils.PCEventHandler;
import com.voltaire.d_n_a.dungeons_and_arcanus.worldgen.feature.PCPlacementModifierType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Dungeons_and_arcanus.MOD_ID)
public class Dungeons_and_arcanus {

    public static final String MOD_ID = "d_n_a";
    private static final Logger LOGGER = LogUtils.getLogger();
    public static PCConfig loadedConfig = new PCConfig();

    public Dungeons_and_arcanus() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        PCStatistics.CUSTOM_STATS.register(modEventBus);

        ModCreativeModeTabs.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);

        ModEntitys.ENTITY_TYPES.register(modEventBus);
        ModBlockEntitys.BLOCK_ENTITY_TYPES.register(modEventBus);
        PCSounds.SOUND_EVENTS.register(modEventBus);
        PCScreenHandlerType.MENUS.register(modEventBus);
        PCPlacementModifierType.PLACEMENT_MODIFIERS.register(modEventBus);
        PCFeatureRegistry.FEATURES.register(modEventBus);

        modEventBus.addListener(ModEntitys::registerAttributes);
        modEventBus.addListener(ModEntitys::registerSpawnPlacements);
        modEventBus.addListener(PCClient::registerRenderers);
        modEventBus.addListener(PCClient::clientSetup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            LOGGER.info("[Dungeons-And-Arcanus] is initializing.");

            PCEventHandler.registerEvents();
        });
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        // add items/blocks to tabs here if needed
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // server start hooks
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public static PCConfig getConfig() {
        return loadedConfig;
    }

    public static CompoundTag configToNBT() {
        PCConfig config = getConfig();
        CompoundTag nbt = new CompoundTag();
        nbt.putFloat("pot_spawn_chance", config.worldGen.potSpawnChance);
        nbt.putFloat("chest_spawn_chance", config.worldGen.chestSpawnChance);
        nbt.putFloat("secret_mimic_chance", config.worldGen.secretMimicChance);
        nbt.putInt("mimic_difficulty", config.mimicSettings.mimicDifficulty.toInt());
        nbt.putBoolean("spawn_natural_mimics", config.mimicSettings.spawnNaturalMimics);
        nbt.putFloat("natural_mimic_spawn_rate", config.mimicSettings.naturalMimicSpawnRate);
        nbt.putBoolean("allow_pet_mimics", config.mimicSettings.allowPetMimics);
        nbt.putBoolean("do_pet_mimic_limit", config.mimicSettings.doPetMimicLimit);
        nbt.putInt("pet_mimic_limit", config.mimicSettings.petMimicLimit);
        nbt.putInt("abandoned_mimic_timer", config.mimicSettings.abandonedMimicTimer);
        nbt.putBoolean("allow_pet_mimic_locking", config.mimicSettings.allowPetMimicLocking);
        nbt.putBoolean("allow_chest_locking", config.chestSettings.allowChestLocking);
        nbt.putBoolean("enable_locked_chest_owners", config.chestSettings.enableLockedChestOwners);
        return nbt;
    }

    public static PCConfig nbtToConfig(CompoundTag nbt) {
        PCConfig config = new PCConfig();
        if (nbt == null) {
            return config;
        }
        config.worldGen.potSpawnChance = nbt.getFloat("pot_spawn_chance");
        config.worldGen.chestSpawnChance = nbt.getFloat("chest_spawn_chance");
        config.worldGen.secretMimicChance = nbt.getFloat("secret_mimic_chance");
        config.mimicSettings.mimicDifficulty = MimicDifficulty.fromInt(nbt.getInt("mimic_difficulty"));
        config.mimicSettings.spawnNaturalMimics = nbt.getBoolean("spawn_natural_mimics");
        config.mimicSettings.naturalMimicSpawnRate = nbt.getFloat("natural_mimic_spawn_rate");
        config.mimicSettings.allowPetMimics = nbt.getBoolean("allow_pet_mimics");
        config.mimicSettings.doPetMimicLimit = nbt.getBoolean("do_pet_mimic_limit");
        config.mimicSettings.petMimicLimit = nbt.getInt("pet_mimic_limit");
        config.mimicSettings.abandonedMimicTimer = nbt.getInt("abandoned_mimic_timer");
        config.mimicSettings.allowPetMimicLocking = nbt.getBoolean("allow_pet_mimic_locking");
        config.chestSettings.allowChestLocking = nbt.getBoolean("allow_chest_locking");
        config.chestSettings.enableLockedChestOwners = nbt.getBoolean("enable_locked_chest_owners");
        return config;
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                // MenuScreens, client-only setup
            });
        }
    }
}