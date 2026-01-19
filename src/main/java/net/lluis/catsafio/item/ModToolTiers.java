package net.lluis.catsafio.item;

import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.util.ModTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.TierSortingRegistry;

import java.util.List;

public class ModToolTiers {
    public static final Tier AURALITA = TierSortingRegistry.registerTier(
            new ForgeTier(7,4000, 26f, 1f, 26,
                    ModTags.Blocks.NEEDS_AURALITA_TOOL, () -> Ingredient.of(ModItems.AURALITA.get())),
            new ResourceLocation(Catsafio.MOD_ID, "auralita"), List.of(Tiers.NETHERITE), List.of());
    public static final Tier INFERNIT = TierSortingRegistry.registerTier(
            new ForgeTier(6,3500, 22f, 1f, 22,
                    ModTags.Blocks.NEEDS_INFERNIT_TOOL, () -> Ingredient.of(ModItems.INFERNIT.get())),
            new ResourceLocation(Catsafio.MOD_ID, "infernit"), List.of(Tiers.NETHERITE), List.of());
    public static final Tier RAYOLITA = TierSortingRegistry.registerTier(
            new ForgeTier(5,3000, 18f, 1f, 18,
                    ModTags.Blocks.NEEDS_RAYOLITA_TOOL, () -> Ingredient.of(ModItems.RAYOLITA.get())),
            new ResourceLocation(Catsafio.MOD_ID, "rayolita"), List.of(Tiers.NETHERITE), List.of());

    public static final Tier HEALING_SWORD = TierSortingRegistry.registerTier(
            new ForgeTier(5,3000, 1f, 1f, 18,
                    ModTags.Blocks.NEEDS_RAYOLITA_TOOL, () -> Ingredient.of(ModItems.RAYOLITA.get())),
            new ResourceLocation(Catsafio.MOD_ID, "healing_sword"), List.of(Tiers.NETHERITE), List.of());
}