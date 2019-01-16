package party.lemons.statues.handler;

import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

@SideOnly(Side.CLIENT)
public class StatueEffectHelper
{
	public static void doSculptEffect(BlockPos pos, IBlockState state)
	{
		for(int i = 0; i < 32 * 6; i++)
		{
			doHitEffect(pos, state, Minecraft.getMinecraft().world.rand);
		}

	}

	public static void doPaintEffect(BlockPos pos)
	{
		for(int i = 0; i < 15 * 6; i++)
		{
			doHitEffect(pos, Blocks.CONCRETE.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.GREEN), Minecraft.getMinecraft().world.rand);
			doHitEffect(pos, Blocks.CONCRETE.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.RED), Minecraft.getMinecraft().world.rand);
			doHitEffect(pos, Blocks.CONCRETE.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.YELLOW), Minecraft.getMinecraft().world.rand);
			doHitEffect(pos, Blocks.CONCRETE.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.BLUE), Minecraft.getMinecraft().world.rand);
		}
	}

	public static void doHitEffect(BlockPos pos, IBlockState state, Random rand)
	{
		float f = 0.1F;

		double xPos = pos.getX() - rand.nextDouble() * (-.15 - 1.15- f * 2.0F) + f + -.15;
		double yPos = pos.getY() - rand.nextDouble() * (0 - 2.15 - f * 2.0F) + f + 0;
		double zPos = pos.getZ() - rand.nextDouble() * (-0.15 - 1.15 - f * 2.0F) + f + -.15;

		Minecraft.getMinecraft().world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, xPos - 0.15, yPos, zPos - 0.15, 0, 0, 0, Block.getStateId(state));
	}
}
