package com.voltaire.d_n_a.dungeons_and_arcanus.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.voltaire.d_n_a.dungeons_and_arcanus.Dungeons_and_arcanus;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class SpinningWheenRecipie implements Recipe<SimpleContainer> {
    private final NonNullList<Ingredient> inputItems;
    private final int inputCount;
    private final ItemStack output;
    private final ResourceLocation id;

    public SpinningWheenRecipie(NonNullList<Ingredient> inputItems, int inputCount, ItemStack output, ResourceLocation id) {
        this.inputItems = inputItems;
        this.inputCount = inputCount;
        this.output = output;
        this.id = id;
    }

    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        if(pLevel.isClientSide()){
            return false;
        }

        ItemStack stack = pContainer.getItem(0);
        return inputItems.get(0).test(stack) && stack.getCount() >= inputCount;
    }
    public int getInputCount() {
        return this.inputCount;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.inputItems;
    }

    @Override
    public ItemStack assemble(SimpleContainer pContainer, RegistryAccess pRegistryAccess) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return output.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }
    public static class Type implements RecipeType<SpinningWheenRecipie> {
        public static final Type INSTANCE = new Type();
        public static final  String ID = "spinning";
    }
    public static class Serializer implements RecipeSerializer<SpinningWheenRecipie> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(Dungeons_and_arcanus.MOD_ID, "spinning");

        @Override
        public SpinningWheenRecipie fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"));

            JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(1, Ingredient.EMPTY);

            for(int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }
            int count = GsonHelper.getAsInt(pSerializedRecipe, "count", 1);

            return new SpinningWheenRecipie(inputs, count, output, pRecipeId);
        }


        @Override
        public @Nullable SpinningWheenRecipie fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(pBuffer.readInt(), Ingredient.EMPTY);

            for(int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromNetwork(pBuffer));
            }
            int count = pBuffer.readVarInt();
            ItemStack output = pBuffer.readItem();
            return new SpinningWheenRecipie(inputs, count, output, pRecipeId);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, SpinningWheenRecipie pRecipe) {
            pBuffer.writeInt(pRecipe.inputItems.size());

            for (Ingredient ingredient : pRecipe.getIngredients()) {
                ingredient.toNetwork(pBuffer);
            }
            pBuffer.writeVarInt(pRecipe.inputCount);
            pBuffer.writeItemStack(pRecipe.getResultItem(null), false);
        }
    }
}
