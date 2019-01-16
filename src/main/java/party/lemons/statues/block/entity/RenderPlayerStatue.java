package party.lemons.statues.block.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import party.lemons.statues.statue.EntityStatuePlayer;

import javax.annotation.Nullable;

public class RenderPlayerStatue extends RenderLivingBase<EntityStatuePlayer>
{
	public static final ResourceLocation	steveTextures	= new ResourceLocation("textures/entity/steve.png");

	public RenderPlayerStatue()
	{
		super(Minecraft.getMinecraft().getRenderManager(), new ModelBipedStatue(0, false), 0.5F);

		this.addLayer(new LayerStatueArmor(this));
		this.addLayer(new LayerHeldItem(this));
		this.addLayer(new LayerCustomHead((((ModelBiped)this.getMainModel()).bipedHead)));
		this.addLayer(new LayerElytra(this));
	}

	protected boolean bindEntityTexture(EntityStatuePlayer entity)
	{
		ITextureObject tex = entity.getTextureSkin();
		if(tex == null)
			Minecraft.getMinecraft().renderEngine.bindTexture(steveTextures);
		else
			GlStateManager.bindTexture(entity.getTextureSkin().getGlTextureId());

		return true;
	}

	@Override
	public ModelBase getMainModel()
	{
		return super.getMainModel();
	}

	@Override
	public void doRender(EntityStatuePlayer player, double x, double y, double z, float unk, float frame) {

		GlStateManager.pushMatrix();

		GlStateManager.color(1.0F, 1.0F, 1.0F);
		setupModel(((ModelBipedStatue)getMainModel()),player);

		float voffl= calcHeight(((ModelBiped)getMainModel()).bipedLeftLeg);
		float voffr= calcHeight(((ModelBiped)getMainModel()).bipedRightLeg);
		float voff=voffr<voffl?voffr:voffl;

		GlStateManager.rotate(-45+player.info.bodyA*90, 0.0f, 1.0f, 0.0f);
		GlStateManager.translate(0.0f, -player.renderOffsetY, 0.0f);
		GlStateManager.rotate(-30+player.info.bodyB*60, 1.0f, 0.0f, 0.0f);
		GlStateManager.translate(0.0f, player.renderOffsetY, 0.0f);

		super.doRender(player, x, y - (voff*0.7f), z, unk, frame);

		GlStateManager.popMatrix();
	}



	static final float multiplier=(float) (Math.PI/180);
	void setupModel(ModelBipedStatue model,EntityStatuePlayer player){
		model.bipedRightArm.rotateAngleX =	multiplier * (  0 - 180 * player.info.armRightB);
		model.bipedRightArm.rotateAngleY =	multiplier * ( 90 - 135 * player.info.armRightA);
		model.bipedRightArm.rotateAngleZ =	multiplier * 0;
		model.bipedLeftArm.rotateAngleX =	multiplier * (    + 180 * (1.0f-player.info.armLeftB));
		model.bipedLeftArm.rotateAngleY =	multiplier * (270 - 135 * player.info.armLeftA);
		model.bipedLeftArm.rotateAngleZ =	multiplier * 180;
		model.bipedRightLeg.rotateAngleX =	multiplier * (120 - 240 * player.info.legRightB);
		model.bipedRightLeg.rotateAngleY =	multiplier * ( 90 -  90 * player.info.legRightA);
		model.bipedRightLeg.rotateAngleZ =	0;
		model.bipedLeftLeg.rotateAngleX =	multiplier * (120 - 240 * player.info.legLeftB);
		model.bipedLeftLeg.rotateAngleY =	multiplier * (-90 +  90 * player.info.legLeftA);
		model.bipedLeftLeg.rotateAngleZ =	0;
		model.bipedHead.rotateAngleX =		multiplier * (-45 +  90 * player.info.headB);
		model.bipedHead.rotateAngleY =		multiplier * ( 45 -  90 * player.info.headA);
		model.bipedHead.rotateAngleZ =		0;
		model.bipedHeadwear.rotateAngleX =	model.bipedHead.rotateAngleX;
		model.bipedHeadwear.rotateAngleY =	model.bipedHead.rotateAngleY;
		model.bipedHeadwear.rotateAngleZ =	model.bipedHead.rotateAngleZ;
	}

	@Override
	public void renderName(EntityStatuePlayer entity, double x, double y, double z)
	{
	}

	float calcHeight(ModelRenderer leg){
		double cos=Math.cos(leg.rotateAngleX); if(cos<0) cos=0;
		float res=(float) (1.0f-Math.abs(cos*Math.cos(leg.rotateAngleZ)));
		if(res<0) res=0;
		return res;
	}

	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(EntityStatuePlayer entityStatuePlayer)
	{
		return entityStatuePlayer.getLocationSkin();
	}
}
