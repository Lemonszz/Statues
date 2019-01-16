package party.lemons.statues.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import party.lemons.statues.block.entity.TileEntityStatue;

public class MessageUpdateEquipment implements IMessage
{
	public MessageUpdateEquipment()
	{}

	public BlockPos pos;
	public NBTTagCompound tags;

	public MessageUpdateEquipment(BlockPos pos, TileEntityStatue statue)
	{
		this.pos = pos;

		tags = statue.getUpdateTag();
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		tags = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());

		ByteBufUtils.writeTag(buf, tags);
	}

	public static class Handler implements IMessageHandler<MessageUpdateEquipment, IMessage>
	{
		@Override
		@SideOnly(Side.CLIENT)
		public IMessage onMessage(MessageUpdateEquipment message, MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(()->{
				TileEntity te = Minecraft.getMinecraft().world.getTileEntity(message.pos);

				if(te != null && te instanceof TileEntityStatue)
				{
					te.handleUpdateTag(message.tags);
					((TileEntityStatue)te).updateModel();
				}

			});

			return null;
		}
	}
}