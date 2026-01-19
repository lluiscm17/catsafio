package net.lluis.catsafio.recipe;

import net.lluis.catsafio.Catsafio;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Catsafio.MOD_ID);

    public static final RegistryObject<RecipeSerializer<HornoBigRecipe>> HORNO_BIG_SERIALIZER =
            SERIALIZERS.register("horno_big", () -> HornoBigRecipe.Serializer.INSTANCE);

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}
