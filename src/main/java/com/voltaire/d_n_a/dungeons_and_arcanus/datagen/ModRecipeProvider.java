package com.voltaire.d_n_a.dungeons_and_arcanus.datagen;

import com.voltaire.d_n_a.dungeons_and_arcanus.Dungeons_and_arcanus;
import com.voltaire.d_n_a.dungeons_and_arcanus.blocks.ModBlocks;
import com.voltaire.d_n_a.dungeons_and_arcanus.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.List;
import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    private static final List<ItemLike> MITHRIL_SMELTABLES = List.of(ModItems.RAW_MITHRIL.get(),
            ModBlocks.MITHRIL_ORE.get(), ModBlocks.DEEPSLATE_MITHRIL_ORE.get());
    private static final List<ItemLike> SILVER_SMELTABLES = List.of(ModItems.RAW_SILVER.get(),
            ModBlocks.SILVER_ORE.get(), ModBlocks.DEEPSLATE_SILVER_ORE.get());

    public ModRecipeProvider(PackOutput pOutput) {

        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        //if you have opened the mod to look for gem smelting no! that would crack the gems
        oreBlasting(pWriter, MITHRIL_SMELTABLES, RecipeCategory.MISC, ModItems.MITHRIL_INGOT.get(), 0.25f, 100,"mithril");
        oreBlasting(pWriter, SILVER_SMELTABLES, RecipeCategory.MISC, ModItems.SILVER_INGOT.get(), 0.25f, 100,"silver");
        oreSmelting(pWriter, MITHRIL_SMELTABLES, RecipeCategory.MISC, ModItems.MITHRIL_INGOT.get(), 0.25f, 200,"mithril");
        oreSmelting(pWriter, SILVER_SMELTABLES, RecipeCategory.MISC, ModItems.SILVER_INGOT.get(), 0.25f, 200,"silver");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.MITHRIL_BLOCK.get())
                .pattern("mmm")
                .pattern("mmm")
                .pattern("mmm")
                .define('m', ModItems.MITHRIL_INGOT.get())
                .unlockedBy(getHasName(ModItems.MITHRIL_INGOT.get()), has(ModItems.MITHRIL_INGOT.get()))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.MITHRIL_INGOT.get(), 9)
                .requires(ModBlocks.MITHRIL_BLOCK.get())
                .unlockedBy(getHasName(ModBlocks.MITHRIL_BLOCK.get()), has(ModBlocks.MITHRIL_BLOCK.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.SILVER_BLOCK.get())
                .pattern("sss")
                .pattern("sss")
                .pattern("sss")
                .define('s', ModItems.SILVER_INGOT.get())
                .unlockedBy(getHasName(ModItems.SILVER_INGOT.get()), has(ModItems.SILVER_INGOT.get()))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.SILVER_INGOT.get(), 9)
                .requires(ModBlocks.SILVER_BLOCK.get())
                .unlockedBy(getHasName(ModBlocks.SILVER_BLOCK.get()), has(ModBlocks.SILVER_BLOCK.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.RAW_SILVER_BLOCK.get())
                .pattern("RRR")
                .pattern("RRR")
                .pattern("RRR")
                .define('R', ModItems.RAW_SILVER.get())
                .unlockedBy(getHasName(ModItems.RAW_SILVER.get()), has(ModItems.RAW_SILVER.get()))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.RAW_SILVER.get(), 9)
                .requires(ModBlocks.RAW_SILVER_BLOCK.get())
                .unlockedBy(getHasName(ModBlocks.RAW_SILVER_BLOCK.get()), has(ModBlocks.RAW_SILVER_BLOCK.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.RUBY_BLOCK.get())
                .pattern("RRR")
                .pattern("RRR")
                .pattern("RRR")
                .define('R', ModItems.RUBY.get())
                .unlockedBy(getHasName(ModItems.RUBY.get()), has(ModItems.RUBY.get()))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.RUBY.get(), 9)
                .requires(ModBlocks.RUBY_BLOCK.get())
                .unlockedBy(getHasName(ModBlocks.RUBY_BLOCK.get()), has(ModBlocks.RUBY_BLOCK.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.ORICALCIUM_BLOCK.get())
                .pattern("ooo")
                .pattern("ooo")
                .pattern("ooo")
                .define('o', ModItems.ORICALCIUM_GEM.get())
                .unlockedBy(getHasName(ModItems.ORICALCIUM_GEM.get()), has(ModItems.ORICALCIUM_GEM.get()))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.ORICALCIUM_GEM.get(), 9)
                .requires(ModBlocks.ORICALCIUM_BLOCK.get())
                .unlockedBy(getHasName(ModBlocks.ORICALCIUM_BLOCK.get()), has(ModBlocks.ORICALCIUM_BLOCK.get()))
                .save(pWriter);

    }
    protected static void oreSmelting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTIme, String pGroup) {
        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.SMELTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTIme, pGroup, "_from_smelting");
    }

    protected static void oreBlasting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup) {
        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.BLASTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTime, pGroup, "_from_blasting");
    }

    protected static void oreCooking(Consumer<FinishedRecipe> pFinishedRecipeConsumer, RecipeSerializer<? extends AbstractCookingRecipe> pCookingSerializer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup, String pRecipeName) {
        for(ItemLike itemlike : pIngredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), pCategory, pResult, pExperience, pCookingTime, pCookingSerializer).group(pGroup).unlockedBy(getHasName(itemlike), has(itemlike))
                    .save(pFinishedRecipeConsumer, Dungeons_and_arcanus.MOD_ID + ":" + getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike));
        }

    }
}
