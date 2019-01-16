package party.lemons.statues.item;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import party.lemons.lemonlib.item.IItemModel;
import party.lemons.statues.Statues;
import party.lemons.statues.block.BlockStatue;
import party.lemons.statues.block.entity.TileEntityStatue;
import party.lemons.statues.handler.StatuesGuiHandler;
import party.lemons.statues.init.StatuesBlocks;

public class ItemHammer extends Item implements IItemModel
{
	public ItemHammer()
	{
		this.setMaxStackSize(1);
	}



	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if(worldIn.isRemote)
			return EnumActionResult.SUCCESS;

		IBlockState state = worldIn.getBlockState(pos);
		if(state.getBlockHardness(worldIn, pos) > 0)
		{
			if(state.equals(worldIn.getBlockState(pos.up())))
			{
				player.openGui(Statues.Instance, StatuesGuiHandler.SCUPLT, worldIn, pos.getX(), pos.getY(), pos.getZ());
			}

			if(state.equals(worldIn.getBlockState(pos.down())))
			{
				player.openGui(Statues.Instance, StatuesGuiHandler.SCUPLT, worldIn, pos.getX(), pos.getY() -1, pos.getZ());
			}
		}
		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}

}
