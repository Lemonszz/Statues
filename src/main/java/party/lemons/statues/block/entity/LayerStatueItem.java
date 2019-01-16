package party.lemons.statues.block.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import party.lemons.statues.statue.EntityStatuePlayer;

public class LayerStatueItem extends LayerHeldItem
{
	public LayerStatueItem(RenderLivingBase<?> livingEntityRendererIn)
	{
		super(livingEntityRendererIn);
	}

	public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		if(!(entitylivingbaseIn instanceof EntityStatuePlayer))
		{
			super.doRenderLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
			return;
		}

		EntityStatuePlayer player = (EntityStatuePlayer) entitylivingbaseIn;

		boolean flag = entitylivingbaseIn.getPrimaryHand() == EnumHandSide.RIGHT;
		ItemStack itemstack = flag ? entitylivingbaseIn.getHeldItemOffhand() : entitylivingbaseIn.getHeldItemMainhand();
		ItemStack itemstack1 = flag ? entitylivingbaseIn.getHeldItemMainhand() : entitylivingbaseIn.getHeldItemOffhand();

		if (!itemstack.isEmpty() || !itemstack1.isEmpty())
		{
			GlStateManager.pushMatrix();

			this.renderHeldItem(entitylivingbaseIn, itemstack1, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, EnumHandSide.RIGHT, player.info.itemLeftA);
			this.renderHeldItem(entitylivingbaseIn, itemstack, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, EnumHandSide.LEFT, player.info.itemRightA);
			GlStateManager.popMatrix();
		}
	}

	private void renderHeldItem(EntityLivingBase p_188358_1_, ItemStack p_188358_2_, ItemCameraTransforms.TransformType p_188358_3_, EnumHandSide handSide, float angle)
	{
		if (!p_188358_2_.isEmpty())
		{
			GlStateManager.pushMatrix();


			GlStateManager.translate(0.0f, 0.0f, (0.5f-Math.abs(0.5f-angle)));
			GlStateManager.translate(0.0f, 0.25f*angle, 0.0f);
			GlStateManager.rotate(angle*180f, 1.0f, 0.0f, 0.0f);

			this.translateToHand(handSide);
			GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);

			boolean flag = handSide == EnumHandSide.LEFT;
			GlStateManager.translate((float)(flag ? -1 : 1) / 16.0F, 0.125F, -0.625F);

			Minecraft.getMinecraft().getItemRenderer().renderItemSide(p_188358_1_, p_188358_2_, p_188358_3_, flag);
			GlStateManager.popMatrix();
		}
	}
}
