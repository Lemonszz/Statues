package party.lemons.statues.init;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import party.lemons.lemonlib.item.ItemRegistry;
import party.lemons.statues.Statues;
import party.lemons.statues.item.ItemHammer;
import party.lemons.statues.item.ItemPalette;

@Mod.EventBusSubscriber(modid = Statues.MODID)
@GameRegistry.ObjectHolder(Statues.MODID)
public class StatuesItems
{
	public static final Item PALETTE = Items.AIR;

	@SubscribeEvent
	public static void onRegisterItems(RegistryEvent.Register<Item> event)
	{
		ItemRegistry.setup(Statues.MODID, event.getRegistry(), CreativeTabs.TOOLS);

		ItemRegistry.registerItem(new ItemHammer(), "hammer");
		ItemRegistry.registerItem(new ItemPalette(), "palette");
	}
}
