package party.lemons.statues.init;

import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import party.lemons.statues.Statues;

@Mod.EventBusSubscriber(modid = Statues.MODID)
@GameRegistry.ObjectHolder(Statues.MODID)
public class StatuesSound
{
	public static final SoundEvent COPY = SoundEvents.ENTITY_PAINTING_BREAK;
	public static final SoundEvent PAINT = SoundEvents.ENTITY_PAINTING_BREAK;

	@SubscribeEvent
	public static void onRegisterSound(RegistryEvent.Register<SoundEvent> event)
	{
		event.getRegistry().registerAll(
				createSound("copy"),
				createSound("paint")
		);
	}


	private static SoundEvent createSound(String name)
	{
		ResourceLocation loc = new ResourceLocation(Statues.MODID, name);
		SoundEvent event = new SoundEvent(loc);
		event.setRegistryName(loc);

		return event;
	}
}
