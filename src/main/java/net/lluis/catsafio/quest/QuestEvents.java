package net.lluis.catsafio.quest;

import net.lluis.catsafio.Catsafio;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = Catsafio.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class QuestEvents {

    /** Se dispara cuando cualquier LivingEntity muere */
    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        // Solo nos interesa si el atacante es un jugador
        if (!(event.getSource().getEntity() instanceof Player)) return;

        LivingEntity killed = event.getEntity();
        ResourceLocation mobId = ForgeRegistries.ENTITY_TYPES.getKey(killed.getType());
        if (mobId == null) return;

        QuestManager.get().onKill(mobId.toString());
    }

    /** Se dispara cuando el jugador craftea un item */
    @SubscribeEvent
    public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        ItemStack result = event.getCrafting();
        ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(result.getItem());
        if (itemId == null) return;

        QuestManager.get().onCraft(itemId.toString());
    }

    /** Guarda el progreso cuando el jugador sale */
    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            QuestManager.get().saveProgress(player.getUUID());
        }
    }
}
