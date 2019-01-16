package party.lemons.statues.block.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)

public class LayerStatueArmor extends LayerArmorBase<ModelBiped>
{
	private RenderLivingBase renderer;
	private float alpha = 1.0F;
	private float colorR = 1.0F;
	private float colorG = 1.0F;
	private float colorB = 1.0F;
	private boolean skipRenderGlint;

	public LayerStatueArmor(RenderLivingBase<?> rendererIn)
	{
		super(rendererIn);
		this.renderer = rendererIn;
	}

	protected void initArmor()
	{
		this.modelLeggings = new ModelBiped(0.5F)
		{
			@Override
			public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
			{
				isChild = false;
			}
		};
		this.modelArmor = new ModelBiped(1.0F)
		{
			@Override
			public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
			{
				isChild = false;

			}
		};
	}

	public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		ModelBipedStatue model = (ModelBipedStatue) renderer.getMainModel();

		ModelBase.copyModelAngles(model.bipedLeftLeg, modelLeggings.bipedLeftLeg);
		ModelBase.copyModelAngles(model.bipedRightLeg, modelLeggings.bipedRightLeg);

		ModelBase.copyModelAngles(model.bipedLeftLeg, modelArmor.bipedLeftLeg);
		ModelBase.copyModelAngles(model.bipedRightLeg, modelArmor.bipedRightLeg);

		ModelBase.copyModelAngles(model.bipedBody, modelArmor.bipedBody);
		ModelBase.copyModelAngles(model.bipedLeftArm, modelArmor.bipedLeftArm);
		ModelBase.copyModelAngles(model.bipedRightArm, modelArmor.bipedRightArm);
		ModelBiped.copyModelAngles(model.bipedHead, modelArmor.bipedHead);

		this.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.CHEST);
		this.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.LEGS);
		this.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.FEET);
		this.renderArmorLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, EntityEquipmentSlot.HEAD);

		//super.doRenderLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
	}

	private boolean isLegSlot(EntityEquipmentSlot slotIn)
	{
		return slotIn == EntityEquipmentSlot.LEGS;
	}

	public ModelBiped getModelFromSlot(EntityEquipmentSlot slotIn)
	{
		return (ModelBiped)(this.isLegSlot(slotIn) ? this.modelLeggings : this.modelArmor);
	}


	private void renderArmorLayer(EntityLivingBase entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, EntityEquipmentSlot slotIn)
	{
		ItemStack itemstack = entityLivingBaseIn.getItemStackFromSlot(slotIn);

		if (itemstack.getItem() instanceof ItemArmor)
		{
			ItemArmor itemarmor = (ItemArmor)itemstack.getItem();

			if (itemarmor.getEquipmentSlot() == slotIn)
			{
				ModelBiped t = this.getModelFromSlot(slotIn);
				t = getArmorModelHook(entityLivingBaseIn, itemstack, slotIn, t);
				this.setModelSlotVisible(t, slotIn);
				boolean flag = this.isLegSlot(slotIn);
				this.renderer.bindTexture(this.getArmorResource(entityLivingBaseIn, itemstack, slotIn, null));

				{
					if (itemarmor.hasOverlay(itemstack)) // Allow this for anything, not only cloth
					{
						int i = itemarmor.getColor(itemstack);
						float f = (float)(i >> 16 & 255) / 255.0F;
						float f1 = (float)(i >> 8 & 255) / 255.0F;
						float f2 = (float)(i & 255) / 255.0F;
						GlStateManager.color(this.colorR * f, this.colorG * f1, this.colorB * f2, this.alpha);
						t.render(entityLivingBaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
						this.renderer.bindTexture(this.getArmorResource(entityLivingBaseIn, itemstack, slotIn, "overlay"));
					}
					{ // Non-colored
						GlStateManager.color(this.colorR, this.colorG, this.colorB, this.alpha);
						t.render(entityLivingBaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
					} // Default
					if (!this.skipRenderGlint && itemstack.hasEffect())
					{
						renderEnchantedGlint(this.renderer, entityLivingBaseIn, t, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
					}
				}
			}
		}
	}

	@SuppressWarnings("incomplete-switch")
	protected void setModelSlotVisible(ModelBiped p_188359_1_, EntityEquipmentSlot slotIn)
	{
		this.setModelVisible(p_188359_1_);

		switch (slotIn)
		{
			case HEAD:
				p_188359_1_.bipedHead.showModel = true;
				p_188359_1_.bipedHeadwear.showModel = true;
				break;
			case CHEST:
				p_188359_1_.bipedBody.showModel = true;
				p_188359_1_.bipedRightArm.showModel = true;
				p_188359_1_.bipedLeftArm.showModel = true;
				break;
			case LEGS:
				p_188359_1_.bipedBody.showModel = true;
				p_188359_1_.bipedRightLeg.showModel = true;
				p_188359_1_.bipedLeftLeg.showModel = true;
				break;
			case FEET:
				p_188359_1_.bipedRightLeg.showModel = true;
				p_188359_1_.bipedLeftLeg.showModel = true;
		}
	}

	protected void setModelVisible(ModelBiped model)
	{
		model.setVisible(false);
	}

	@Override
	protected ModelBiped getArmorModelHook(net.minecraft.entity.EntityLivingBase entity, net.minecraft.item.ItemStack itemStack, EntityEquipmentSlot slot, ModelBiped model)
	{
		return net.minecraftforge.client.ForgeHooksClient.getArmorModel(entity, itemStack, slot, model);
	}

}
