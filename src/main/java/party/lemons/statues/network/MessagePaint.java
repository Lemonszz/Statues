package party.lemons.statues.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import party.lemons.statues.block.BlockStatue;
import party.lemons.statues.block.entity.TileEntityStatue;
import party.lemons.statues.handler.StatueEffectHelper;
import party.lemons.statues.init.StatuesBlocks;
import party.lemons.statues.statue.DummyContainer;
import party.lemons.statues.statue.StatueInfo;

public class MessagePaint implements IMessage
{
	public MessagePaint()
	{}

	public BlockPos pos;

	public MessagePaint(BlockPos pos)
	{
		this.pos = pos;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
	}

	public static class Handler implements IMessageHandler<MessagePaint, IMessage>
	{
		@Override
		@SideOnly(Side.CLIENT)
		public IMessage onMessage(MessagePaint message, MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(()->{
				TileEntity te = Minecraft.getMinecraft().world.getTileEntity(message.pos);

				StatueEffectHelper.doPaintEffect(message.pos);
				if(te != null && te instanceof TileEntityStatue)
				{
					((TileEntityStatue) te).state = Blocks.BEDROCK.getDefaultState();
					((TileEntityStatue)te).updateModel();
				}
			});

			return null;
		}
	}
}