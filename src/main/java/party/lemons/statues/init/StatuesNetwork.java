package party.lemons.statues.init;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import party.lemons.lemonlib.event.InitEvent;
import party.lemons.statues.Statues;
import party.lemons.statues.network.MessagePaint;
import party.lemons.statues.network.MessageSendSculptInfo;
import party.lemons.statues.network.MessageUpdateEquipment;
import party.lemons.statues.network.MessageUpdateName;

@Mod.EventBusSubscriber(modid = Statues.MODID)
public class StatuesNetwork
{
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Statues.MODID);

	@SubscribeEvent
	public static void onPreInit(InitEvent.Pre event)
	{
		INSTANCE.registerMessage(MessageSendSculptInfo.Handler.class, MessageSendSculptInfo.class, id++, Side.SERVER);
		INSTANCE.registerMessage(MessagePaint.Handler.class, MessagePaint.class, id++, Side.CLIENT);
		INSTANCE.registerMessage(MessageUpdateName.Handler.class, MessageUpdateName.class, id++, Side.CLIENT);
		INSTANCE.registerMessage(MessageUpdateEquipment.Handler.class, MessageUpdateEquipment.class, id++, Side.CLIENT);
	}

	private static int id = 0;

}
