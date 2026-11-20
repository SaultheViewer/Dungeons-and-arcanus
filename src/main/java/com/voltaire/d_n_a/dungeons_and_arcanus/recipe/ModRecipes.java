package com.voltaire.d_n_a.dungeons_and_arcanus.recipe;

import com.voltaire.d_n_a.dungeons_and_arcanus.Dungeons_and_arcanus;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


    public class ModRecipes {
        public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
                DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Dungeons_and_arcanus.MOD_ID);

        public static final RegistryObject<RecipeSerializer<SpinningWheenRecipie>> GEM_POLISHING_SERIALIZER =
                SERIALIZERS.register("spinning", () -> SpinningWheenRecipie.Serializer.INSTANCE);

        public static void register(IEventBus eventBus) {
            SERIALIZERS.register(eventBus);
        }
    }

