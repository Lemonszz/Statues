package party.lemons.statues.block.entity;

import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.Entity;

public class ModelBipedStatue extends ModelPlayer
{
	public ModelBipedStatue(float par1, boolean smallArms) {
		super( par1, smallArms);

	}

	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
	{
		copyModelAngles(this.bipedLeftLeg, this.bipedLeftLegwear);
		copyModelAngles(this.bipedRightLeg, this.bipedRightLegwear);
		copyModelAngles(this.bipedLeftArm, this.bipedLeftArmwear);
		copyModelAngles(this.bipedRightArm, this.bipedRightArmwear);
		copyModelAngles(this.bipedBody, this.bipedBodyWear);
	}

}