package net.lluis.catsafio.hub.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.lluis.catsafio.Catsafio;
import net.lluis.catsafio.hub.MobInfo;
import net.lluis.catsafio.hub.data.HubData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class CatpediaScreen extends Screen {

    private static final ResourceLocation CARTA_BLOQUEADA =
            new ResourceLocation(Catsafio.MOD_ID, "textures/gui/hub/carta_bloqueada.png");
    private static final ResourceLocation CARTA_DESBLOQUEADA =
            new ResourceLocation(Catsafio.MOD_ID, "textures/gui/hub/carta_desbloqueada.png");

    private static final int CARD_W = 60;
    private static final int CARD_H = 90;
    private static final int CARDS_PER_ROW = 5;
    private static final int CARDS_PER_PAGE = 10;
    private static final int SPACING = 12;

    private int guiLeft, guiTop;
    private int hoveredCard = -1;
    private int currentPage = 0;
    private int totalPages;
    private int tickCount = 0;

    public CatpediaScreen() {
        super(Component.translatable("gui.catsafio.catpedia"));
    }

    @Override
    protected void init() {
        totalPages = (int) Math.ceil((double) MobInfo.ALL_MOBS.size() / CARDS_PER_PAGE);

        int totalWidth = CARD_W * CARDS_PER_ROW + SPACING * (CARDS_PER_ROW - 1);

        guiLeft = (this.width - totalWidth) / 2;
        guiTop = 15;

        if (totalPages > 1) {
            this.addRenderableWidget(Button.builder(
                            Component.literal("◀ Anterior"),
                            btn -> { if (currentPage > 0) currentPage--; })
                    .pos(this.width / 2 - 110, this.height - 30)
                    .size(80, 20)
                    .build());

            this.addRenderableWidget(Button.builder(
                            Component.literal("Siguiente ▶"),
                            btn -> { if (currentPage < totalPages - 1) currentPage++; })
                    .pos(this.width / 2 + 30, this.height - 30)
                    .size(80, 20)
                    .build());
        }
    }

    @Override
    public void render(GuiGraphics g, int mx, int my, float partialTick) {
        this.renderBackground(g);

        tickCount++;

        if (totalPages > 1) {
            g.drawCenteredString(font, "§7Página " + (currentPage + 1) + "/" + totalPages,
                    this.width / 2, this.height - 45, 0xAAAAAA);
        }

        hoveredCard = -1;

        int startIdx = currentPage * CARDS_PER_PAGE;
        int endIdx = Math.min(startIdx + CARDS_PER_PAGE, MobInfo.ALL_MOBS.size());

        for (int i = startIdx; i < endIdx; i++) {
            MobInfo mob = MobInfo.ALL_MOBS.get(i);
            boolean unlocked = HubData.get().isMobUnlocked(mob.getId());

            int localIdx = i - startIdx;
            int row = localIdx / CARDS_PER_ROW;
            int col = localIdx % CARDS_PER_ROW;
            int x = guiLeft + col * (CARD_W + SPACING);
            int y = guiTop + row * (CARD_H + SPACING);

            if (mx >= x && mx < x + CARD_W && my >= y && my < y + CARD_H) {
                hoveredCard = i;
            }

            // Renderizar carta con scale
            g.pose().pushPose();

            float scale = CARD_W / 128f;
            g.pose().translate(x, y, 0);
            g.pose().scale(scale, scale, 1f);

            ResourceLocation cardBg = unlocked ? CARTA_DESBLOQUEADA : CARTA_BLOQUEADA;
            RenderSystem.setShaderTexture(0, cardBg);
            g.blit(cardBg, 0, 0, 0, 0, 128, 176, 128, 176);

            g.pose().popPose();

            // Renderizar modelo 3D si está desbloqueada
            if (unlocked && mob.getEntityType() != null) {
                renderEntityInCard(g, x, y, mob.getId(), mob.getEntityType(), tickCount + i * 30);
            }

            // Nombre en la parte SUPERIOR de la carta, en MAYÚSCULAS y NEGRO
            if (unlocked) {
                String nameStr = mob.getDisplayName().getString();
                // Quitar códigos de color y convertir a mayúsculas
                String nameClean = nameStr.replaceAll("§.", "").toUpperCase();
                int nameW = font.width(nameClean);

                // Truncar si es muy largo
                if (nameW > CARD_W - 6) {
                    while (nameW > CARD_W - 12 && nameClean.length() > 3) {
                        nameClean = nameClean.substring(0, nameClean.length() - 1);
                        nameW = font.width(nameClean + "...");
                    }
                    nameClean += "...";
                }

                // Dibujar en la parte superior (y + 8 píxeles desde el borde)
                int textX = x + (CARD_W - font.width(nameClean)) / 2;
                int textY = y + 8;
                // Color negro puro (0xFF000000)
                g.drawString(font, nameClean, textX, textY, 0xFF000000, false);
            }

            // Efecto hover
            if (hoveredCard == i) {
                g.fill(x, y, x + CARD_W, y + CARD_H, 0x44FFFFFF);
            }
        }

        // Tooltip
        if (hoveredCard >= 0) {
            MobInfo mob = MobInfo.ALL_MOBS.get(hoveredCard);
            if (HubData.get().isMobUnlocked(mob.getId())) {
                List<Component> tooltip = new ArrayList<>();
                tooltip.add(mob.getDisplayName());

                List<Component> desc = mob.getDescription();
                for (int i = 0; i < Math.min(4, desc.size()); i++) {
                    tooltip.add(desc.get(i));
                }
                if (desc.size() > 4) {
                    tooltip.add(Component.literal("§8..."));
                }

                tooltip.add(Component.literal(""));
                tooltip.add(Component.literal("§c❤ §f" + mob.getHearts() + " corazones"));
                tooltip.add(Component.literal("§4⚔ §f" + mob.getDamage()));
                tooltip.add(Component.literal("§e⭐ §f" + mob.getDrops()));
                g.renderComponentTooltip(font, tooltip, mx, my);
            }
        }

        BackButtonHelper.drawAndCheckHover(g, font, this.width, this.height, mx, my);

        super.render(g, mx, my, partialTick);
    }

    private void renderEntityInCard(GuiGraphics graphics, int cardX, int cardY,
                                    String mobId,
                                    net.minecraft.world.entity.EntityType<?> entityType,
                                    float rotationTicks) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || entityType == null) return;

        Entity entity = entityType.create(mc.level);
        if (entity == null) return;

        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();

        // Posición centrada en la carta
        int centerX = cardX + CARD_W / 2;
        int centerY = cardY + CARD_H / 2 + 25;

        // Solo el sapo más abajo
        if ("sapo".equals(mobId)) {
            centerY = cardY + CARD_H / 2 + 35;
        }

        poseStack.translate(centerX, centerY, 100);
        poseStack.scale(-20f, -20f, -20f);

        // Rotación
        poseStack.mulPose(Axis.YP.rotationDegrees(rotationTicks * 0.4f));
        poseStack.mulPose(Axis.XP.rotationDegrees(10f));

        // Renderizar
        EntityRenderDispatcher dispatcher = mc.getEntityRenderDispatcher();
        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();

        try {
            dispatcher.render(entity, 0, 0, 0, 0f, 1f,
                    poseStack, bufferSource, 15728880);
            bufferSource.endBatch();
        } catch (Exception e) {
            // Ignorar errores de render
        }

        poseStack.popPose();
        entity.discard();
    }

    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        if (button != 0) return false;

        if (BackButtonHelper.isClicked(this.width, this.height, mx, my)) {
            minecraft.setScreen(new MenuRadialScreen());
            return true;
        }

        return super.mouseClicked(mx, my, button);
    }

    @Override
    public boolean isPauseScreen() { return false; }
}