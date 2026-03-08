package net.lluis.catsafio.hub.mission;

import net.lluis.catsafio.hub.data.WorldMissionData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.lluis.catsafio.Catsafio;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.resources.ResourceLocation;

@Mod.EventBusSubscriber(modid = Catsafio.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MissionTracker {

    /** Llamado cuando un mob muere */
    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (!(event.getSource().getEntity() instanceof ServerPlayer player)) return;

        ResourceLocation mobId = ForgeRegistries.ENTITY_TYPES.getKey(event.getEntity().getType());
        if (mobId == null) return;

        // TRAINING missions de matar
        for (Mission m : MissionRegistry.getByType(MissionType.TRAINING)) {
            if (!m.isEnabled() || m.isCompleted()) continue;
            if (m.getTarget() != null && m.getTarget().equals(mobId)) {
                m.addProgress(1);
                checkCompletion(m, player);
                // Guardar en el mundo actual
                WorldMissionData.get(player.level()).saveMissionProgress(m.getId(), m.getProgress(), m.isCompleted());
            }
        }
    }

    /** Llamado cuando el jugador craftea un item */
    @SubscribeEvent
    public static void onCraft(PlayerEvent.ItemCraftedEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        ItemStack crafted = event.getCrafting();
        ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(crafted.getItem());
        if (itemId == null) return;

        // DAILY missions de craftear
        for (Mission m : MissionRegistry.getByType(MissionType.DAILY)) {
            if (!m.isEnabled() || m.isCompleted()) continue;

            // Misión regular de craftear
            if (!m.isMultiItem() && m.getTarget() != null && m.getTarget().equals(itemId)) {
                m.addProgress(crafted.getCount());
                checkCompletion(m, player);
                WorldMissionData.get(player.level()).saveMissionProgress(m.getId(), m.getProgress(), m.isCompleted());
            }

            // Misión multi-item (Full Diamante)
            if (m.isMultiItem()) {
                int index = m.getMultiTargets().indexOf(itemId);
                if (index >= 0 && !m.isSubItemComplete(itemId)) {
                    m.completeSubItem(index);
                    checkCompletion(m, player);
                    WorldMissionData.get(player.level()).saveMissionProgress(m.getId(), m.getProgress(), m.isCompleted());
                }
            }
        }
    }

    /** Llamado cuando el jugador obtiene un efecto de poción */
    @SubscribeEvent
    public static void onPotionAdded(MobEffectEvent.Added event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        // Detectar Hero of the Village (raid completada)
        if (event.getEffectInstance().getEffect() == MobEffects.HERO_OF_THE_VILLAGE) {
            for (Mission m : MissionRegistry.getByType(MissionType.DAILY)) {
                if (!m.isEnabled() || m.isCompleted()) continue;
                // Verificar si es la misión de raid
                if (m.getId().equals("daily_kill_raid")) {
                    m.addProgress(1);
                    checkCompletion(m, player);
                    WorldMissionData.get(player.level()).saveMissionProgress(m.getId(), m.getProgress(), m.isCompleted());
                }
            }
        }
    }

    private static void checkCompletion(Mission m, ServerPlayer player) {
        if (m.isFinished() && !m.isCompleted()) {
            m.setCompleted(true);
            if (!m.getRewardItem().isEmpty()) {
                player.addItem(m.getRewardItem().copy());
            }
            player.giveExperiencePoints(m.getRewardXp());
            player.sendSystemMessage(
                    net.minecraft.network.chat.Component.literal(
                            "§a§l✔ Misión completada: §f" + m.getDisplayName() +
                                    " §7(+" + m.getRewardXp() + " XP)")
            );
        }
    }
}