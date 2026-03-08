package net.lluis.catsafio.hub.mission;

import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.item.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.*;

public class MissionRegistry {

    private static final Map<String, Mission> MISSIONS = new LinkedHashMap<>();
    private static boolean initialized = false;

    public static void init() {
        if (initialized) return;
        initialized = true;

        // ── MISIONES DIARIAS ─────────────────────────────────────────────

        // MISIÓN DÍA 1: Full Diamante - RECOMPENSA: Dedita Celeste "Misión Día 1"
        ItemStack deditaDia1 = new ItemStack(ModItems.DEDITA_CELESTE.get(), 1);
        deditaDia1.setHoverName(Component.literal("§bDedita Misión Día 1"));
        var deditaDia1Meta = deditaDia1.getOrCreateTag();
        var lore1 = new net.minecraft.nbt.ListTag();
        lore1.add(net.minecraft.nbt.StringTag.valueOf(Component.Serializer.toJson(
                Component.literal("§7Se puede canjear en la montaña")
        )));
        deditaDia1Meta.put("display", new net.minecraft.nbt.CompoundTag());
        deditaDia1Meta.getCompound("display").put("Lore", lore1);

        register(new Mission(
                "daily_diamond_armor",
                "Full Diamante",
                "Craftea todas las piezas de armadura de diamante",
                MissionType.DAILY,
                Arrays.asList(
                        new ResourceLocation("minecraft", "diamond_helmet"),
                        new ResourceLocation("minecraft", "diamond_chestplate"),
                        new ResourceLocation("minecraft", "diamond_leggings"),
                        new ResourceLocation("minecraft", "diamond_boots")
                ),
                deditaDia1,
                300
        ));

        // MISIÓN DÍA 2: Manzanas Doradas - RECOMPENSA: Dedita Celeste "Misión Día 2"
        ItemStack deditaDia2 = new ItemStack(ModItems.DEDITA_CELESTE.get(), 1);
        deditaDia2.setHoverName(Component.literal("§bDedita Misión Día 2"));
        var deditaDia2Meta = deditaDia2.getOrCreateTag();
        var lore2 = new net.minecraft.nbt.ListTag();
        lore2.add(net.minecraft.nbt.StringTag.valueOf(Component.Serializer.toJson(
                Component.literal("§7Se puede canjear en la montaña")
        )));
        deditaDia2Meta.put("display", new net.minecraft.nbt.CompoundTag());
        deditaDia2Meta.getCompound("display").put("Lore", lore2);

        register(new Mission(
                "daily_golden_apples",
                "Manzanas Doradas",
                "Craftea 14 manzanas doradas",
                MissionType.DAILY,
                new ResourceLocation("minecraft", "golden_apple"),
                14,
                deditaDia2,
                150
        ));

        // MISIÓN DÍA 3: Matar una raid - RECOMPENSA: Dedita Celeste "Misión Día 3"
        ItemStack deditaDia3 = new ItemStack(ModItems.DEDITA_CELESTE.get(), 1);
        deditaDia3.setHoverName(Component.literal("§bDedita Misión Día 3"));
        var deditaDia3Meta = deditaDia3.getOrCreateTag();
        var lore3 = new net.minecraft.nbt.ListTag();
        lore3.add(net.minecraft.nbt.StringTag.valueOf(Component.Serializer.toJson(
                Component.literal("§7Se puede canjear en la montaña")
        )));
        deditaDia3Meta.put("display", new net.minecraft.nbt.CompoundTag());
        deditaDia3Meta.getCompound("display").put("Lore", lore3);

        register(new Mission(
                "daily_kill_raid",
                "Defensor de la Aldea",
                "Completa una raid y obtén el efecto Héroe de la Aldea",
                MissionType.DAILY,
                new ResourceLocation("minecraft", "hero_of_the_village"),
                1,
                deditaDia3,
                250
        ));

        // ── MISIONES DE ENTRENAMIENTO ────────────────────────────────────

        // Matar 10 Toros Infernales
        register(new Mission(
                "training_kill_bulls",
                "Cazador de Toros",
                "Mata 10 Toros Infernales",
                MissionType.TRAINING,
                new ResourceLocation(Catsafio.MOD_ID, "infernal_bull"),
                10,
                new ItemStack(ModItems.DEDITA_AZUL.get(), 2),
                200
        ));
    }

    public static void register(Mission m) {
        MISSIONS.put(m.getId(), m);
    }

    public static Mission get(String id) {
        return MISSIONS.get(id);
    }

    public static Collection<Mission> getAll() {
        return MISSIONS.values();
    }

    public static List<Mission> getByType(MissionType type) {
        return MISSIONS.values().stream()
                .filter(m -> m.getType() == type)
                .toList();
    }
}