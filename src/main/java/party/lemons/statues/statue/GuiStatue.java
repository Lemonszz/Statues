package party.lemons.statues.statue;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.world.World;
import party.lemons.statues.block.entity.ContainerStatue;
import party.lemons.statues.block.entity.TileEntityStatue;
import party.lemons.statues.gui.GuiScreenPlus;

public class GuiStatue extends GuiScreenPlus
{
	public final InventoryPlayer invg;
	public final TileEntityStatue tile;
	int wx,wy,wz;
	float ila,ira;

	public GuiStatue(EntityPlayer player, InventoryPlayer inv, final TileEntityStatue te, World par2, int x, int y, int z) {
		super(new ContainerStatue(player, inv, te),176,226,"statues:textures/gui-statue.png");
		invg = inv;
		tile = te;
		wx=x; wy=y; wz=z;

		/*
		addChild(new Gui2dScroller(121, 92, 43, 13, "statues:textures/gui-sculpt.png",13,13,243,0,ira=te.info.itemRightA,0) {
			@Override void onChange(){
				ira=te.info.itemRightA=(float) u;
				te.updateModel();
			}
		});

		addChild(new Gui2dScroller(12, 92, 43, 13, "statues:textures/gui-sculpt.png",13,13,243,0,ila=te.info.itemLeftA,0) {
			@Override void onChange(){
				ila=te.info.itemLeftA=(float) u;
				te.updateModel();
			}
		});
		*/
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	public void onGuiClosed(){
		/*try {
			Packet adjustStatue = Statues.packet.create(Packets.SCULPTURE_ADJUSTMENT)
					.writeInt(wx).writeInt(wy).writeInt(wz).writeFloat(tile.pose.itemLeftA).writeFloat(tile.pose.itemRightA);
			Statues.packet.sendToServer(adjustStatue);
		} catch(Exception e) { e.printStackTrace(); }
*/
		super.onGuiClosed();
	}

}