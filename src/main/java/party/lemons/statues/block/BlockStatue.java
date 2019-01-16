package party.lemons.statues.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemNameTag;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import party.lemons.lemonlib.item.IItemModel;
import party.lemons.lemonlib.util.InventoryUtil;
import party.lemons.statues.Statues;
import party.lemons.statues.block.entity.TileEntityStatue;
import party.lemons.statues.handler.StatuesGuiHandler;
import party.lemons.statues.init.StatuesItems;
import party.lemons.statues.init.StatuesNetwork;
import party.lemons.statues.init.StatuesSound;
import party.lemons.statues.item.ItemPalette;
import party.lemons.statues.network.MessageUpdateName;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockStatue extends BlockContainer
{
	public static final PropertyBool MAIN_PART = PropertyBool.create("main");

	public BlockStatue()
	{
		super(Material.ROCK);
		setLightOpacity(0);

		this.setDefaultState(this.blockState.getBaseState().withProperty(MAIN_PART, true));
	}


	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if(!state.getValue(MAIN_PART))
			pos = pos.down();

		if(worldIn.isRemote)
			return true;

		if(playerIn.getHeldItem(hand).getItem() instanceof ItemPalette)
			return false;

		if(playerIn.getHeldItem(hand).getItem() instanceof ItemNameTag)
		{
			ItemStack stack = playerIn.getHeldItem(hand);
			if(stack.hasDisplayName())
			{
				TileEntityStatue statue = (TileEntityStatue) worldIn.getTileEntity(pos);
				if(!statue.info.name.equals(stack.getDisplayName()))
				{
					statue.info.name = stack.getDisplayName();

					statue.markDirty();
					stack.shrink(1);

					MessageUpdateName pkt = new MessageUpdateName(pos, statue.info.name);
					StatuesNetwork.INSTANCE.sendToAllTracking(pkt, new NetworkRegistry.TargetPoint(worldIn.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 128));
					worldIn.playSound(null, pos, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.BLOCKS, 1, 1);

					return true;
				}
			}
		}
		playerIn.openGui(Statues.Instance, StatuesGuiHandler.STATUE, worldIn, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	@Override
	public int quantityDropped(Random random)
	{
		return 0;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	@Nullable
	@Override
	public TileEntity createNewTileEntity(World world, int i)
	{
		return new TileEntityStatue();
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		if(state.getValue(MAIN_PART))
		{
			worldIn.setBlockToAir(pos.up());
			TileEntity tileEntity = worldIn.getTileEntity(pos);
			if(tileEntity instanceof TileEntityStatue)
			{
				InventoryHelper.dropInventoryItems(worldIn, pos, ((TileEntityStatue) tileEntity).inventory);
			}
		}
		else
		{
			worldIn.setBlockToAir(pos.down());
		}

		super.breakBlock(worldIn, pos, state);
	}

	public IBlockState getStateFromMeta(int meta)
	{
		return meta == 0 ? getDefaultState() : getDefaultState().withProperty(MAIN_PART, false);
	}

	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(MAIN_PART) ? 0 : 1;
	}

	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, MAIN_PART);
	}

	@Override
	public boolean canProvidePower(IBlockState state)
	{
		return true;
	}
}
