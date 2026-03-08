package net.lluis.catsafio;

import com.mojang.logging.LogUtils;
import net.lluis.catsafio.block.ModBlocks;
import net.lluis.catsafio.block.entity.ModBlockEntities;
import net.lluis.catsafio.entity.ModEntities;
import net.lluis.catsafio.entity.client.*;
import net.lluis.catsafio.item.ModCreativeModTabs;
import net.lluis.catsafio.item.ModItems;
import net.lluis.catsafio.loot.ModLootModifiers;
import net.lluis.catsafio.network.*;
import net.lluis.catsafio.roulette.RouletteConfig;
import net.lluis.catsafio.sound.ModSounds;
import net.lluis.catsafio.recipe.ModRecipes;
import net.lluis.catsafio.screen.HornoBigScreen;
import net.lluis.catsafio.screen.ModMenuTypes;
import net.lluis.catsafio.hub.command.WildfireDungeonCommand;
import net.lluis.catsafio.command.*;
import net.lluis.catsafio.hub.data.HubData;
import net.lluis.catsafio.hub.data.WorldMissionData;
import net.lluis.catsafio.hub.mission.MissionRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.lluis.catsafio.quest.QuestManager;
import org.slf4j.Logger;

@Mod(Catsafio.MOD_ID)
public class Catsafio {

    public static final String MOD_ID = "catsafio";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Catsafio() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModCreativeModTabs.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModLootModifiers.register(modEventBus);
        ModEntities.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModRecipes.register(modEventBus);
        ModSounds.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.addListener(Catsafio::onWorldSave);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            QuestManager.get().init(FMLPaths.CONFIGDIR.get());
            HubData.get().init(FMLPaths.CONFIGDIR.get());
            MissionRegistry.init();
        });

        CreeperCakePacket.register();
        SapoPackets.register();
        FlashbangPackets.register();
        OrbPackets.register();
        DeathAnimationPacket.register();
        RoulettePacket.register();
        RouletteCompletionPacket.register();
        GorgonaPetrificationPacket.register();
        BombaAmarillaAttachPacket.register();
    }

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        RouletteConfig.load(event.getServer());
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.INFERNIT);
            event.accept(ModItems.RAW_INFERNIT);
            event.accept(ModItems.INFERNIT_UPGRADE_SMITHING_TEMPLATE);
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Cargar misiones del mundo al iniciar el servidor
        if (event.getServer().overworld() != null) {
            WorldMissionData.get(event.getServer().overworld());
            Catsafio.LOGGER.info("Mission data loaded for world");
        }
        RouletteCommand.register(event.getServer().getCommands().getDispatcher());
    }

    @SubscribeEvent
    public static void onWorldSave(LevelEvent.Save event) {
        if (!event.getLevel().isClientSide() && event.getLevel() instanceof ServerLevel level) {
            if (level.dimension() == Level.OVERWORLD) {
                WorldMissionData data = WorldMissionData.get(level);
                data.setDirty();
            }
        }
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        WildfireDungeonCommand.register(event.getDispatcher());
        ChargedCreepersCommand.register(event.getDispatcher());
        DeadlyCactusCommand.register(event.getDispatcher());
        DeadlyButtonCommand.register(event.getDispatcher());
        TripleDamageCommand.register(event.getDispatcher());
        SpiderWebCommand.register(event.getDispatcher());
        CrazyHourCommand.register(event.getDispatcher());
        DeadlyDoorCommand.register(event.getDispatcher());
        PhantomPandemicCommand.register(event.getDispatcher());
        WardenGolemCommand.register(event.getDispatcher());
        GoldFeverCommand.register(event.getDispatcher());
        EnderPearlLimitCommand.register(event.getDispatcher());
        WeakFeetCommand.register(event.getDispatcher());
        StarvationCommand.register(event.getDispatcher());
        InfernusCommand.register(event.getDispatcher());
        DeadlyLavaCommand.register(event.getDispatcher());
        ChaosPhantomCommand.register(event.getDispatcher());
        WildfireTrickCommand.register(event.getDispatcher());
        SpiderFeverCommand.register(event.getDispatcher());
        EnderPearlDamageCommand.register(event.getDispatcher());
        ExplosiveFireworksCommand.register(event.getDispatcher());
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(ModEntities.INFERNAL_BULL.get(),    InfernalBullRenderer::new);
            EntityRenderers.register(ModEntities.TORTUGA_INFERNAL.get(), TortugaInfernalRenderer::new);
            EntityRenderers.register(ModEntities.CREEPER_CAKE.get(),     CreeperCakeRenderer::new);
            EntityRenderers.register(ModEntities.SAPO.get(),             SapoRenderer::new);
            EntityRenderers.register(ModEntities.FANTASMA.get(),          FantasmaRenderer::new);
            EntityRenderers.register(ModEntities.WILDFIRE.get(),           WildfireRenderer::new);
            EntityRenderers.register(ModEntities.FLASHBANG.get(),          FlashbangRenderer::new);
            EntityRenderers.register(ModEntities.MURCIELAGO_WARDEN.get(), MurcielagoWardenRenderer::new);
            EntityRenderers.register(ModEntities.ORB.get(),                 OrbRenderer::new);
            EntityRenderers.register(ModEntities.RUNA.get(),                RunaRenderer::new);
            EntityRenderers.register(ModEntities.CREEPER_WARDEN.get(), CreeperWardenRenderer::new);
            EntityRenderers.register(ModEntities.GORGONA.get(), GorgonaRenderer::new);
            EntityRenderers.register(ModEntities.BOMBA_AMARILLA.get(), BombaAmarillaRenderer::new);
            EntityRenderers.register(ModEntities.TAXI_VOLADOR.get(), TaxiVoladorRenderer::new);
            MenuScreens.register(ModMenuTypes.HORNO_BIG_MENU.get(), HornoBigScreen::new);

        }
    }
}
