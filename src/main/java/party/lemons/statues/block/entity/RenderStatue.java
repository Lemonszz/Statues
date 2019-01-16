package party.lemons.statues.block.entity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.math.BlockPos;
import party.lemons.statues.block.BlockStatue;
import party.lemons.statues.statue.EntityStatuePlayer;

public class RenderStatue extends TileEntitySpecialRenderer<TileEntityStatue>
{
	RenderPlayerStatue renderer= new RenderPlayerStatue();

	public RenderStatue()
	{

	}

	@Override
	public void render(TileEntityStatue te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		BlockPos pos = te.getPos();
		IBlockState state = getWorld().getBlockState(pos);

		if(!(state.getBlock() instanceof BlockStatue))
			return;

		if(!state.getValue(BlockStatue.MAIN_PART))
			return;

		EntityStatuePlayer player = te.getStatueEntity();
		if(player == null)
			return;

		GlStateManager.pushMatrix();
		GlStateManager.translate(0.5, 0,  0.5);

		if(!player.info.name.isEmpty())
		{
			double maxDistance = 16;

			double d0 = player.getDistanceSq(renderer.getRenderManager().renderViewEntity);

			if (d0 <= (maxDistance * maxDistance))
			{
				boolean flag = player.isSneaking();
				float f = renderer.getRenderManager().playerViewY;
				float f1 = renderer.getRenderManager().playerViewX;
				boolean flag1 = renderer.getRenderManager().options.thirdPersonView == 2;
				float f2 = player.height + 0.5F - (flag ? 0.25F : 0.0F);

				EntityRenderer.drawNameplate(Minecraft.getMinecraft().fontRenderer, player.info.name, (float)x, (float)y + f2, (float)z, 0, f, f1, flag1, flag);
			}
		}

		GlStateManager.translate(x, y, z);
		GlStateManager.rotate(90 * te.facing, 0, 1, 0);

		renderer.doRender(player, 0, 0, 0, 0, alpha);


		GlStateManager.popMatrix();;
	}
}
