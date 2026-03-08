package net.lluis.catsafio.hub;

import net.lluis.catsafio.entity.ModEntities;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public class MobInfo {

    private final String id;
    private final Component displayName;
    private final List<Component> description;
    private final ResourceLocation texture;
    private final EntityType<?> entityType;
    private final int hearts;
    private final String damage;
    private final String drops;

    public MobInfo(String id, Component name, List<Component> desc,
                   ResourceLocation texture, EntityType<?> type,
                   int hearts, String damage, String drops) {
        this.id = id;
        this.displayName = name;
        this.description = desc;
        this.texture = texture;
        this.entityType = type;
        this.hearts = hearts;
        this.damage = damage;
        this.drops = drops;
    }

    public String getId() { return id; }
    public Component getDisplayName() { return displayName; }
    public List<Component> getDescription() { return description; }
    public ResourceLocation getTexture() { return texture; }
    public EntityType<?> getEntityType() { return entityType; }
    public int getHearts() { return hearts; }
    public String getDamage() { return damage; }
    public String getDrops() { return drops; }

    // ── Lista completa de mobs ──────────────────────────────────────────
    public static final List<MobInfo> ALL_MOBS = new ArrayList<>();

    static {
        // Creeper Cake
        ALL_MOBS.add(new MobInfo(
                "creeper_cake",
                Component.literal("§2Creeper Cake"),
                List.of(
                        Component.literal("§7Un pastel explosivo con patas."),
                        Component.literal("§7Se acerca sigilosamente y"),
                        Component.literal("§7explota al estar cerca del jugador."),
                        Component.literal(""),
                        Component.literal("§e⚠ Explosión: §c4 bloques de radio"),
                        Component.literal("§7Overlay visual antes de explotar")
                ),
                new ResourceLocation("catsafio", "textures/entity/creepercake.png"),
                ModEntities.CREEPER_CAKE.get(),
                20, "Explosión variable", "§6TNT (raro)"
        ));

        // Sapo
        ALL_MOBS.add(new MobInfo(
                "sapo",
                Component.literal("§2Sapo Gigante"),
                List.of(
                        Component.literal("§7Sapo gigante que atrapa jugadores"),
                        Component.literal("§7con su lengua pegajosa."),
                        Component.literal(""),
                        Component.literal("§c¡FASE 1: Lengua Pegajosa!"),
                        Component.literal("§7• Te arrastra hacia él"),
                        Component.literal("§7• Pulsa §fESPACIO §750 veces en 6s"),
                        Component.literal(""),
                        Component.literal("§4¡FASE 2: Tragado!"),
                        Component.literal("§7• 4 HP/s de daño"),
                        Component.literal("§7• Mata al sapo para escapar"),
                        Component.literal("§7• Cooldown: 30 segundos")
                ),
                new ResourceLocation("catsafio", "textures/entity/sapo.png"),
                ModEntities.SAPO.get(),
                125, "4 HP/segundo tragado", "§6Ojo de Sapo"
        ));

        // Fantasma
        ALL_MOBS.add(new MobInfo(
                "fantasma",
                Component.literal("§cFantasma Rojo"),
                List.of(
                        Component.literal("§7Espíritu errante que atraviesa"),
                        Component.literal("§7paredes y flota libremente."),
                        Component.literal(""),
                        Component.literal("§e⚠ Inmune a armas normales"),
                        Component.literal("§7Solo la §5Espada Fantasmal"),
                        Component.literal("§7puede dañarlo."),
                        Component.literal(""),
                        Component.literal("§7Spawn: Cuevas oscuras (Y<60)")
                ),
                new ResourceLocation("catsafio", "textures/entity/fantasma.png"),
                ModEntities.FANTASMA.get(),
                15, "2 corazones", "§7Depende del loot table"
        ));

        // Wildfire
        ALL_MOBS.add(new MobInfo(
                "wildfire",
                Component.literal("§6Wildfire"),
                List.of(
                        Component.literal("§7Blaze mejorado del Nether con"),
                        Component.literal("§7escudos flotantes y fuego mortal."),
                        Component.literal(""),
                        Component.literal("§c¡Sistema de Escudos!"),
                        Component.literal("§7• 4 escudos (150→112→75→37 HP)"),
                        Component.literal("§7• Textura cambia al perder escudos"),
                        Component.literal(""),
                        Component.literal("§6Ataque de Fuego:"),
                        Component.literal("§7• Ráfagas de 3 bolas de fuego"),
                        Component.literal("§c• Ignora Fire Resistance"),
                        Component.literal("§7• 4 corazones por proyectil")
                ),
                new ResourceLocation("catsafio", "textures/entity/wildfire_4.png"),
                ModEntities.WILDFIRE.get(),
                75, "4 corazones/proyectil", "§7Depende del loot table"
        ));

        // Flashbang
        ALL_MOBS.add(new MobInfo(
                "flashbang",
                Component.literal("§eFlashbang"),
                List.of(
                        Component.literal("§7Criatura veloz que explota"),
                        Component.literal("§7al contacto con un flash cegador."),
                        Component.literal(""),
                        Component.literal("§e⚠ Efectos al Impacto:"),
                        Component.literal("§7• Flash blanco de 4 segundos"),
                        Component.literal("§7• 1 corazón de daño"),
                        Component.literal("§7• Sonido ensordecedor"),
                        Component.literal(""),
                        Component.literal("§cMuy rápido (0.55 velocidad)"),
                        Component.literal("§7Se autodestruye tras explotar")
                ),
                new ResourceLocation("catsafio", "textures/entity/flashbang.png"),
                ModEntities.FLASHBANG.get(),
                4, "1 corazón", "§7Ninguno"
        ));

        // Murciélago Warden
        ALL_MOBS.add(new MobInfo(
                "murcielago_warden",
                Component.literal("§8Murciélago Warden"),
                List.of(
                        Component.literal("§7Depredador aéreo del Deep Dark"),
                        Component.literal("§7con vuelo dinámico y mortal."),
                        Component.literal(""),
                        Component.literal("§e3 Estados:"),
                        Component.literal("§7• §8Dormido: §7Inactivo hasta daño"),
                        Component.literal("§7• §7Patrulla: §7Vuelo horizontal"),
                        Component.literal("§7• §cPersecución: §7Caza activa"),
                        Component.literal(""),
                        Component.literal("§cAtaque Aura (radio 1.8):"),
                        Component.literal("§7• 3 corazones por contacto"),
                        Component.literal("§7• Cooldown: 1 segundo"),
                        Component.literal(""),
                        Component.literal("§7Spawn: §8Solo Deep Dark (Y<0)")
                ),
                new ResourceLocation("catsafio", "textures/entity/murcielago_warden.png"),
                ModEntities.MURCIELAGO_WARDEN.get(),
                30, "3 corazones/contacto", "§6Ala de Murciélago Warden"
        ));

        // Toro Infernal - TEXTURA DESDE GEO
        ALL_MOBS.add(new MobInfo(
                "infernal_bull",
                Component.literal("§6Toro Infernal"),
                List.of(
                        Component.literal("§7Bestia del Nether con embestida"),
                        Component.literal("§7devastadora y resistencia brutal."),
                        Component.literal(""),
                        Component.literal("§c¡Embestida Mortal!"),
                        Component.literal("§7• 8 corazones de daño"),
                        Component.literal("§7• Knockback masivo"),
                        Component.literal("§7• Velocidad: 0.35"),
                        Component.literal(""),
                        Component.literal("§eStats:"),
                        Component.literal("§7• 50 corazones de vida"),
                        Component.literal("§7• Armadura: 8 puntos"),
                        Component.literal("§7Spawn: §cNether (todos los biomas)")
                ),
                new ResourceLocation("catsafio", "textures/entity/infernal_bull.png"),
                ModEntities.INFERNAL_BULL.get(),
                50, "8 corazones", "§6Cuerno Infernal"
        ));

        // Tortuga Infernal - TEXTURA DESDE GEO
        ALL_MOBS.add(new MobInfo(
                "tortuga_infernal",
                Component.literal("§cTortuga Infernal"),
                List.of(
                        Component.literal("§7Tortuga acorazada del Nether"),
                        Component.literal("§7con defensa impenetrable."),
                        Component.literal(""),
                        Component.literal("§e¡Sistema de Caparazón!"),
                        Component.literal("§7• Se esconde cuando tiene baja vida"),
                        Component.literal("§7• §cInmune a proyectiles §7escondida"),
                        Component.literal("§7• Sale tras curar o ser atacada"),
                        Component.literal(""),
                        Component.literal("§eStats:"),
                        Component.literal("§7• 100 corazones de vida"),
                        Component.literal("§7• 3 corazones de daño"),
                        Component.literal("§7Spawn: §cNether (todos los biomas)")
                ),
                new ResourceLocation("catsafio", "textures/entity/tortuga_infernal.png"),
                ModEntities.TORTUGA_INFERNAL.get(),
                100, "3 corazones", "§6Fragmento de Netherita"
        ));

        // Creeper Warden
        ALL_MOBS.add(new MobInfo(
                "creeper_warden",
                Component.literal("§0Creeper Warden"),
                List.of(
                        Component.literal("§7Creeper CIEGO que detecta por"),
                        Component.literal("§7vibraciones como el Warden."),
                        Component.literal(""),
                        Component.literal("§c¡Detección por Vibraciones!"),
                        Component.literal("§7• §4CIEGO §7- No puede verte"),
                        Component.literal("§7• Detecta §cMOVIMIENTO §7(15 bloques)"),
                        Component.literal("§7• Agachado: §a5 bloques §7(1/3)"),
                        Component.literal("§7• Quieto: §aINVISIBLE"),
                        Component.literal(""),
                        Component.literal("§4Explosión Mortal:"),
                        Component.literal("§7• Radio: §c5 bloques §7(10 powered)"),
                        Component.literal("§7• Tiempo: §e1.5 segundos"),
                        Component.literal("§7• Distancia: §c3 bloques §7→ BOOM!"),
                        Component.literal(""),
                        Component.literal("§8Spawn: Solo Deep Dark (Y<0)")
                ),
                new ResourceLocation("catsafio", "textures/entity/creeper_warden.png"),
                ModEntities.CREEPER_WARDEN.get(),
                40, "Explosión radio 5", "§7Ninguno"
        ));

        ALL_MOBS.add(new MobInfo(
                "gorgona",
                Component.literal("§0Gorgona"),
                List.of(
                        Component.literal("§7Creeper CIEGO que detecta por"),
                        Component.literal("§7vibraciones como el Warden."),
                        Component.literal(""),
                        Component.literal("§cMonstruo Mitológico"),
                        Component.literal("§7Petrifica en un ratio de 7 bloques"),
                        Component.literal(""),
                        Component.literal("§8Spawn: Solo Dungeon")
                ),
                new ResourceLocation("catsafio", "textures/entity/gorgona.png"),
                ModEntities.GORGONA.get(),
                40, "Rango petrficación 7", "§7Sangre de gorgona"
        ));

        ALL_MOBS.add(new MobInfo(
                "bomba_amarila",
                Component.literal("§0Bomba Amarilla"),
                List.of(
                        Component.literal("§7Bomba que te quita toda"),
                        Component.literal("§7la vida al explotar."),
                        Component.literal("§7Pulsa 50 veces el espacio"),
                        Component.literal("§7para salvarte."),
                        Component.literal(""),
                        Component.literal("§8Spawn: Overworld Dia y Noche")
                ),
                new ResourceLocation("catsafio", "textures/entity/bomba_amarilla.png"),
                ModEntities.GORGONA.get(),
                20, "Rango petrficación 7", "§7Sangre de gorgona"
        ));

    }
}