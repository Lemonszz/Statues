package party.lemons.statues.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
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
import party.lemons.statues.init.StatuesSound;
import party.lemons.statues.statue.DummyContainer;
import party.lemons.statues.statue.StatueInfo;

import java.util.Random;

public class MessageSendSculptInfo implements IMessage
{
	public MessageSendSculptInfo()
	{}

	public StatueInfo info = new StatueInfo();
	public BlockPos pos;
	public String name;
	public int facing;

	public MessageSendSculptInfo(StatueInfo info, int facing, String name, BlockPos pos)
	{
		this.info = info;
		this.name = name;
		this.pos = pos;
		this.facing = facing;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		info.read(buf);
		name = ByteBufUtils.readUTF8String(buf);
		pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		facing = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		info.write(buf);
		ByteBufUtils.writeUTF8String(buf, name);

		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());

		buf.writeInt(facing);
	}

	public static class Handler implements IMessageHandler<MessageSendSculptInfo, IMessage>
	{
		@Override
		public IMessage onMessage(MessageSendSculptInfo message, MessageContext ctx)
		{
			WorldServer world = (WorldServer) ctx.getServerHandler().player.world;
			world.addScheduledTask(()->
			{
				if(!(ctx.getServerHandler().player.openContainer instanceof DummyContainer))
					return;

				world.playSound(null, message.pos, StatuesSound.COPY, SoundCategory.BLOCKS, 1, 1);

				IBlockState state= world.getBlockState(message.pos);

				world.setBlockState(message.pos, StatuesBlocks.STATUE.getDefaultState());
				world.setBlockState(message.pos.up(), StatuesBlocks.STATUE.getDefaultState().withProperty(BlockStatue.MAIN_PART, false));

				TileEntityStatue te = (TileEntityStatue) world.getTileEntity(message.pos);
				te.state = state;
				te.name = message.name;
				te.info = message.info;
				try
				{
					te.facing = (byte) message.facing;
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				te.markDirty();
			});

			return null;
		}
	}
}