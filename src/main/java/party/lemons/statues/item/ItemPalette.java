package party.lemons.statues.item;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import party.lemons.lemonlib.item.IItemModel;
import party.lemons.statues.block.BlockStatue;
import party.lemons.statues.block.entity.TileEntityStatue;
import party.lemons.statues.init.StatuesNetwork;
import party.lemons.statues.init.StatuesSound;
import party.lemons.statues.network.MessagePaint;

public class ItemPalette extends Item implements IItemModel
{
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		IBlockState state = worldIn.getBlockState(pos);

		if(state.getBlock() instanceof BlockStatue)
		{
			if(!worldIn.isRemote)
			{
				TileEntityStatue statue;
				if(state.getValue(BlockStatue.MAIN_PART))
				{
					statue = (TileEntityStatue) worldIn.getTileEntity(pos);
				}
				else
				{
					statue = (TileEntityStatue) worldIn.getTileEntity(pos.down());
				}

				if(statue.state == Blocks.BEDROCK.getDefaultState())
					return EnumActionResult.FAIL;


				statue.state = Blocks.BEDROCK.getDefaultState();
				statue.updateModel();
				statue.markDirty();

				SPacketUpdateTileEntity spacketupdatetileentity = statue.getUpdatePacket();
				((EntityPlayerMP)player).connection.sendPacket(spacketupdatetileentity);
				worldIn.playSound(null, pos, StatuesSound.PAINT, SoundCategory.BLOCKS, 1, 1);

				StatuesNetwork.INSTANCE.sendToAllTracking(new MessagePaint(statue.getPos()), new NetworkRegistry.TargetPoint(
						worldIn.provider.getDimension(),
						statue.getPos().getX(),
						statue.getPos().getY(),
						statue.getPos().getZ(), 128));
			}

			return EnumActionResult.SUCCESS;
		}
		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}
}
