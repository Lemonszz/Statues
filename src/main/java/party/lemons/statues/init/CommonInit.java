package party.lemons.statues.init;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import party.lemons.lemonlib.event.InitEvent;
import party.lemons.statues.Statues;
import party.lemons.statues.handler.StatuesGuiHandler;

@Mod.EventBusSubscriber(modid = Statues.MODID)
public class CommonInit
{
	@SubscribeEvent
	public static void onInit(InitEvent.Init event)
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(Statues.Instance, new StatuesGuiHandler());
	}
}
