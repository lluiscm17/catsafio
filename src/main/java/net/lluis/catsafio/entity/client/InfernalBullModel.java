package net.lluis.catsafio.entity.client;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.lluis.catsafio.entity.animations.ModAnimationDefinitions;
import net.lluis.catsafio.entity.custom.InfernalBullEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class InfernalBullModel<T extends Entity> extends HierarchicalModel<T> {
	private final ModelPart Infernalbull;
	private final ModelPart rightleg4;
	private final ModelPart back_right_leg3;
	private final ModelPart back_right_leg2;
	private final ModelPart leftleg4;
	private final ModelPart back_left_leg2;
	private final ModelPart back_left_leg3;
	private final ModelPart bone;
	private final ModelPart body2;
	private final ModelPart Cuerpo;
	private final ModelPart body;
	private final ModelPart Montura;
	private final ModelPart pelos;
	private final ModelPart Cabeza;
	private final ModelPart MaxSup;
	private final ModelPart CuernoIzq;
	private final ModelPart CuernoDer;
	private final ModelPart DientesSup;
	private final ModelPart MaxInf;
	private final ModelPart ColIzq;
	private final ModelPart ColDer;
	private final ModelPart DientesInf;
	private final ModelPart leftleg3;
	private final ModelPart back_left_leg;
	private final ModelPart rightleg3;
	private final ModelPart back_right_leg;

	public InfernalBullModel(ModelPart root) {
		this.Infernalbull = root.getChild("Infernalbull");
		this.rightleg4 = this.Infernalbull.getChild("rightleg4");
		this.back_right_leg3 = this.rightleg4.getChild("back_right_leg3");
		this.back_right_leg2 = this.rightleg4.getChild("back_right_leg2");
		this.leftleg4 = this.Infernalbull.getChild("leftleg4");
		this.back_left_leg2 = this.leftleg4.getChild("back_left_leg2");
		this.back_left_leg3 = this.leftleg4.getChild("back_left_leg3");
		this.bone = this.Infernalbull.getChild("bone");
		this.body2 = this.bone.getChild("body2");
		this.Cuerpo = this.body2.getChild("Cuerpo");
		this.body = this.Cuerpo.getChild("body");
		this.Montura = this.Cuerpo.getChild("Montura");
		this.pelos = this.Cuerpo.getChild("pelos");
		this.Cabeza = Infernalbull.getChild("bone").getChild("body2").getChild("Cabeza");
		this.MaxSup = this.Cabeza.getChild("MaxSup");
		this.CuernoIzq = this.MaxSup.getChild("CuernoIzq");
		this.CuernoDer = this.MaxSup.getChild("CuernoDer");
		this.DientesSup = this.MaxSup.getChild("DientesSup");
		this.MaxInf = this.Cabeza.getChild("MaxInf");
		this.ColIzq = this.MaxInf.getChild("ColIzq");
		this.ColDer = this.MaxInf.getChild("ColDer");
		this.DientesInf = this.MaxInf.getChild("DientesInf");
		this.leftleg3 = this.bone.getChild("leftleg3");
		this.back_left_leg = this.leftleg3.getChild("back_left_leg");
		this.rightleg3 = this.bone.getChild("rightleg3");
		this.back_right_leg = this.rightleg3.getChild("back_right_leg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Infernalbull = partdefinition.addOrReplaceChild("Infernalbull", CubeListBuilder.create(), PartPose.offset(0.0F, 2.0F, 0.0F));

		PartDefinition rightleg4 = Infernalbull.addOrReplaceChild("rightleg4", CubeListBuilder.create(), PartPose.offset(-6.0F, 6.8002F, 11.2864F));

		PartDefinition rightleg4_r1 = rightleg4.addOrReplaceChild("rightleg4_r1", CubeListBuilder.create().texOffs(78, 0).addBox(-5.0F, -6.5F, -6.0F, 10.0F, 13.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -0.2854F, 0.5176F, 0.3054F, 0.0F, 0.0F));

		PartDefinition back_right_leg3 = rightleg4.addOrReplaceChild("back_right_leg3", CubeListBuilder.create(), PartPose.offset(-2.0F, 5.12F, 7.7231F));

		PartDefinition back_right_leg3_r1 = back_right_leg3.addOrReplaceChild("back_right_leg3_r1", CubeListBuilder.create().texOffs(116, 113).addBox(-4.0F, -6.5F, -5.0F, 8.0F, 9.0F, 10.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.0036F, 0.0F, 0.0F));

		PartDefinition back_right_leg2 = rightleg4.addOrReplaceChild("back_right_leg2", CubeListBuilder.create().texOffs(82, 113).addBox(-10.0F, -45.3298F, -20.7405F, 8.0F, 13.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(78, 25).addBox(-10.0F, -34.3298F, -22.7405F, 8.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 47.6998F, 24.2136F));

		PartDefinition leftleg4 = Infernalbull.addOrReplaceChild("leftleg4", CubeListBuilder.create(), PartPose.offset(6.0F, 6.8002F, 11.2864F));

		PartDefinition leftleg4_r1 = leftleg4.addOrReplaceChild("leftleg4_r1", CubeListBuilder.create().texOffs(78, 0).mirror().addBox(-5.0F, -6.5F, -6.0F, 10.0F, 13.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.0F, -0.2854F, 0.5176F, 0.3054F, 0.0F, 0.0F));

		PartDefinition back_left_leg2 = leftleg4.addOrReplaceChild("back_left_leg2", CubeListBuilder.create().texOffs(82, 113).mirror().addBox(2.0F, -45.3298F, -20.7405F, 8.0F, 13.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(78, 25).mirror().addBox(2.0F, -34.3298F, -22.7405F, 8.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-4.0F, 47.6998F, 24.2136F));

		PartDefinition back_left_leg3 = leftleg4.addOrReplaceChild("back_left_leg3", CubeListBuilder.create(), PartPose.offset(2.0F, 5.12F, 7.7231F));

		PartDefinition back_left_leg3_r1 = back_left_leg3.addOrReplaceChild("back_left_leg3_r1", CubeListBuilder.create().texOffs(116, 113).mirror().addBox(-4.0F, -6.5F, -5.0F, 8.0F, 9.0F, 10.0F, new CubeDeformation(0.1F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.0036F, 0.0F, 0.0F));

		PartDefinition bone = Infernalbull.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0F, 7.0F, 13.0F));

		PartDefinition body2 = bone.addOrReplaceChild("body2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -4.0F, -2.5F, 0.1745F, 0.0F, 0.0F));

		PartDefinition Cuerpo = body2.addOrReplaceChild("Cuerpo", CubeListBuilder.create(), PartPose.offset(0.0F, -7.1217F, -26.1494F));

		PartDefinition Cuerpo_r1 = Cuerpo.addOrReplaceChild("Cuerpo_r1", CubeListBuilder.create().texOffs(62, 53).addBox(-7.0F, -4.0054F, -5.9666F, 14.0F, 19.0F, 12.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0.0F, -0.9331F, 21.9408F, -0.3927F, 0.0F, 0.0F));

		PartDefinition Cuerpo_r2 = Cuerpo.addOrReplaceChild("Cuerpo_r2", CubeListBuilder.create().texOffs(0, 61).addBox(-7.0F, -6.847F, 0.2544F, 14.0F, 14.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.4674F, 19.5892F, -0.48F, 0.0F, 0.0F));

		PartDefinition body = Cuerpo.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 29).addBox(-8.0F, -7.2352F, -6.946F, 16.0F, 17.0F, 15.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 1.0798F, 8.7405F));

		PartDefinition body_r1 = body.addOrReplaceChild("body_r1", CubeListBuilder.create().texOffs(111, 89).addBox(0.0F, -5.0F, -4.5F, 0.0F, 10.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -6.1912F, -2.6597F, 0.0F, 0.0F, -0.4363F));

		PartDefinition body_r2 = body.addOrReplaceChild("body_r2", CubeListBuilder.create().texOffs(111, 89).mirror().addBox(0.0F, -5.0F, -4.5F, 0.0F, 10.0F, 14.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.0F, -6.1912F, -2.6597F, 0.0F, 0.0F, 0.4363F));

		PartDefinition body_r3 = body.addOrReplaceChild("body_r3", CubeListBuilder.create().texOffs(111, 89).addBox(0.0F, -5.0F, -4.5F, 0.0F, 10.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.75F, -8.4412F, -2.6597F, 0.0F, 0.0F, -0.4363F));

		PartDefinition body_r4 = body.addOrReplaceChild("body_r4", CubeListBuilder.create().texOffs(111, 89).addBox(0.0F, -3.0F, -4.5F, 0.0F, 8.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, -7.1912F, -2.6597F, 0.0F, 0.0F, -0.8727F));

		PartDefinition body_r5 = body.addOrReplaceChild("body_r5", CubeListBuilder.create().texOffs(111, 89).mirror().addBox(0.0F, -5.0F, -4.5F, 0.0F, 10.0F, 14.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.75F, -9.1912F, -2.6597F, 0.0F, 0.0F, 0.4363F));

		PartDefinition body_r6 = body.addOrReplaceChild("body_r6", CubeListBuilder.create().texOffs(111, 89).mirror().addBox(0.0F, -3.0F, -4.5F, 0.0F, 8.0F, 14.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.5F, -7.1912F, -2.6597F, 0.0F, 0.0F, 0.8727F));

		PartDefinition body_r7 = body.addOrReplaceChild("body_r7", CubeListBuilder.create().texOffs(38, 108).addBox(-7.0F, -4.0F, -5.0F, 14.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(0.0F, -1.0798F, -8.7405F, -0.1745F, 0.0F, 0.0F));

		PartDefinition Montura = Cuerpo.addOrReplaceChild("Montura", CubeListBuilder.create(), PartPose.offset(0.0F, -0.9331F, 21.9408F));

		PartDefinition Montura_r1 = Montura.addOrReplaceChild("Montura_r1", CubeListBuilder.create().texOffs(66, 60).mirror().addBox(7.651F, 1.1736F, -1.8969F, 0.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.2119F, 0.0587F, -0.2602F, -0.2975F, -0.8335F));

		PartDefinition Montura_r2 = Montura.addOrReplaceChild("Montura_r2", CubeListBuilder.create().texOffs(66, 60).addBox(-7.651F, 1.1736F, -1.8969F, 0.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.2119F, 0.0587F, -0.2602F, 0.2975F, 0.8335F));

		PartDefinition Montura_r3 = Montura.addOrReplaceChild("Montura_r3", CubeListBuilder.create().texOffs(110, 0).addBox(-6.0F, -5.4566F, -5.7247F, 12.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
		.texOffs(113, 2).addBox(-5.0F, -5.9946F, -4.5334F, 10.0F, 1.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition pelos = Cuerpo.addOrReplaceChild("pelos", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -6.9202F, 16.2405F, -0.0436F, 0.0F, 0.0F));

		PartDefinition pelos_r1 = pelos.addOrReplaceChild("pelos_r1", CubeListBuilder.create().texOffs(0, 121).addBox(-0.3211F, -2.3113F, -4.3073F, 0.0F, 8.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 4.8088F, 16.8403F, -0.3186F, -0.1451F, -0.413F));

		PartDefinition pelos_r2 = pelos.addOrReplaceChild("pelos_r2", CubeListBuilder.create().texOffs(119, 97).addBox(0.0F, -5.0F, -4.5F, 0.0F, 10.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.8088F, -26.1597F, -0.1188F, -0.0552F, -0.4331F));

		PartDefinition pelos_r3 = pelos.addOrReplaceChild("pelos_r3", CubeListBuilder.create().texOffs(119, 97).mirror().addBox(0.0F, -5.0F, -4.5F, 0.0F, 10.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 3.8088F, -26.1597F, -0.1188F, 0.0552F, 0.4331F));

		PartDefinition pelos_r4 = pelos.addOrReplaceChild("pelos_r4", CubeListBuilder.create().texOffs(112, 90).mirror().addBox(0.0F, -5.0F, -4.5F, 0.0F, 10.0F, 13.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.0F, 1.8088F, -22.9097F, -0.1188F, 0.0552F, 0.4331F));

		PartDefinition pelos_r5 = pelos.addOrReplaceChild("pelos_r5", CubeListBuilder.create().texOffs(112, 90).mirror().addBox(0.0F, -5.0F, -4.5F, 0.0F, 10.0F, 13.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.0F, 3.8088F, -23.1597F, -0.1188F, 0.0552F, 0.4331F));

		PartDefinition pelos_r6 = pelos.addOrReplaceChild("pelos_r6", CubeListBuilder.create().texOffs(112, 90).mirror().addBox(0.0F, -3.0F, -4.5F, 0.0F, 8.0F, 13.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.5F, 2.8088F, -23.1597F, -0.0844F, 0.1002F, 0.8684F));

		PartDefinition pelos_r7 = pelos.addOrReplaceChild("pelos_r7", CubeListBuilder.create().texOffs(112, 90).addBox(0.0F, -5.0F, -4.5F, 0.0F, 10.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.75F, 0.8088F, -22.9097F, -0.1188F, -0.0552F, -0.4331F));

		PartDefinition pelos_r8 = pelos.addOrReplaceChild("pelos_r8", CubeListBuilder.create().texOffs(112, 90).addBox(0.0F, -5.0F, -4.5F, 0.0F, 10.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 3.8088F, -23.1597F, -0.1188F, -0.0552F, -0.4331F));

		PartDefinition pelos_r9 = pelos.addOrReplaceChild("pelos_r9", CubeListBuilder.create().texOffs(112, 90).addBox(0.0F, -3.0F, -4.5F, 0.0F, 8.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, 2.8088F, -23.1597F, -0.0844F, -0.1002F, -0.8684F));

		PartDefinition pelos_r10 = pelos.addOrReplaceChild("pelos_r10", CubeListBuilder.create().texOffs(115, 93).addBox(-0.276F, -4.4082F, 0.7544F, 0.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 5.5588F, 12.3402F, -0.3999F, -0.1796F, -0.3999F));

		PartDefinition pelos_r11 = pelos.addOrReplaceChild("pelos_r11", CubeListBuilder.create().texOffs(115, 93).addBox(0.0F, -5.0F, -0.5F, 0.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.25F, 4.5588F, 12.8402F, -0.3999F, -0.1796F, -0.3999F));

		PartDefinition pelos_r12 = pelos.addOrReplaceChild("pelos_r12", CubeListBuilder.create().texOffs(115, 93).mirror().addBox(0.276F, -4.4082F, 0.7544F, 0.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.25F, 4.5588F, 12.8402F, -0.3999F, 0.1796F, 0.3999F));

		PartDefinition pelos_r13 = pelos.addOrReplaceChild("pelos_r13", CubeListBuilder.create().texOffs(115, 93).mirror().addBox(0.276F, -4.4082F, 0.7544F, 0.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.0F, 5.5588F, 12.3402F, -0.3999F, 0.1796F, 0.3999F));

		PartDefinition pelos_r14 = pelos.addOrReplaceChild("pelos_r14", CubeListBuilder.create().texOffs(115, 93).mirror().addBox(0.5002F, -2.5802F, 0.7544F, 0.0F, 8.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.5F, 4.5588F, 12.3402F, -0.2912F, 0.3297F, 0.8239F));

		PartDefinition pelos_r15 = pelos.addOrReplaceChild("pelos_r15", CubeListBuilder.create().texOffs(115, 93).addBox(-0.5002F, -2.5802F, 0.7544F, 0.0F, 8.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, 4.5588F, 12.3402F, -0.2912F, -0.3297F, -0.8239F));

		PartDefinition Cabeza = body2.addOrReplaceChild("Cabeza", CubeListBuilder.create(), PartPose.offset(0.0F, -2.0419F, -25.4089F));

		PartDefinition MaxSup = Cabeza.addOrReplaceChild("MaxSup", CubeListBuilder.create(), PartPose.offset(7.3416F, -9.6843F, -8.9598F));

		PartDefinition MaxSup_r1 = MaxSup.addOrReplaceChild("MaxSup_r1", CubeListBuilder.create().texOffs(114, 75).addBox(-4.0F, 0.0F, -7.5F, 8.0F, 5.0F, 2.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(-7.3416F, 1.9395F, -2.5195F, -0.1745F, 0.0F, 0.0F));

		PartDefinition MaxSup_r2 = MaxSup.addOrReplaceChild("MaxSup_r2", CubeListBuilder.create().texOffs(62, 29).addBox(-7.0F, -4.0F, -2.5F, 14.0F, 8.0F, 16.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(-7.3416F, 2.4395F, -5.0195F, -0.1745F, 0.0F, 0.0F));

		PartDefinition CuernoIzq = MaxSup.addOrReplaceChild("CuernoIzq", CubeListBuilder.create(), PartPose.offsetAndRotation(1.0F, -1.0F, -3.5F, 0.162F, 0.1342F, 0.4284F));

		PartDefinition CuernoIzq_r1 = CuernoIzq.addOrReplaceChild("CuernoIzq_r1", CubeListBuilder.create().texOffs(74, 124).mirror().addBox(-1.4211F, -16.7877F, 4.8665F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.3832F, -0.3221F, -0.0319F));

		PartDefinition CuernoIzq_r2 = CuernoIzq.addOrReplaceChild("CuernoIzq_r2", CubeListBuilder.create().texOffs(122, 45).mirror().addBox(-1.9506F, -11.6567F, 2.3796F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.2087F, -0.3221F, -0.0319F));

		PartDefinition CuernoIzq_r3 = CuernoIzq.addOrReplaceChild("CuernoIzq_r3", CubeListBuilder.create().texOffs(22, 121).mirror().addBox(-2.0189F, -8.3399F, 0.0682F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.0348F, -0.3658F, -0.163F));

		PartDefinition CuernoIzq_r4 = CuernoIzq.addOrReplaceChild("CuernoIzq_r4", CubeListBuilder.create().texOffs(38, 92).mirror().addBox(-2.9394F, -3.0035F, -0.2772F, 5.0F, 10.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.75F, 0.0F, -1.5F, 0.5912F, -0.5019F, -0.0442F));

		PartDefinition CuernoDer = MaxSup.addOrReplaceChild("CuernoDer", CubeListBuilder.create(), PartPose.offsetAndRotation(-15.6831F, -1.0F, -3.5F, 0.162F, -0.1342F, -0.4284F));

		PartDefinition CuernoDer_r1 = CuernoDer.addOrReplaceChild("CuernoDer_r1", CubeListBuilder.create().texOffs(74, 124).addBox(-0.5789F, -16.7877F, 4.8665F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.3832F, 0.3221F, 0.0319F));

		PartDefinition CuernoDer_r2 = CuernoDer.addOrReplaceChild("CuernoDer_r2", CubeListBuilder.create().texOffs(122, 45).addBox(-1.0494F, -11.6567F, 2.3796F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.2087F, 0.3221F, 0.0319F));

		PartDefinition CuernoDer_r3 = CuernoDer.addOrReplaceChild("CuernoDer_r3", CubeListBuilder.create().texOffs(22, 121).addBox(-1.9811F, -8.3399F, 0.0682F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.0348F, 0.3658F, 0.163F));

		PartDefinition CuernoDer_r4 = CuernoDer.addOrReplaceChild("CuernoDer_r4", CubeListBuilder.create().texOffs(38, 92).addBox(-2.0606F, -3.0035F, -0.2772F, 5.0F, 10.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.75F, 0.0F, -1.5F, 0.5912F, 0.5019F, 0.0442F));

		PartDefinition DientesSup = MaxSup.addOrReplaceChild("DientesSup", CubeListBuilder.create(), PartPose.offsetAndRotation(-7.4249F, 8.0919F, -4.8773F, 0.0F, 0.0F, -3.1416F));

		PartDefinition DientesSup_r1 = DientesSup.addOrReplaceChild("DientesSup_r1", CubeListBuilder.create().texOffs(47, 17).addBox(-7.0F, -1.0F, -16.5F, 0.0F, 3.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(47, 17).addBox(6.0F, -1.0F, -16.5F, 0.0F, 3.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0833F, -2.663F, 12.8899F, 0.1745F, 0.0F, 0.0F));

		PartDefinition DientesSup_r2 = DientesSup.addOrReplaceChild("DientesSup_r2", CubeListBuilder.create().texOffs(48, 32).addBox(-20.0F, -1.0F, -16.5F, 13.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(13.0833F, -2.663F, 12.6399F, 0.1745F, 0.0F, 0.0F));

		PartDefinition MaxInf = Cabeza.addOrReplaceChild("MaxInf", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -0.2554F, -2.1972F, 0.2618F, 0.0F, 0.0F));

		PartDefinition MaxInf_r1 = MaxInf.addOrReplaceChild("MaxInf_r1", CubeListBuilder.create().texOffs(3, 3).addBox(-8.0F, 0.031F, -18.4016F, 16.0F, 6.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1745F, 0.0F, 0.0F));

		PartDefinition ColIzq = MaxInf.addOrReplaceChild("ColIzq", CubeListBuilder.create(), PartPose.offsetAndRotation(8.9577F, -0.3246F, -19.7717F, -0.2436F, 1.1271F, 1.3427F));

		PartDefinition ColIzq_r1 = ColIzq.addOrReplaceChild("ColIzq_r1", CubeListBuilder.create().texOffs(74, 124).addBox(-2.5236F, -17.5959F, 6.2501F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.6161F, 8.8958F, 13.009F, 1.3832F, -0.3221F, -0.0319F));

		PartDefinition ColIzq_r2 = ColIzq.addOrReplaceChild("ColIzq_r2", CubeListBuilder.create().texOffs(122, 45).addBox(-3.0531F, -12.6929F, 3.6018F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.6161F, 8.8958F, 13.009F, 1.2087F, -0.3221F, -0.0319F));

		PartDefinition ColDer = MaxInf.addOrReplaceChild("ColDer", CubeListBuilder.create(), PartPose.offsetAndRotation(-8.9577F, -0.3246F, -19.7717F, -0.2436F, -1.1271F, -1.3427F));

		PartDefinition ColDer_r1 = ColDer.addOrReplaceChild("ColDer_r1", CubeListBuilder.create().texOffs(74, 124).addBox(0.5236F, -17.5959F, 6.2501F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.6161F, 8.8958F, 13.009F, 1.3832F, 0.3221F, 0.0319F));

		PartDefinition ColDer_r2 = ColDer.addOrReplaceChild("ColDer_r2", CubeListBuilder.create().texOffs(122, 45).addBox(0.0531F, -12.6929F, 3.6018F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.6161F, 8.8958F, 13.009F, 1.2087F, 0.3221F, 0.0319F));

		PartDefinition DientesInf = MaxInf.addOrReplaceChild("DientesInf", CubeListBuilder.create(), PartPose.offset(-0.0833F, -2.337F, -11.6399F));

		PartDefinition DientesInf_r1 = DientesInf.addOrReplaceChild("DientesInf_r1", CubeListBuilder.create().texOffs(47, 17).addBox(-7.0F, 2.031F, -17.4016F, 0.0F, 3.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(47, 17).addBox(-21.0F, 2.031F, -17.4016F, 0.0F, 3.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(14.0833F, -4.663F, 23.6399F, 0.1745F, 0.0F, 0.0F));

		PartDefinition DientesInf_r2 = DientesInf.addOrReplaceChild("DientesInf_r2", CubeListBuilder.create().texOffs(47, 17).addBox(-7.0F, 2.031F, -17.4016F, 0.0F, 3.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(47, 32).addBox(-7.0F, 2.031F, -17.4016F, 14.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(47, 17).addBox(7.0F, 2.031F, -17.4016F, 0.0F, 3.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0833F, -2.663F, 11.6399F, 0.1745F, 0.0F, 0.0F));

		PartDefinition DientesInf_r3 = DientesInf.addOrReplaceChild("DientesInf_r3", CubeListBuilder.create().texOffs(47, 17).addBox(-7.0F, 2.031F, -17.4016F, 0.0F, 3.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(47, 17).addBox(5.0F, 2.031F, -17.4016F, 0.0F, 3.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0833F, -2.663F, 12.8899F, 0.1745F, 0.0F, 0.0F));

		PartDefinition DientesInf_r4 = DientesInf.addOrReplaceChild("DientesInf_r4", CubeListBuilder.create().texOffs(48, 32).addBox(-20.0F, 2.031F, -17.4016F, 13.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(13.0833F, -2.663F, 12.6399F, 0.1745F, 0.0F, 0.0F));

		PartDefinition leftleg3 = bone.addOrReplaceChild("leftleg3", CubeListBuilder.create(), PartPose.offset(6.0F, 0.2518F, -23.7994F));

		PartDefinition leftleg3_r1 = leftleg3.addOrReplaceChild("leftleg3_r1", CubeListBuilder.create().texOffs(62, 84).mirror().addBox(-5.0F, -5.5F, -6.0F, 10.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.0F, 1.363F, -0.3966F, 0.7418F, 0.0F, 0.0F));

		PartDefinition back_left_leg = leftleg3.addOrReplaceChild("back_left_leg", CubeListBuilder.create().texOffs(114, 53).mirror().addBox(2.0F, -45.3298F, -29.7405F, 8.0F, 13.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-4.0F, 47.0982F, 23.2994F));

		PartDefinition rightleg3 = bone.addOrReplaceChild("rightleg3", CubeListBuilder.create(), PartPose.offset(-6.0F, 0.2518F, -23.7994F));

		PartDefinition rightleg3_r1 = rightleg3.addOrReplaceChild("rightleg3_r1", CubeListBuilder.create().texOffs(62, 84).addBox(-5.0F, -5.5F, -6.0F, 10.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 1.363F, -0.3966F, 0.7418F, 0.0F, 0.0F));

		PartDefinition back_right_leg = rightleg3.addOrReplaceChild("back_right_leg", CubeListBuilder.create().texOffs(114, 53).addBox(-10.0F, -45.3298F, -29.7405F, 8.0F, 13.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 47.0982F, 23.2994F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.applyHeadRotation(netHeadYaw, headPitch, ageInTicks);

        this.animateWalk(ModAnimationDefinitions.RUN, limbSwing, limbSwingAmount, 2f, 2.5f);
        this.animate(((InfernalBullEntity) entity).idleAnimationState, ModAnimationDefinitions.IDLE, ageInTicks, 1f);
        this.animate(((InfernalBullEntity) entity).attackAnimationState, ModAnimationDefinitions.CARGA, ageInTicks, 1f);
	}

    private void applyHeadRotation(float pNetHeadYaw, float pHeadPitch, float pAgeInTicks) {
        pNetHeadYaw = Mth.clamp(pNetHeadYaw, -30.0F, 30.0F);
        pHeadPitch = Mth.clamp(pHeadPitch, -25.0F, 45.0F);

        this.Cabeza.yRot = pNetHeadYaw * ((float)Math.PI / 180F);
        this.Cabeza.xRot = pHeadPitch * ((float)Math.PI / 180F);
    }


    @Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Infernalbull.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

    @Override
    public ModelPart root() {
        return Infernalbull;
    }
}