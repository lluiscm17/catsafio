package net.lluis.catsafio.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.lluis.catsafio.entity.animations.ModAnimationDefinitions;
import net.lluis.catsafio.entity.custom.InfernalBullEntity;
import net.lluis.catsafio.entity.custom.TortugaInfernalEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class TortugaInfernalModel<T extends Entity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "unknown"), "main");
	private final ModelPart bone;
	private final ModelPart head;
	private final ModelPart body;
	private final ModelPart cuerno_lateral_delantero_der;
	private final ModelPart cuerno_lateral_trasero_der2;
	private final ModelPart cuerno_lateral_delantero_iz;
	private final ModelPart cuerno_lateral_trasero_iz;
	private final ModelPart cuerno_delantero_derecho;
	private final ModelPart cuerno_trasero_central;
	private final ModelPart cuerno_delantero_izquierdo;
	private final ModelPart cuerno_superior_izquierdo;
	private final ModelPart cuerno_superior_izquierdo2;
	private final ModelPart cuerno_superior_izquierdo3;
	private final ModelPart front_right_leg;
	private final ModelPart front_left_leg;
	private final ModelPart back_right_leg;
	private final ModelPart back_left_leg;
	private final ModelPart tail;

	public TortugaInfernalModel(ModelPart root) {
		this.bone = root.getChild("bone");
		this.head = this.bone.getChild("head");
		this.body = this.bone.getChild("body");
		this.cuerno_lateral_delantero_der = this.body.getChild("cuerno_lateral_delantero_der");
		this.cuerno_lateral_trasero_der2 = this.body.getChild("cuerno_lateral_trasero_der2");
		this.cuerno_lateral_delantero_iz = this.body.getChild("cuerno_lateral_delantero_iz");
		this.cuerno_lateral_trasero_iz = this.body.getChild("cuerno_lateral_trasero_iz");
		this.cuerno_delantero_derecho = this.body.getChild("cuerno_delantero_derecho");
		this.cuerno_trasero_central = this.body.getChild("cuerno_trasero_central");
		this.cuerno_delantero_izquierdo = this.body.getChild("cuerno_delantero_izquierdo");
		this.cuerno_superior_izquierdo = this.body.getChild("cuerno_superior_izquierdo");
		this.cuerno_superior_izquierdo2 = this.body.getChild("cuerno_superior_izquierdo2");
		this.cuerno_superior_izquierdo3 = this.body.getChild("cuerno_superior_izquierdo3");
		this.front_right_leg = this.bone.getChild("front_right_leg");
		this.front_left_leg = this.bone.getChild("front_left_leg");
		this.back_right_leg = this.bone.getChild("back_right_leg");
		this.back_left_leg = this.bone.getChild("back_left_leg");
		this.tail = this.bone.getChild("tail");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offsetAndRotation(-12.0F, 24.0F, -1.5F, 0.0F, 1.5708F, 0.0F));

		PartDefinition head = bone.addOrReplaceChild("head", CubeListBuilder.create().texOffs(106, 49).addBox(13.0F, -2.5F, 5.0F, 11.25F, 6.25F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(106, 41).addBox(13.0F, -2.5F, 13.0F, 11.25F, 6.25F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(102, 12).addBox(13.0F, -3.0F, 7.0F, 11.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(105, 24).addBox(7.6F, -4.5F, 7.0F, 10.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.1F, -7.5F, 2.0F));

		PartDefinition head_r1 = head.addOrReplaceChild("head_r1", CubeListBuilder.create().texOffs(94, 88).addBox(12.91F, -1.01F, -5.0F, 10.1F, 1.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.25F, -3.0F, 9.0F, 0.0F, 0.0F, 0.0873F));

		PartDefinition head_r2 = head.addOrReplaceChild("head_r2", CubeListBuilder.create().texOffs(94, 88).addBox(12.9F, -4.0F, -5.0F, 10.1F, 4.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.35F, -4.0F, 9.0F, 0.0F, 0.0F, 0.0873F));

		PartDefinition head_r3 = head.addOrReplaceChild("head_r3", CubeListBuilder.create().texOffs(102, 0).addBox(12.53F, -5.0F, -3.0F, 11.31F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.6F, -5.5F, 9.0F, 0.0F, 0.0F, 0.1745F));

		PartDefinition head_r4 = head.addOrReplaceChild("head_r4", CubeListBuilder.create().texOffs(0, 102).addBox(13.0F, -5.3F, -4.25F, 11.0F, 3.3F, 10.5F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -3.0F, 9.0F, 0.0F, 0.0F, 0.0873F));

		PartDefinition body = bone.addOrReplaceChild("body", CubeListBuilder.create().texOffs(1, 1).addBox(-15.0F, -10.0F, 0.0F, 26.0F, 14.0F, 24.0F, new CubeDeformation(0.0F))
		.texOffs(1, 68).addBox(-16.0F, -12.0F, 3.0F, 28.0F, 16.0F, 18.0F, new CubeDeformation(0.0F))
		.texOffs(95, 68).addBox(-16.5F, 4.0F, 2.6F, 29.0F, 2.0F, 18.8F, new CubeDeformation(0.0F))
		.texOffs(1, 40).addBox(-15.5F, 4.0F, -0.5F, 27.0F, 2.0F, 25.0F, new CubeDeformation(0.0F))
		.texOffs(22, 119).addBox(-14.0F, -1.0F, -1.0F, 10.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(22, 119).addBox(0.0F, -1.0F, -1.0F, 10.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -10.0F, 0.0F));

		PartDefinition body_r1 = body.addOrReplaceChild("body_r1", CubeListBuilder.create().texOffs(22, 119).addBox(10.0F, 3.0F, -1.0F, 10.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, -4.0F, 24.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition body_r2 = body.addOrReplaceChild("body_r2", CubeListBuilder.create().texOffs(22, 119).addBox(10.0F, 3.0F, -1.0F, 10.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(20.0F, -4.0F, 24.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition cuerno_lateral_delantero_der = body.addOrReplaceChild("cuerno_lateral_delantero_der", CubeListBuilder.create().texOffs(126, 104).addBox(9.0F, -6.0F, 19.0F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-16.5F, -1.0F, 11.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition cuerno_lateral_delantero_der_r1 = cuerno_lateral_delantero_der.addOrReplaceChild("cuerno_lateral_delantero_der_r1", CubeListBuilder.create().texOffs(34, 138).addBox(-3.0F, 9.0F, 19.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.25F, -14.05F, 1.5F, 0.0F, 0.0F, -0.3491F));

		PartDefinition cuerno_lateral_delantero_der_r2 = cuerno_lateral_delantero_der.addOrReplaceChild("cuerno_lateral_delantero_der_r2", CubeListBuilder.create().texOffs(130, 134).addBox(-3.0F, 8.0F, 19.0F, 5.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(12.65F, -12.85F, 1.0F, 0.0F, 0.0F, -0.2182F));

		PartDefinition cuerno_lateral_trasero_der2 = body.addOrReplaceChild("cuerno_lateral_trasero_der2", CubeListBuilder.create().texOffs(126, 114).addBox(9.0F, -6.0F, 19.0F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-30.5F, -3.0F, 11.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition cuerno_lateral_trasero_der2_r1 = cuerno_lateral_trasero_der2.addOrReplaceChild("cuerno_lateral_trasero_der2_r1", CubeListBuilder.create().texOffs(44, 138).addBox(-3.0F, 9.0F, 19.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.25F, -14.05F, 1.5F, 0.0F, 0.0F, -0.3491F));

		PartDefinition cuerno_lateral_trasero_der2_r2 = cuerno_lateral_trasero_der2.addOrReplaceChild("cuerno_lateral_trasero_der2_r2", CubeListBuilder.create().texOffs(136, 12).addBox(-3.0F, 8.0F, 19.0F, 5.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(12.65F, -12.85F, 1.0F, 0.0F, 0.0F, -0.2182F));

		PartDefinition cuerno_lateral_delantero_iz = body.addOrReplaceChild("cuerno_lateral_delantero_iz", CubeListBuilder.create().texOffs(86, 121).addBox(9.0F, -6.0F, 19.0F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(26.5F, -1.0F, 13.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition cuerno_lateral_delantero_iz_r1 = cuerno_lateral_delantero_iz.addOrReplaceChild("cuerno_lateral_delantero_iz_r1", CubeListBuilder.create().texOffs(32, 115).addBox(-3.0F, 9.0F, 19.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.25F, -14.15F, 1.5F, 0.0F, 0.0F, -0.3491F));

		PartDefinition cuerno_lateral_delantero_iz_r2 = cuerno_lateral_delantero_iz.addOrReplaceChild("cuerno_lateral_delantero_iz_r2", CubeListBuilder.create().texOffs(98, 131).addBox(-3.0F, 8.0F, 19.0F, 5.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(12.65F, -12.9F, 1.0F, 0.0F, 0.0F, -0.2182F));

		PartDefinition cuerno_lateral_trasero_iz = body.addOrReplaceChild("cuerno_lateral_trasero_iz", CubeListBuilder.create().texOffs(0, 124).addBox(9.0F, -6.0F, 19.0F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(12.5F, -3.0F, 13.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition cuerno_lateral_trasero_iz_r1 = cuerno_lateral_trasero_iz.addOrReplaceChild("cuerno_lateral_trasero_iz_r1", CubeListBuilder.create().texOffs(24, 138).addBox(-3.0F, 9.0F, 19.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.25F, -14.15F, 1.5F, 0.0F, 0.0F, -0.3491F));

		PartDefinition cuerno_lateral_trasero_iz_r2 = cuerno_lateral_trasero_iz.addOrReplaceChild("cuerno_lateral_trasero_iz_r2", CubeListBuilder.create().texOffs(114, 134).addBox(-3.0F, 8.0F, 19.0F, 5.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(12.65F, -12.9F, 1.0F, 0.0F, 0.0F, -0.2182F));

		PartDefinition cuerno_delantero_derecho = body.addOrReplaceChild("cuerno_delantero_derecho", CubeListBuilder.create().texOffs(66, 121).addBox(9.0F, -16.0F, 0.0F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 9.0F, 1.0F));

		PartDefinition cuerno_delantero_derecho_r1 = cuerno_delantero_derecho.addOrReplaceChild("cuerno_delantero_derecho_r1", CubeListBuilder.create().texOffs(82, 131).addBox(-3.0F, 8.0F, 19.0F, 5.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(12.65F, -23.0F, -18.0F, 0.0F, 0.0F, -0.2182F));

		PartDefinition cuerno_delantero_derecho_r2 = cuerno_delantero_derecho.addOrReplaceChild("cuerno_delantero_derecho_r2", CubeListBuilder.create().texOffs(22, 115).addBox(-3.0F, 9.0F, 19.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.25F, -24.25F, -17.5F, 0.0F, 0.0F, -0.3491F));

		PartDefinition cuerno_trasero_central = body.addOrReplaceChild("cuerno_trasero_central", CubeListBuilder.create().texOffs(126, 124).addBox(9.0F, -16.0F, 0.0F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, 8.0F, 14.5F, 0.0F, 3.1416F, 0.0F));

		PartDefinition cuerno_trasero_central_r1 = cuerno_trasero_central.addOrReplaceChild("cuerno_trasero_central_r1", CubeListBuilder.create().texOffs(136, 18).addBox(-3.0F, 8.0F, 19.0F, 5.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(12.65F, -23.0F, -18.0F, 0.0F, 0.0F, -0.2182F));

		PartDefinition cuerno_trasero_central_r2 = cuerno_trasero_central.addOrReplaceChild("cuerno_trasero_central_r2", CubeListBuilder.create().texOffs(138, 88).addBox(-3.0F, 9.0F, 19.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.25F, -24.25F, -17.5F, 0.0F, 0.0F, -0.3491F));

		PartDefinition cuerno_delantero_izquierdo = body.addOrReplaceChild("cuerno_delantero_izquierdo", CubeListBuilder.create().texOffs(106, 121).addBox(9.0F, -16.0F, 16.0F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 9.0F, 2.0F));

		PartDefinition cuerno_delantero_izquierdo_r1 = cuerno_delantero_izquierdo.addOrReplaceChild("cuerno_delantero_izquierdo_r1", CubeListBuilder.create().texOffs(14, 138).addBox(-3.0F, 9.0F, 19.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.25F, -24.25F, -1.5F, 0.0F, 0.0F, -0.3491F));

		PartDefinition cuerno_delantero_izquierdo_r2 = cuerno_delantero_izquierdo.addOrReplaceChild("cuerno_delantero_izquierdo_r2", CubeListBuilder.create().texOffs(134, 32).addBox(-3.0F, 8.0F, 19.0F, 5.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(12.65F, -23.0F, -2.0F, 0.0F, 0.0F, -0.2182F));

		PartDefinition cuerno_superior_izquierdo = body.addOrReplaceChild("cuerno_superior_izquierdo", CubeListBuilder.create().texOffs(20, 128).addBox(9.0F, -16.0F, 16.0F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, 1.0F, 23.0F, 0.0F, 3.1416F, 1.5708F));

		PartDefinition cuerno_superior_izquierdo_r1 = cuerno_superior_izquierdo.addOrReplaceChild("cuerno_superior_izquierdo_r1", CubeListBuilder.create().texOffs(138, 92).addBox(-3.0F, 9.0F, 19.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.25F, -24.25F, -1.5F, 0.0F, 0.0F, -0.3491F));

		PartDefinition cuerno_superior_izquierdo_r2 = cuerno_superior_izquierdo.addOrReplaceChild("cuerno_superior_izquierdo_r2", CubeListBuilder.create().texOffs(60, 137).addBox(-3.0F, 8.0F, 19.0F, 5.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(12.65F, -23.0F, -2.0F, 0.0F, 0.0F, -0.2182F));

		PartDefinition cuerno_superior_izquierdo2 = body.addOrReplaceChild("cuerno_superior_izquierdo2", CubeListBuilder.create().texOffs(40, 128).addBox(9.0F, -16.0F, 16.0F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, 1.0F, 38.0F, 0.0F, 3.1416F, 1.5708F));

		PartDefinition cuerno_superior_izquierdo2_r1 = cuerno_superior_izquierdo2.addOrReplaceChild("cuerno_superior_izquierdo2_r1", CubeListBuilder.create().texOffs(138, 96).addBox(-3.0F, 9.0F, 19.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.25F, -24.25F, -1.5F, 0.0F, 0.0F, -0.3491F));

		PartDefinition cuerno_superior_izquierdo2_r2 = cuerno_superior_izquierdo2.addOrReplaceChild("cuerno_superior_izquierdo2_r2", CubeListBuilder.create().texOffs(76, 137).addBox(-3.0F, 8.0F, 19.0F, 5.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(12.65F, -23.0F, -2.0F, 0.0F, 0.0F, -0.2182F));

		PartDefinition cuerno_superior_izquierdo3 = body.addOrReplaceChild("cuerno_superior_izquierdo3", CubeListBuilder.create().texOffs(128, 57).addBox(9.0F, -16.0F, 16.0F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-24.0F, -1.0F, 30.5F, 0.0F, 3.1416F, 1.5708F));

		PartDefinition cuerno_superior_izquierdo3_r1 = cuerno_superior_izquierdo3.addOrReplaceChild("cuerno_superior_izquierdo3_r1", CubeListBuilder.create().texOffs(138, 100).addBox(-3.0F, 9.0F, 19.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.25F, -24.25F, -1.5F, 0.0F, 0.0F, -0.3491F));

		PartDefinition cuerno_superior_izquierdo3_r2 = cuerno_superior_izquierdo3.addOrReplaceChild("cuerno_superior_izquierdo3_r2", CubeListBuilder.create().texOffs(92, 137).addBox(-3.0F, 8.0F, 19.0F, 5.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(12.65F, -23.0F, -2.0F, 0.0F, 0.0F, -0.2182F));

		PartDefinition front_right_leg = bone.addOrReplaceChild("front_right_leg", CubeListBuilder.create().texOffs(70, 104).addBox(-5.5F, -7.0F, -3.75F, 7.0F, 10.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(132, 41).addBox(1.5F, 1.0F, -2.75F, 1.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(7.0F, -3.0F, -1.25F));

		PartDefinition front_left_leg = bone.addOrReplaceChild("front_left_leg", CubeListBuilder.create().texOffs(106, 24).addBox(13.0F, 3.0F, -4.0F, 7.0F, 10.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(132, 49).addBox(20.0F, 11.0F, -4.0F, 1.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(132, 49).addBox(19.5F, 9.0F, -4.0F, 1.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-11.5F, -13.0F, 26.0F));

		PartDefinition back_right_leg = bone.addOrReplaceChild("back_right_leg", CubeListBuilder.create().texOffs(98, 104).addBox(-11.75F, 1.5F, -16.5F, 7.0F, 10.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(134, 24).addBox(-4.75F, 9.5F, -15.5F, 1.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.75F, -11.5F, 11.5F));

		PartDefinition back_left_leg = bone.addOrReplaceChild("back_left_leg", CubeListBuilder.create().texOffs(42, 102).addBox(-2.5F, 4.0F, -5.0F, 7.0F, 10.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(0, 134).addBox(4.5F, 12.0F, -5.0F, 1.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-10.0F, -14.0F, 27.0F));

		PartDefinition tail = bone.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(60, 131).addBox(-21.5F, -6.5F, 10.0F, 7.0F, 2.5F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.applyHeadRotation(netHeadYaw, headPitch, ageInTicks);

        this.animateWalk(ModAnimationDefinitions.WALK_TORTUGA, limbSwing, limbSwingAmount, 2f, 2.5f);
        this.animate(((TortugaInfernalEntity) entity).idleAnimationState, ModAnimationDefinitions.IDLE_TORTUGA, ageInTicks, 1f);
        this.animate(((TortugaInfernalEntity) entity).attackAnimationState, ModAnimationDefinitions.ATTACK_TORTUGA, ageInTicks, 1f);
	}

    private void applyHeadRotation(float pNetHeadYaw, float pHeadPitch, float pAgeInTicks) {
        pNetHeadYaw = Mth.clamp(pNetHeadYaw, -30.0F, 30.0F);
        pHeadPitch = Mth.clamp(pHeadPitch, -25.0F, 45.0F);

        this.head.yRot = pNetHeadYaw * ((float)Math.PI / 180F);
        this.head.xRot = pHeadPitch * ((float)Math.PI / 180F);
    }

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

    @Override
    public ModelPart root() {
        return bone;
    }
}