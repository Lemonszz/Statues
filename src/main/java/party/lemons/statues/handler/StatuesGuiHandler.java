package party.lemons.statues.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import party.lemons.statues.block.entity.ContainerStatue;
import party.lemons.statues.block.entity.TileEntityStatue;
import party.lemons.statues.statue.DummyContainer;
import party.lemons.statues.statue.GuiSculpt;
import party.lemons.statues.statue.GuiStatue;

import javax.annotation.Nullable;

public class StatuesGuiHandler implements IGuiHandler
{
	public static final int SCUPLT = 0;
	public static final int STATUE = 1;

	@Nullable
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		switch(ID)
		{
			case SCUPLT:
				return new DummyContainer();
			case STATUE:
				return new ContainerStatue(player, player.inventory, (TileEntityStatue) world.getTileEntity(new BlockPos(x, y, z)));
		}

		return null;
	}

	@Nullable
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		switch(ID)
		{
			case SCUPLT:
				int face = MathHelper.floor((player.rotationYaw * 4F) / 360F + 0.5D) & 3;
				if(face == 2)
					face = 0;
				else if(face == 0)
					face = 2;

				return new GuiSculpt(world,x,y,z,player,face);
			case STATUE:
				return new GuiStatue(player, player.inventory, (TileEntityStatue) world.getTileEntity(new BlockPos(x, y, z)), world, x, y, z);
		}

		return null;
	}
}
