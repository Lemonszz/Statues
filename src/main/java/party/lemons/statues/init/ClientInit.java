package party.lemons.statues.init;

import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import party.lemons.lemonlib.event.InitEvent;
import party.lemons.statues.Statues;
import party.lemons.statues.block.entity.RenderStatue;
import party.lemons.statues.block.entity.TileEntityStatue;

@Mod.EventBusSubscriber(modid = Statues.MODID, value = Side.CLIENT)
public class ClientInit
{
	@SubscribeEvent
	public static void init(InitEvent.Init event)
	{
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityStatue.class, new RenderStatue());
	}
}
