package wily.betterfurnaces.recipes;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import wily.betterfurnaces.init.ModObjects;
import wily.betterfurnaces.init.Registration;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class CobblestoneGeneratorRecipes implements Recipe<Container> {
    public static final CobblestoneGeneratorRecipes.Serializer SERIALIZER = new CobblestoneGeneratorRecipes.Serializer();

    public ResourceLocation recipeId;
    private final HashMap<Ingredient, Integer> ingredients = new LinkedHashMap<>();
    public Ingredient result;
    public int resultCount;
    public int duration;
    public CobblestoneGeneratorRecipes(ResourceLocation recipeId) {
        this.recipeId = recipeId;
    }
    public int getDuration() {
        return duration;
    }
    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public boolean matches(Container inv, Level worldIn) {
        return result.test(inv.getItem(0));
    }

    @Override
    public ItemStack assemble(Container p_44001_, RegistryAccess access) {
        return getResultItem(access).copy();
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess access) {
        return result.getItems()[0];
    }

    @Override
    public ResourceLocation getId() {
        return recipeId;
    }
    public Map<Ingredient, Integer> getIngredientMap() {
        return ImmutableMap.copyOf(ingredients);
    }
    public int getIngredientCost(ItemStack stack) {
        for (Map.Entry<Ingredient, Integer> entry : ingredients.entrySet()) {
            if (entry.getKey().test(stack)) {
                return entry.getValue();
            }
        }
        return 0;
    }

    public static class Serializer implements RecipeSerializer<CobblestoneGeneratorRecipes> {

        @Override
        public CobblestoneGeneratorRecipes fromJson(ResourceLocation recipeId, JsonObject json) {
            CobblestoneGeneratorRecipes recipe = new CobblestoneGeneratorRecipes(recipeId);

            JsonObject ingredient = GsonHelper.getAsJsonObject(json, "result");
            recipe.resultCount = GsonHelper.getAsInt(ingredient, "count", 1);

            recipe.result = Ingredient.fromJson(json.get("result"));
            recipe.duration = GsonHelper.getAsInt(json, "duration", 600);

            return recipe;
        }

        @Nullable
        @Override
        public CobblestoneGeneratorRecipes fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            CobblestoneGeneratorRecipes recipe = new CobblestoneGeneratorRecipes(recipeId);
            recipe.result = Ingredient.fromNetwork(buffer);
            recipe.duration = buffer.readInt();
            recipe.resultCount = buffer.readInt();
            return recipe;
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, CobblestoneGeneratorRecipes recipe) {

            recipe.result.toNetwork(buffer);
            buffer.writeInt(recipe.duration);
            buffer.writeInt(recipe.resultCount);

        }
    }
    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }
    @Override
    public RecipeType<?> getType() {
        return ModObjects.ROCK_GENERATING_RECIPE.get();
    }
}