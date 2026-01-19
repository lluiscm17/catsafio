package net.lluis.catsafio.entity;

import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.entity.custom.InfernalBullEntity;
import net.lluis.catsafio.entity.custom.TortugaInfernalEntity;
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


    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
