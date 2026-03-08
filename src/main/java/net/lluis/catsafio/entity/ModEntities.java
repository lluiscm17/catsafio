package net.lluis.catsafio.entity;

import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.entity.custom.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Catsafio.MOD_ID);

    public static final RegistryObject<EntityType<InfernalBullEntity>> INFERNAL_BULL =
            ENTITY_TYPES.register("infernal_bull", () -> EntityType.Builder.of(InfernalBullEntity::new, MobCategory.MONSTER)
                    .sized(2.5f, 1.5f).build("infernal_bull"));

    public static final RegistryObject<EntityType<TortugaInfernalEntity>> TORTUGA_INFERNAL =
            ENTITY_TYPES.register("tortuga_infernal", () -> EntityType.Builder.of(TortugaInfernalEntity::new, MobCategory.MONSTER)
                    .sized(1.5f, 1.5f).build("tortuga_infernal"));

    public static final RegistryObject<EntityType<CreeperCakeEntity>> CREEPER_CAKE =
            ENTITY_TYPES.register("creepercake", () -> EntityType.Builder.of(CreeperCakeEntity::new, MobCategory.MONSTER)
                    .sized(0.6f, 1.7f).build("creepercake"));

    public static final RegistryObject<EntityType<SapoEntity>> SAPO =
            ENTITY_TYPES.register("sapo", () -> EntityType.Builder.of(SapoEntity::new, MobCategory.MONSTER)
                    .sized(2f, 3f).build("sapo"));

    public static final RegistryObject<EntityType<FantasmaEntity>> FANTASMA =
            ENTITY_TYPES.register("fantasma", () -> EntityType.Builder.of(FantasmaEntity::new, MobCategory.MONSTER)
                    .sized(0.6f, 1.8f).build("fantasma"));

    public static final RegistryObject<EntityType<WildfireEntity>> WILDFIRE =
            ENTITY_TYPES.register("wildfire", () -> EntityType.Builder.of(WildfireEntity::new, MobCategory.MONSTER)
                    .sized(1.8f, 2.5f).fireImmune().build("wildfire"));

    public static final RegistryObject<EntityType<FlashbangEntity>> FLASHBANG =
            ENTITY_TYPES.register("flashbang", () -> EntityType.Builder.of(FlashbangEntity::new, MobCategory.MONSTER)
                    .sized(0.6f, 1.7f).build("flashbang"));

    public static final RegistryObject<EntityType<MurcielagoWardenEntity>> MURCIELAGO_WARDEN =
            ENTITY_TYPES.register("murcielago_warden", () -> EntityType.Builder.of(MurcielagoWardenEntity::new, MobCategory.MONSTER)
                    .sized(1.0f, 0.8f).build("murcielago_warden"));

    public static final RegistryObject<EntityType<OrbEntity>> ORB =
            ENTITY_TYPES.register("orb", () -> EntityType.Builder.of(OrbEntity::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f).build("orb"));

    public static final RegistryObject<EntityType<CreeperWardenEntity>> CREEPER_WARDEN =
            ENTITY_TYPES.register("creeper_warden", () -> EntityType.Builder.of(
                            CreeperWardenEntity::new, MobCategory.MONSTER)
                    .sized(0.6f, 1.7f)
                    .build("creeper_warden"));
    public static final RegistryObject<EntityType<GorgonaEntity>> GORGONA =
            ENTITY_TYPES.register("gorgona",
                    () -> EntityType.Builder.of(GorgonaEntity::new, MobCategory.MONSTER)
                            .sized(0.6f, 1.8f)
                            .build("gorgona"));
    public static final RegistryObject<EntityType<BombaAmarillaEntity>> BOMBA_AMARILLA =
            ENTITY_TYPES.register("bomba_amarilla",
                    () -> EntityType.Builder.of(BombaAmarillaEntity::new, MobCategory.MONSTER)
                            .sized(0.6f, 1.0f)
                            .clientTrackingRange(10)
                            .build("bomba_amarilla"));

    public static final RegistryObject<EntityType<TaxiVoladorEntity>> TAXI_VOLADOR =
            ENTITY_TYPES.register("taxi_volador",
                    () -> EntityType.Builder.of(TaxiVoladorEntity::new, MobCategory.MISC)
                            .sized(2.5f, 2.0f)
                            .clientTrackingRange(10)
                            .updateInterval(1) // Sync cada tick para movimiento fluido
                            .build("taxi_volador"));

    // NUEVA ENTIDAD: RUNA
    public static final RegistryObject<EntityType<RunaEntity>> RUNA =
            ENTITY_TYPES.register("runa", () -> EntityType.Builder.of(RunaEntity::new, MobCategory.MISC)
                    .sized(0.8f, 1.4f)
                    .clientTrackingRange(10)
                    .build("runa"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}