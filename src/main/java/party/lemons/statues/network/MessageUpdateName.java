package party.lemons.statues.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import party.lemons.statues.block.entity.TileEntityStatue;
import party.lemons.statues.handler.StatueEffectHelper;

public class MessageUpdateName implements IMessage
{
	public MessageUpdateName()
	{}

	public BlockPos pos;
	public String name;

	public MessageUpdateName(BlockPos pos, String name)
	{
		this.pos = pos;
		this.name = name;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		name = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());

		ByteBufUtils.writeUTF8String(buf, name);
	}

	public static class Handler implements IMessageHandler<MessageUpdateName, IMessage>
	{
		@Override
		@SideOnly(Side.CLIENT)
		public IMessage onMessage(MessageUpdateName message, MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(()->{
				TileEntity te = Minecraft.getMinecraft().world.getTileEntity(message.pos);

				if(te != null && te instanceof TileEntityStatue)
				{
					((TileEntityStatue) te).info.name = message.name;
					((TileEntityStatue)te).updateModel();
				}
			});

			return null;
		}
	}
}