package net.lluis.catsafio.item.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class HeartItem extends Item {

    // Un UUID único por slot de corazón (0-7)
    private static final UUID[] HEART_UUIDS = {
        UUID.fromString("ca750001-0000-0000-0000-000000000001"),
        UUID.fromString("ca750001-0000-0000-0000-000000000002"),
        UUID.fromString("ca750001-0000-0000-0000-000000000003"),
        UUID.fromString("ca750001-0000-0000-0000-000000000004"),
        UUID.fromString("ca750001-0000-0000-0000-000000000005"),
        UUID.fromString("ca750001-0000-0000-0000-000000000006"),
        UUID.fromString("ca750001-0000-0000-0000-000000000007"),
        UUID.fromString("ca750001-0000-0000-0000-000000000008"),
    };

    public static final int    MAX_HEARTS   = 8;
    public static final String NBT_KEY      = "CatsafioHearts"; // persiste en player NBT
    private static final double HP_PER_HEART = 2.0; // 1 corazón = 2 HP

    public HeartItem(Properties properties) {
        super(properties);
    }

    // ── Comer el corazón ──────────────────────────────────────────────
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide) return InteractionResultHolder.pass(stack);
        if (!(player instanceof ServerPlayer sp)) return InteractionResultHolder.pass(stack);

        CompoundTag data = sp.getPersistentData();
        int current = data.getInt(NBT_KEY);

        if (current >= MAX_HEARTS) {
            sp.sendSystemMessage(Component.literal("§c¡Ya tienes el máximo de corazones! (8/8)"));
            return InteractionResultHolder.fail(stack);
        }

        AttributeInstance maxHp = sp.getAttribute(Attributes.MAX_HEALTH);
        if (maxHp == null) return InteractionResultHolder.fail(stack);

        // Añadir modificador permanente con UUID único para este slot
        UUID uuid = HEART_UUIDS[current];
        if (maxHp.getModifier(uuid) == null) {
            maxHp.addPermanentModifier(new AttributeModifier(
                uuid,
                "catsafio_heart_" + current,
                HP_PER_HEART,
                AttributeModifier.Operation.ADDITION
            ));
        }

        current++;
        data.putInt(NBT_KEY, current);

        // Curar con la vida extra ganada
        sp.setHealth(Math.min(sp.getHealth() + (float) HP_PER_HEART, sp.getMaxHealth()));

        sp.sendSystemMessage(Component.literal(
            "§c❤ ¡Corazón permanente añadido! §7(" + current + "/" + MAX_HEARTS + ")"));

        stack.shrink(1);
        return InteractionResultHolder.consume(stack);
    }

    // ── Tooltip ───────────────────────────────────────────────────────
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("§cAñade un corazón permanente"));
        tooltip.add(Component.literal("§7Máximo §c8 §7corazones extra"));
        tooltip.add(Component.literal("§7§o⚠ Se pierden todos al morir"));
    }
}
