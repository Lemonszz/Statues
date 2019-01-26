package party.lemons.statues.block.entity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import party.lemons.statues.statue.EntityBadIdea;
import party.lemons.statues.statue.EntityStatuePlayer;
import party.lemons.statues.statue.StatueInfo;

public class TileEntityStatue extends TileEntity
{
	@SideOnly(Side.CLIENT)
	private EntityBadIdea player;
	public String name = "";

	public StatueInfo info = new StatueInfo();

	public IBlockState state = Blocks.STONE.getDefaultState();
	public byte facing = 0;

	public InventoryBasic inventory = new InventoryBasic("", false, 6);

	@SideOnly(Side.CLIENT)
	public EntityStatuePlayer getStatueEntity()
	{
		if(player == null)
		{
			EntityStatuePlayer player = new EntityStatuePlayer(world, name, getPos());
			player.ticksExisted = 10;
			player.info = info;

			player.applySkin(name, state, facing);

			this.player = player;
		}

		return (EntityStatuePlayer)player;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		if(compound == null)
		{
			getStatueEntity();
			return;
		}

		super.readFromNBT(compound);
		inventory.clear();

		name = compound.getString("name");
		info.readFromNBT(compound.getCompoundTag("info"));

		state = NBTUtil.readBlockState(compound.getCompoundTag("state"));
		facing = compound.getByte("facing");

		NBTTagList nbttaglist = compound.getTagList("inv", 10);

		for (int i = 0; i < nbttaglist.tagCount(); ++i)
		{
			ItemStack itemstack = new ItemStack(nbttaglist.getCompoundTagAt(i));

			if (!itemstack.isEmpty())
			{
				this.inventory.setInventorySlotContents(i, itemstack);
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		compound.setString("name", name);
		compound.setTag("info", info.toNBT());
		compound.setTag("state", NBTUtil.writeBlockState(new NBTTagCompound(), state));
		compound.setByte("facing", facing);

		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < this.inventory.getSizeInventory(); ++i)
		{
			ItemStack itemstack = this.inventory.getStackInSlot(i);
			nbttaglist.appendTag(itemstack.writeToNBT(new NBTTagCompound()));
		}
		compound.setTag("inv", nbttaglist);

		return super.writeToNBT(compound);
	}

	@SideOnly(Side.CLIENT)
	public void updateModel()
	{
		if(player != null && world != null && world.isRemote)
		{
			((EntityStatuePlayer)player).applySkin(name, state, facing);
		}
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return writeToNBT(new NBTTagCompound());
	}

	@Override
	public void handleUpdateTag(NBTTagCompound tag)
	{
		readFromNBT(tag);
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound nbtTag = new NBTTagCompound();
		this.writeToNBT(nbtTag);
		return new SPacketUpdateTileEntity(getPos(), 1, nbtTag);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
		this.readFromNBT(packet.getNbtCompound());
	}



}
