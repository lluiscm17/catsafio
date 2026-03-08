package net.lluis.catsafio.command;

import net.lluis.catsafio.Catsafio;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

@Mod.EventBusSubscriber(modid = Catsafio.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommandStateManager {

    private static final String FILE_NAME = "catsafio_commands.dat";

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        loadStates(event.getServer());
        Catsafio.LOGGER.info("Command states loaded successfully");
    }

    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event) {
        saveStates(event.getServer());
        Catsafio.LOGGER.info("Command states saved successfully");
    }

    private static void saveStates(MinecraftServer server) {
        try {
            File worldFolder = server.getWorldPath(LevelResource.ROOT).toFile();
            File saveFile = new File(worldFolder, FILE_NAME);

            CompoundTag tag = new CompoundTag();
            
            // Comandos originales
            tag.putBoolean("chargedCreepers", ChargedCreepersCommand.isEnabled());
            tag.putBoolean("deadlyCactus", DeadlyCactusCommand.isEnabled());
            tag.putBoolean("deadlyButtons", DeadlyButtonCommand.isEnabled());
            tag.putBoolean("tripleDamage", TripleDamageCommand.isEnabled());
            tag.putBoolean("spiderWeb", SpiderWebCommand.isEnabled());
            tag.putBoolean("crazyHour", CrazyHourCommand.isEnabled());
            tag.putBoolean("deadlyDoors", DeadlyDoorCommand.isEnabled());
            
            // Comandos nuevos
            tag.putBoolean("phantomPandemic", PhantomPandemicCommand.isEnabled());
            tag.putBoolean("wardenGolem", WardenGolemCommand.isEnabled());
            tag.putBoolean("goldFever", GoldFeverCommand.isEnabled());
            tag.putBoolean("enderPearlLimit", EnderPearlLimitCommand.isEnabled());
            tag.putBoolean("weakFeet", WeakFeetCommand.isEnabled());
            tag.putBoolean("starvation", StarvationCommand.isEnabled());
            tag.putBoolean("infernus", InfernusCommand.isEnabled());
            tag.putBoolean("deadlyLava", DeadlyLavaCommand.isEnabled());
            tag.putBoolean("chaosPhantom", ChaosPhantomCommand.isEnabled());
            tag.putBoolean("wildfireTrick", WildfireTrickCommand.isEnabled());
            tag.putBoolean("spiderFever", SpiderFeverCommand.isEnabled());
            tag.putBoolean("enderPearlDamage", EnderPearlDamageCommand.isEnabled());
            tag.putBoolean("explosiveFireworks", ExplosiveFireworksCommand.isEnabled());

            try (FileOutputStream fos = new FileOutputStream(saveFile)) {
                net.minecraft.nbt.NbtIo.writeCompressed(tag, fos);
            }
        } catch (Exception e) {
            Catsafio.LOGGER.error("Failed to save command states", e);
        }
    }

    private static void loadStates(MinecraftServer server) {
        try {
            File worldFolder = server.getWorldPath(LevelResource.ROOT).toFile();
            File saveFile = new File(worldFolder, FILE_NAME);

            if (!saveFile.exists()) {
                Catsafio.LOGGER.info("No command state file found, using defaults");
                return;
            }

            CompoundTag tag;
            try (FileInputStream fis = new FileInputStream(saveFile)) {
                tag = net.minecraft.nbt.NbtIo.readCompressed(fis);
            }

            // Comandos originales
            setState("chargedCreepers", ChargedCreepersCommand.class, "chargedCreepersEnabled", tag.getBoolean("chargedCreepers"));
            setState("deadlyCactus", DeadlyCactusCommand.class, "deadlyCactusEnabled", tag.getBoolean("deadlyCactus"));
            setState("deadlyButtons", DeadlyButtonCommand.class, "deadlyButtonsEnabled", tag.getBoolean("deadlyButtons"));
            setState("tripleDamage", TripleDamageCommand.class, "tripleDamageEnabled", tag.getBoolean("tripleDamage"));
            setState("spiderWeb", SpiderWebCommand.class, "spiderWebEnabled", tag.getBoolean("spiderWeb"));
            setState("crazyHour", CrazyHourCommand.class, "crazyHourEnabled", tag.getBoolean("crazyHour"));
            setState("deadlyDoors", DeadlyDoorCommand.class, "deadlyDoorsEnabled", tag.getBoolean("deadlyDoors"));
            
            // Comandos nuevos
            setState("phantomPandemic", PhantomPandemicCommand.class, "enabled", tag.getBoolean("phantomPandemic"));
            setState("wardenGolem", WardenGolemCommand.class, "enabled", tag.getBoolean("wardenGolem"));
            setState("goldFever", GoldFeverCommand.class, "enabled", tag.getBoolean("goldFever"));
            setState("enderPearlLimit", EnderPearlLimitCommand.class, "enabled", tag.getBoolean("enderPearlLimit"));
            setState("weakFeet", WeakFeetCommand.class, "enabled", tag.getBoolean("weakFeet"));
            setState("starvation", StarvationCommand.class, "enabled", tag.getBoolean("starvation"));
            setState("infernus", InfernusCommand.class, "enabled", tag.getBoolean("infernus"));
            setState("deadlyLava", DeadlyLavaCommand.class, "enabled", tag.getBoolean("deadlyLava"));
            setState("chaosPhantom", ChaosPhantomCommand.class, "enabled", tag.getBoolean("chaosPhantom"));
            setState("wildfireTrick", WildfireTrickCommand.class, "enabled", tag.getBoolean("wildfireTrick"));
            setState("spiderFever", SpiderFeverCommand.class, "enabled", tag.getBoolean("spiderFever"));
            setState("enderPearlDamage", EnderPearlDamageCommand.class, "enabled", tag.getBoolean("enderPearlDamage"));
            setState("explosiveFireworks", ExplosiveFireworksCommand.class, "enabled", tag.getBoolean("explosiveFireworks"));

        } catch (Exception e) {
            Catsafio.LOGGER.error("Failed to load command states", e);
        }
    }

    private static void setState(String name, Class<?> clazz, String fieldName, boolean value) {
        try {
            var field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.setBoolean(null, value);
        } catch (Exception e) {
            Catsafio.LOGGER.error("Failed to set state for: " + name, e);
        }
    }
}
